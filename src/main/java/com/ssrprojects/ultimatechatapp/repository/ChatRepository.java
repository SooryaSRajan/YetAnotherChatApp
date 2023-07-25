package com.ssrprojects.ultimatechatapp.repository;

import com.ssrprojects.ultimatechatapp.model.UserChats;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CassandraRepository<UserChats, String> {

    @Query("SELECT * FROM user_chats WHERE participating_users CONTAINS ?0 AND participating_users CONTAINS ?1")
    UserChats findByParticipatingUsers(String userA, String userB);

}
