package com.ssrprojects.ultimatechatapp.service.UserService;

import com.ssrprojects.ultimatechatapp.entity.User;
import model.SignUpRequest;
import model.VerificationRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    void addUser(User user);

    User getUserByUsername(String username);

    Pair<Boolean, String> provisionNewUser(SignUpRequest signUpRequest);

    UsernamePasswordAuthenticationToken getAuthenticationToken(String username);

    Pair<Boolean, String> verifyUser(VerificationRequest verificationRequest);
}
