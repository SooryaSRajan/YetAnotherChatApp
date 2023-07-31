package com.ssrprojects.ultimatechatapp.service.FileService;

import com.ssrprojects.ultimatechatapp.entity.FileData.ProfilePicture;
import com.ssrprojects.ultimatechatapp.entity.User;
import com.ssrprojects.ultimatechatapp.repository.ProfilePictureRepository;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ProfilePictureService implements FileService<ProfilePicture> {

    ProfilePictureRepository profilePictureRepository;
    UserService userService;

    public ProfilePictureService(ProfilePictureRepository profilePictureRepository, UserService userService) {
        this.profilePictureRepository = profilePictureRepository;
        this.userService = userService;
    }

    private boolean isImageMediaType(MediaType mediaType) {
        return mediaType != null
                && mediaType.getType().equalsIgnoreCase("image")
                && (mediaType.getSubtype().equalsIgnoreCase("jpeg")
                || mediaType.getSubtype().equalsIgnoreCase("png")
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public ProfilePicture saveProfilePictureForUser(MultipartFile multipartFile, String userId) {
        User user = userService.getUserByUsername(userId);

        if (user == null) throw new RuntimeException("User not found");

        String contentType = multipartFile.getContentType();

        if (contentType == null) {
            throw new RuntimeException("Content type is null");
        }

        MediaType mediaType = MediaType.parseMediaType(contentType);

        if (!isImageMediaType(mediaType)) {
            throw new RuntimeException("File is not an image");
        }

        try {
            ProfilePicture profilePicture = profilePictureRepository.findByUserId(userId);

            if (profilePicture != null) {
                profilePicture.setProfilePicture(multipartFile.getBytes());
                profilePicture.setContentType(mediaType);
                profilePicture = saveFile(profilePicture);
                user.setProfilePicture(profilePicture.getProfilePictureId());
                userService.saveOrUpdateUser(user);

                log.info("Profile picture exists, updating profile picture for user {}", userId);

                return profilePicture;
            }

            profilePicture = ProfilePicture
                    .builder()
                    .userId(userId)
                    .profilePicture(multipartFile.getBytes())
                    .contentType(mediaType)
                    .build();

            profilePicture = saveFile(profilePicture);
            user.setProfilePicture(profilePicture.getProfilePictureId());
            userService.saveOrUpdateUser(user);

            log.info("Profile picture does not exist, creating profile picture for user {}", userId);

            return profilePicture;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProfilePicture saveFile(ProfilePicture file) {
        return profilePictureRepository.save(file);
    }

    @Transactional
    public ProfilePicture getProfilePictureForUser(String userId) {
        return profilePictureRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public ProfilePicture getFile(String id) {
        return profilePictureRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteFile(String id) {
        profilePictureRepository.deleteById(id);
    }
}
