package com.ssrprojects.ultimatechatapp.model.keys;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;

@PrimaryKeyClass
@Data
public class ChatRelationshipKey implements Serializable {

    @PrimaryKeyColumn(name = "user_a_id", type = PrimaryKeyType.PARTITIONED)
    private String userAId; // User A's ID

    @PrimaryKeyColumn(name = "user_b_id", type = PrimaryKeyType.PARTITIONED)
    private String userBId; // User B's ID

}