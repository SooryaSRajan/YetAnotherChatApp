package com.ssrprojects.ultimatechatapp.model;

import com.ssrprojects.ultimatechatapp.model.enums.MessageAssetType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Chats chats;

    private String message;

    private Long senderId;

    private Long receiverId;

    private Date sentAt;

    private Date receivedAt;

    private Date seenAt;

    private Boolean isSeen;

    private Boolean isReceived;

    private MessageAssetType assetType;

    private Boolean isEdited;

    private Date editedAt;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
        if(isEdited) {
            this.editedAt = new Date();
        }
    }
}
