package com.ssrprojects.ultimatechatapp.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table("user_chats")
@Data
public class UserChats {

    @PrimaryKey
    private String id;

    private List<String> participatingUsers;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.UDT, userTypeName = "chat_messages")
    private List<Chat> chats;

    public void addToChats(Chat chat) {
        this.chats.add(chat);
    }

    public void addParticipatingUser(String userId) {
        this.participatingUsers.add(userId);
    }

    public UserChats() {
        id = UUID.randomUUID().toString();
        participatingUsers = new ArrayList<>();
        chats = new ArrayList<>();
    }
}
