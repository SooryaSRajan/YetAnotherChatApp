package com.ssrprojects.ultimatechatapp.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

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

    private List<Chat> chats;
}
