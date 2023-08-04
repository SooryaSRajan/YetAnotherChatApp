package com.ssrprojects.ultimatechatapp.entity.FileData;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

@Entity
@Table(name = "profile_picture")
@Data
@Builder
@AllArgsConstructor
public class ProfilePicture {

    @Id
    @Column(name = "profile_picture_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String profilePictureId;

    @Lob
    @Column(name = "profile_picture", length = 2097152)
    private byte[] profilePicture;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private MediaType contentType;

    public ProfilePicture(byte[] profilePicture, String userId, MediaType contentType) {
        this.profilePicture = profilePicture;
        this.userId = userId;
        this.contentType = contentType;
    }

    public ProfilePicture() {

    }
}
