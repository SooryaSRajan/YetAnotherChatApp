package com.ssrprojects.ultimatechatapp.service.UserService;

import com.ssrprojects.ultimatechatapp.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    void addUser(User user);

}
