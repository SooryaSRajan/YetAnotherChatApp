package com.ssrprojects.ultimatechatapp.model;

import com.ssrprojects.ultimatechatapp.model.enums.MessageAssetType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private Long senderId;

    private Long receiverId;

    private Date sentAt;

    private Date receivedAt;

    private Date seenAt;

    private Boolean isSeen;

    private Boolean isReceived;

    private MessageAssetType assetType;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
