package com.ssrprojects.ultimatechatapp.repository;

import com.ssrprojects.ultimatechatapp.entity.FileData.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilePictureRepository extends JpaRepository<ProfilePicture, String> {
    ProfilePicture findByUserId(String userId);
}
