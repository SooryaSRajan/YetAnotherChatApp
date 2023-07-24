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
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Chats chats;

    private String message = "";

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    private Date sentAt = null;

    private Date receivedAt = null;

    private Date seenAt = null;

    private Boolean isSeen = false;

    private Boolean isReceived = false;

    private MessageAssetType assetType = MessageAssetType.TEXT;

    private Boolean isEdited = false;

    private Date editedAt = null;

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
