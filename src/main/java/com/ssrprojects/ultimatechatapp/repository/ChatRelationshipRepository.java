package com.ssrprojects.ultimatechatapp.repository;

import com.ssrprojects.ultimatechatapp.entity.ChatRelationship;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRelationshipRepository extends CassandraRepository<ChatRelationship, String> {

    Optional<ChatRelationship> findByChatRelationshipKeyUserAIdAndChatRelationshipKeyUserBId(String userAId, String userBId);

}
