package com.ssrprojects.ultimatechatapp.service.UserService;

import com.ssrprojects.ultimatechatapp.entity.User;
import model.SignUpRequest;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    void addUser(User user);

    User getUserByUsername(String username);

    Pair<Boolean, String> provisionNewUser(SignUpRequest signUpRequest);
}
