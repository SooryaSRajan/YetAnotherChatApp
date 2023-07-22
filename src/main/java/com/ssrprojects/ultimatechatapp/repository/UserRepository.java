package com.ssrprojects.ultimatechatapp.repository;

import com.ssrprojects.ultimatechatapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);

}
