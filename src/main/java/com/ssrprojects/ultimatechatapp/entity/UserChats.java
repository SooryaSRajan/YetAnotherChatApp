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
        if (this.chats == null)
            this.chats = new ArrayList<>();
        this.chats.add(chat);
    }


    public void addParticipatingUser(String userId) {
        if (this.participatingUsers == null)
            this.participatingUsers = new ArrayList<>();
        this.participatingUsers.add(userId);
    }

    public List<String> getParticipatingUsers() {
        if (participatingUsers == null)
            participatingUsers = new ArrayList<>();
        return participatingUsers;
    }

    public List<Chat> getChats() {
        if (chats == null)
            chats = new ArrayList<>();
        return chats;
    }

    public UserChats() {
        id = UUID.randomUUID().toString();
        participatingUsers = new ArrayList<>();
        chats = new ArrayList<>();
    }
}
