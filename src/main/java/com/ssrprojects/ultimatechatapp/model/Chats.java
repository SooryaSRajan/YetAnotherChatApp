package com.ssrprojects.ultimatechatapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "user_chats")
public class Chats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<User> users;

    @OneToMany
    private List<Chat> chats;

    public Chats() {

    }

}
