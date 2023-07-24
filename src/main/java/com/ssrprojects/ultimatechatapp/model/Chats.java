package com.ssrprojects.ultimatechatapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "user_chats")
public class Chats {

    @Id
    @Column(name = "user_chats_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "user_chat_details",
            joinColumns = @JoinColumn(name = "user_chats_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    @OneToMany(mappedBy = "chats")
    private List<Chat> chats;

    public Chats() {
        chats = new ArrayList<>();
    }

}
