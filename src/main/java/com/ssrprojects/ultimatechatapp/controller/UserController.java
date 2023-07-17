package com.ssrprojects.ultimatechatapp.controller;

import com.ssrprojects.ultimatechatapp.model.User;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "hey there";
    }


    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    //sample URL: http://localhost:8080/user/addUser?username=ssr&password=ssr&email=ssr&firstName=ssr&lastName=ssr

    @GetMapping("/addUser?username={username}&password={password}&email={email}&firstName={firstName}&lastName={lastName}")
    public void addUser(@PathVariable String username, @PathVariable String password, @PathVariable String email, @PathVariable String firstName, @PathVariable String lastName) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userService.addUser(user);
    }
}
