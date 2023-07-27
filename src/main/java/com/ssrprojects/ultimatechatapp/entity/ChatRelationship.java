package com.ssrprojects.ultimatechatapp.entity;

import com.ssrprojects.ultimatechatapp.entity.keys.ChatRelationshipKey;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("chat_relationship")
@Data
public class ChatRelationship {

    @PrimaryKey
    ChatRelationshipKey chatRelationshipKey;

    //set column name as chat_id
    @Column("chat_id")
    private String chatId; // Unique chat ID
}

