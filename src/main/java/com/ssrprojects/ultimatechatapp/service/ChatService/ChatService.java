package com.ssrprojects.ultimatechatapp.service.ChatService;

import com.ssrprojects.ultimatechatapp.model.Chat;
import com.ssrprojects.ultimatechatapp.model.User;

import java.util.List;

public interface ChatService {


    void addChat(Chat chat);
    List<Chat> getChatsForUsers(User a, User b);
    void addChatForUsers(User a, User b, Chat chat);
    void deleteChatForUsers(User a, User b, Chat chat);
    void deleteChatForUsersById(User a, User b, Long id);
    void editChatForUsers(User a, User b, Chat chat);

}
