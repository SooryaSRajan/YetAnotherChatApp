package com.ssrprojects.ultimatechatapp.entity.FileData;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "profile_picture")
@Getter
@Builder
public class ProfilePicture {

    @Id
    @Column(name = "profile_picture_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String profilePictureId;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "BLOB", length = 2097152)
    private byte[] profilePicture;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String contentType;

    public ProfilePicture() {

    }

    public ProfilePicture(String userId, byte[] profilePicture) {
        this.profilePictureId = userId;
        this.profilePicture = profilePicture;
    }

}
