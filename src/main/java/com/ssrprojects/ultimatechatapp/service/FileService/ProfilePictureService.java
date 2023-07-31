package com.ssrprojects.ultimatechatapp.service.FileService;

import com.ssrprojects.ultimatechatapp.entity.FileData.ProfilePicture;
import com.ssrprojects.ultimatechatapp.entity.User;
import com.ssrprojects.ultimatechatapp.repository.ProfilePictureRepository;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ProfilePictureService implements FileService<ProfilePicture> {

    ProfilePictureRepository profilePictureRepository;
    UserService userService;

    @Transactional(rollbackFor = Exception.class)
    public ProfilePicture saveProfilePictureForUser(MultipartFile multipartFile, String userId) {
        User user = userService.getUserByUsername(userId);

        if (user == null) throw new RuntimeException("User not found");

        String contentType = multipartFile.getContentType();

        if (contentType != null && !contentType.equals("image/png") && !contentType.equals("image/jpeg")) {
            throw new RuntimeException("File is not an image");
        }

        try {
            ProfilePicture profilePicture = ProfilePicture
                    .builder()
                    .userId(userId)
                    .profilePicture(multipartFile.getBytes())
                    .contentType(contentType)
                    .build();

            profilePicture = saveFile(profilePicture);
            user.setProfilePicture(profilePicture.getProfilePictureId());
            userService.saveOrUpdateUser(user);

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

    public ProfilePicture getProfilePictureForUser(String userId) {
        return profilePictureRepository.findByUserId(userId);
    }

    @Override
    public ProfilePicture getFile(String id) {
        return profilePictureRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteFile(String id) {
        profilePictureRepository.deleteById(id);
    }
}
