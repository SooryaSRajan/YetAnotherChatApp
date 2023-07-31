package com.ssrprojects.ultimatechatapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssrprojects.ultimatechatapp.entity.enums.ProfileStatus;
import com.ssrprojects.ultimatechatapp.entity.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.time.LocalDateTime;
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
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain alphanumeric characters and underscores")
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    @JsonIgnore
    @Email
    private String email;

    @Column(nullable = false, unique = true)
    private String displayName;

    private String profilePicture;

    private String bio;

    private String verificationToken;

    private Boolean isVerified = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "verification_token_expiration_date")
    private LocalDateTime verificationTokenExpirationDate;

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
        profilePicture = "";
        bio = "";
        friends = new ArrayList<>();
        pendingRequests = new ArrayList<>();
        sentRequests = new ArrayList<>();
    }

    @ElementCollection(targetClass = Roles.class)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private List<Roles> roles;

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
        if (!isEmpty(verificationToken)) {
            isVerified = false;
            verificationTokenExpirationDate = LocalDateTime.now().plusMinutes(15);
        }
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
        if (verified) {
            verificationToken = "";
            verificationTokenExpirationDate = null;
        }
    }

    public boolean hasVerificationExpired() {
        return LocalDateTime.now().isAfter(verificationTokenExpirationDate);
    }

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

    //Use for email verification
    @Override
    public boolean isEnabled() {
        return isVerified;
    }
}
