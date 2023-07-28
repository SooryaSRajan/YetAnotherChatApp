package model;

import lombok.Data;

@Data
//TODO: Add more fields, bio, profile picture, etc.
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String displayName;
}
