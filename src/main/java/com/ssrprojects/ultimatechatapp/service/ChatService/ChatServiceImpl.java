package com.ssrprojects.ultimatechatapp.service.ChatService;

import com.ssrprojects.ultimatechatapp.model.Chat;
import com.ssrprojects.ultimatechatapp.model.User;
import com.ssrprojects.ultimatechatapp.model.UserChats;
import com.ssrprojects.ultimatechatapp.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public UserChats provisionChatsForUsers(User userA, User userB) {

        //UserChats builder
        UserChats userChats = UserChats.builder()
                .participatingUsers(List.of(userA.getId(), userB.getId()))
                .build();

        return null;
    }

    @Override
    public List<Chat> getChatsForUsers(User userA, User userB) {
        return null;
    }

    @Override
    public void addChatForUsers(User userA, User userB, Chat chat) {

    }

    @Override
    public void sendChatFromUser(User userA, User userB, Chat chat) {

    }

    @Override
    public void deleteChatForUsers(User userA, User userB, Chat chat) {

    }

    @Override
    public void deleteChatForUsersById(User userA, User userB, Long id) {

    }

    @Override
    public void editChatForUsers(User userA, User userB, Chat chat) {

    }
}
