package com.ssrprojects.ultimatechatapp.controller;

import com.ssrprojects.ultimatechatapp.entity.User;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    @RequestMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //sample URL: http://localhost:8080/user/addUser/username/password/email/firstName/lastName
    @GetMapping("/addUser/{username}/{password}/{email}/{firstName}/{lastName}")
    public ResponseEntity<String> addUser(@PathVariable String username, @PathVariable String password, @PathVariable String email, @PathVariable String firstName, @PathVariable String lastName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userService.addUser(user);

        return ResponseEntity.ok("User added successfully");

    }
}
