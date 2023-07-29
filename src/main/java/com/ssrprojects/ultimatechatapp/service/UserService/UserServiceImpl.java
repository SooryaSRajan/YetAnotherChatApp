package com.ssrprojects.ultimatechatapp.service.UserService;

import com.ssrprojects.ultimatechatapp.entity.User;
import com.ssrprojects.ultimatechatapp.entity.enums.Roles;
import com.ssrprojects.ultimatechatapp.model.quartz.JobDescriptor;
import com.ssrprojects.ultimatechatapp.model.quartz.TriggerDescriptor;
import com.ssrprojects.ultimatechatapp.repository.UserRepository;
import com.ssrprojects.ultimatechatapp.service.MailService.EmailService;
import com.ssrprojects.ultimatechatapp.service.QuartzService.QuartzService;
import com.ssrprojects.ultimatechatapp.utils.TokenGenerator;
import com.ssrprojects.ultimatechatapp.model.request.SignUpRequest;
import com.ssrprojects.ultimatechatapp.model.request.VerificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static quartz.action.Jobs.DELETE_UNVERIFIED_USERS;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final QuartzService quartzService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, QuartzService quartzService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.quartzService = quartzService;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, AmqpException.class})
    public Pair<Boolean, String> provisionNewUser(SignUpRequest signUpRequest) throws RuntimeException {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return Pair.of(false, "Username is already taken");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return Pair.of(false, "Email is already in use");
        }

        String verificationToken = TokenGenerator.generateToken(8);

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        user.setDisplayName(signUpRequest.getDisplayName());
        user.setRoles(List.of(Roles.USER));
        user.setVerificationToken(passwordEncoder.encode(verificationToken));

        user = userRepository.save(user);

        if (user.getId() == null) {
            return Pair.of(false, "User could not be saved");
        }

        boolean wasMailSuccessful = emailService.sendVerificationEmail(user, verificationToken);

        quartzService.createJob(DELETE_UNVERIFIED_USERS.name(), getTriggerDescriptorForUserDeletion(user));

        if (!wasMailSuccessful) {
            throw new RuntimeException("Email could not be sent");
        }

        return Pair.of(true, "User registered successfully");
    }

    private JobDescriptor getTriggerDescriptorForUserDeletion(User user) {
        String actionName = DELETE_UNVERIFIED_USERS.getJobTitle() + " - " + user.getId();

        final TriggerDescriptor triggerDescriptor = new TriggerDescriptor();
        triggerDescriptor.setName(actionName);
        triggerDescriptor.setGroup(DELETE_UNVERIFIED_USERS.name());
        //TODO: Change to 15 minutes
        triggerDescriptor.setFireTime(LocalDateTime.now().plusMinutes(20));

        HashMap<String, Object> jobDataMap = new HashMap<>();
        jobDataMap.put("userId", user.getId());

        log.info("Unverified user deletion task scheduled, trigger descriptor: {}", triggerDescriptor);

        return JobDescriptor
                .builder()
                .name(actionName)
                .group(DELETE_UNVERIFIED_USERS.name())
                .triggerDescriptors(List.of(triggerDescriptor))
                .data(jobDataMap)
                .build();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;
    }

    @Transactional(readOnly = true)
    public UsernamePasswordAuthenticationToken getAuthenticationToken(String username) {
        UserDetails userDetails = loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
        );
    }

    @Override
    public Pair<Boolean, String> verifyUser(VerificationRequest verificationRequest) {

        User user = getUserByUsername(verificationRequest.getUsername());
        if (user == null) {
            return Pair.of(false, "User not found");
        }

        if (user.getIsVerified()) {
            return Pair.of(false, "User is already verified");
        }

        if (user.hasVerificationExpired()) {
            userRepository.delete(user);
            return Pair.of(false, "Verification token has expired");
        }

        if (user.getEmail().equals(verificationRequest.getEmail())
                && passwordEncoder.matches(verificationRequest.getVerificationCode(), user.getVerificationToken())) {
            user.setVerified(true);
            userRepository.save(user);
            return Pair.of(true, "User verified successfully");
        }

        return Pair.of(false, "Verification failed, please check your email and verification code");
    }

    @Override
    public void removeUnverifiedUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && !user.getIsVerified() && user.hasVerificationExpired()) {
            log.info("Removing unverified user: {}", user);
            userRepository.delete(user);
            return;
        }
        log.error("User not found or is already verified: {}", user);
    }
}
