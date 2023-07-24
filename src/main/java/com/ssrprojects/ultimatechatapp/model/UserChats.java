package com.ssrprojects.ultimatechatapp.model;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table("user_chats")
@Data
@Builder
public class UserChats {

    @PrimaryKey
    private final String id;

    @PrimaryKeyColumn(name = "participating_users", type = PrimaryKeyType.CLUSTERED)
    private List<String> participatingUsers;

    private List<Chat> chats;

    public UserChats() {
        id = Uuids.random().toString();
    }
}
