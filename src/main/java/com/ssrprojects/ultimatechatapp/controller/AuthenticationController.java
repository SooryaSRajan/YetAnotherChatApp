package com.ssrprojects.ultimatechatapp.controller;


import com.ssrprojects.ultimatechatapp.config.security.JWTUtil;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import jakarta.validation.Valid;
import com.ssrprojects.ultimatechatapp.model.request.LoginRequest;
import com.ssrprojects.ultimatechatapp.model.request.SignUpRequest;
import com.ssrprojects.ultimatechatapp.model.request.VerificationRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtTokenUtil;
    private final UserService userService;

    public AuthenticationController(AuthenticationManager authenticationManager, JWTUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body("Error: Cannot authenticate user");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenUtil.generateToken(authentication);

        return ResponseEntity.ok(jwt);
    }

    //sample URL: http://localhost:8080/api/authentication/signUp
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        try {
            Pair<Boolean, String> result = userService.provisionNewUser(signUpRequest);

            if (!result.getFirst()) {
                return ResponseEntity.badRequest().body(result.getSecond());
            } else {
                return ResponseEntity.ok(result.getSecond());
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@Valid @RequestBody VerificationRequest verificationRequest) {
        Pair<Boolean, String> result = userService.verifyUser(verificationRequest);

        if (!result.getFirst()) {
            return ResponseEntity.badRequest().body(result.getSecond());
        } else {
            return ResponseEntity.ok(result.getSecond());
        }
    }

}
