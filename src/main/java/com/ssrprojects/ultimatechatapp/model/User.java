package com.ssrprojects.ultimatechatapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssrprojects.ultimatechatapp.model.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "user_details")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    private String password;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    private String email;

    private String displayName;

    private Integer age;

    private String profilePicture;

    private String bio;

    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus = ProfileStatus.PUBLIC;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnore
    private List<User> friends;

    @ManyToMany
    @JoinTable(
            name = "pending_requests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "pending_request_user_id")
    )
    @JsonIgnore
    private List<User> pendingRequests;

    @ManyToMany
    @JoinTable(
            name = "sent_requests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sent_request_user_id")
    )
    @JsonIgnore
    private List<User> sentRequests;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    private List<Chats> userChats;

    //TODO: Add my posts, stories,

    public User() {

    }
}
