package com.ssrprojects.ultimatechatapp.controller;

import com.ssrprojects.ultimatechatapp.entity.FileData.ProfilePicture;
import com.ssrprojects.ultimatechatapp.service.FileService.ProfilePictureService;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final ProfilePictureService profilePictureService;

    public UserController(ProfilePictureService profilePictureService) {
        this.profilePictureService = profilePictureService;
    }

    @GetMapping("/profilePicture/user/{userId}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String userId) {
        ProfilePicture profilePicture = profilePictureService.getProfilePictureForUser(userId);
        return processProfilePicture(profilePicture);
    }

    @GetMapping("/profilePicture/{profilePictureID}")
    public ResponseEntity<byte[]> getProfilePictureById(@PathVariable String profilePictureID) {
        ProfilePicture profilePicture = profilePictureService.getFile(profilePictureID);
        return processProfilePicture(profilePicture);
    }

    private ResponseEntity<byte[]> processProfilePicture(ProfilePicture profilePicture) {
        if (profilePicture == null || profilePicture.getProfilePicture() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageData = profilePicture.getProfilePicture();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(profilePicture.getContentType());

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @PostMapping("/profilePicture/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) securityContext.getAuthentication().getPrincipal();

        try {
            profilePictureService.saveProfilePictureForUser(file, userDetails.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body("/api/user/profilePicture/user/" + userDetails.getUsername());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Could not upload the file: " + file.getOriginalFilename() + "!");
        }
    }
}
