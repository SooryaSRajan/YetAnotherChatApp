package com.ssrprojects.ultimatechatapp.utils;

import java.util.UUID;

public class TokenGenerator {

    public static String generateToken(Integer maxLength) {
        String uuid = UUID.randomUUID().toString();
        String token = uuid.replaceAll("-", "");
        return token.substring(0, maxLength);
    }

}
