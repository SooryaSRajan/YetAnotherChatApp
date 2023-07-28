package com.ssrprojects.ultimatechatapp.model;

import lombok.Getter;

@Getter
public class VerificationRequest {
    private String username;
    private String email;
    private String verificationCode;
}
