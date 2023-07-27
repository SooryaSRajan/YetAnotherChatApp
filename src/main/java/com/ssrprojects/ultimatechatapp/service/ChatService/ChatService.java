package com.ssrprojects.ultimatechatapp.service.ChatService;

import com.ssrprojects.ultimatechatapp.entity.Chat;
import com.ssrprojects.ultimatechatapp.entity.User;
import com.ssrprojects.ultimatechatapp.entity.UserChats;

import java.util.List;

public interface ChatService {
    UserChats provisionOrGetChatsForUsers(User userA, User userB);

    List<Chat> getChatsForUsers(User userA, User userB);

    void addChatForUsers(User userA, User userB, Chat chat);

    void sendChatFromUser(User userA, User userB, Chat chat);

    void deleteChatForUsersById(User userA, User userB, String id);

    void deleteChatForUsers(User userA, User userB, Chat chat);

    void editChatForUsers(User userA, User userB, Chat chat);
}
