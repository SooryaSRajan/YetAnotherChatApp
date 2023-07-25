package com.ssrprojects.ultimatechatapp.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table("user_chats")
@Builder
@Getter
public class UserChats {

    @PrimaryKey
    @Builder.Default
    private final String id = UUID.randomUUID().toString();

    private List<String> participatingUsers;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.UDT, userTypeName = "chat_messages")
    @Builder.Default
    private List<Chat> chats = new ArrayList<>();

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public void addToChats(Chat chat) {
        this.chats.add(chat);
    }
}
