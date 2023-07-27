package com.ssrprojects.ultimatechatapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssrprojects.ultimatechatapp.model.enums.ProfileStatus;
import com.ssrprojects.ultimatechatapp.model.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "user_details")
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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

    //TODO: Add my posts, stories,

    public User() {
        username = "";
        password = "";
        email = "";
        displayName = "";
        age = 0;
        profilePicture = "";
        bio = "";
        friends = new ArrayList<>();
        pendingRequests = new ArrayList<>();
        sentRequests = new ArrayList<>();
    }

    @ElementCollection(targetClass = Roles.class)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "user_roles", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<Roles> roles = List.of(Roles.USER);

    @Override
    public Collection<Roles> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
