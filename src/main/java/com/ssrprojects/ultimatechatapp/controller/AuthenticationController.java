package com.ssrprojects.ultimatechatapp.controller;


import com.ssrprojects.ultimatechatapp.config.security.JWTUtil;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    JWTUtil jwtTokenUtil;
    UserService userService;


}
