package com.ssrprojects.ultimatechatapp.repository;

import com.ssrprojects.ultimatechatapp.model.Chats;
import com.ssrprojects.ultimatechatapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatsRepository extends JpaRepository<Chats, Long> {

    @Query("SELECT c FROM Chats c WHERE :user1 IN ELEMENTS(c.users) AND :user2 IN ELEMENTS(c.users)")
    Chats findChatsByUsers(User user1, User user2);

}
