package com.ssrprojects.ultimatechatapp.repository;

import com.ssrprojects.ultimatechatapp.model.UserChats;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CassandraRepository<UserChats, String> {

    @Query("SELECT * FROM user_chats WHERE participatingusers CONTAINS ?0 AND participatingusers CONTAINS ?1 ALLOW FILTERING")
    UserChats findByParticipatingUsers(String userA, String userB);

}
