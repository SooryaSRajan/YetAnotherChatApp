package com.ssrprojects.ultimatechatapp.model;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ssrprojects.ultimatechatapp.model.enums.MessageAssetType;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Table("chat_messages")
@Data
public class Chat {

    @PrimaryKey
    private String id;

    private String senderId;

    private String receiverId;

    private String content = "";

    private Date createdAt = new Date();

    private Date sentAt = null;

    private Date receivedAt = null;

    private Date seenAt = null;

    private Boolean isSeen = false;

    private Boolean isReceived = false;

    private MessageAssetType assetType = MessageAssetType.TEXT;

    private Boolean isEdited = false;

    private Date editedAt = null;

    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
        if (isEdited) {
            this.editedAt = new Date();
        }
    }

    public Chat() {
        id = UUID.randomUUID().toString();
    }

}

