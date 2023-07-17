package com.ssrprojects.ultimatechatapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Table(name = "user_details")
public class User {

    @Id
    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    public User() {

    }
}
