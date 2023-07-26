package com.ssrprojects.ultimatechatapp.repository;

import com.ssrprojects.ultimatechatapp.model.UserChats;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends CassandraRepository<UserChats, String> {

    @Query("SELECT * FROM user_chats WHERE participatingusers CONTAINS ?0 AND participatingusers CONTAINS ?1 ALLOW FILTERING")
    Optional<UserChats> findByParticipatingUsers(String userA, String userB);

}
