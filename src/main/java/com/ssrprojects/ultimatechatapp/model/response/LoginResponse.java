package com.ssrprojects.ultimatechatapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class LoginResponse {
    @JsonProperty("token")
    String token;

    @JsonProperty("displayName")
    String displayName;
}
