package com.ssrprojects.ultimatechatapp.service.ChatService;

import com.ssrprojects.ultimatechatapp.model.Chat;
import com.ssrprojects.ultimatechatapp.model.Chats;
import com.ssrprojects.ultimatechatapp.model.User;
import com.ssrprojects.ultimatechatapp.repository.ChatRepository;
import com.ssrprojects.ultimatechatapp.repository.ChatsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatsRepository chatsRepository;

    public ChatServiceImpl(ChatRepository chatRepository, ChatsRepository chatsRepository) {
        this.chatRepository = chatRepository;
        this.chatsRepository = chatsRepository;
    }

    @Override
    public void addChat(Chat chat) {
        chatRepository.save(chat);
    }

    @Override
    @Transactional
    public List<Chat> getChatsForUsers(User a, User b) {
        return chatsRepository.findChatsByUsers(a, b).getChats();
    }

    @Override
    @Transactional
    public void addChatForUsers(User a, User b, Chat chat) {
        Chats chats = chatsRepository.findChatsByUsers(a, b);
        chats.getChats().add(chat);
        chatsRepository.save(chats);
    }

    @Override
    @Transactional
    public void deleteChatForUsers(User a, User b, Chat chat) {
        deleteChatForUsersById(a, b, chat.getId());
    }

    @Override
    @Transactional
    public void deleteChatForUsersById(User a, User b, Long id) {
        Chats chats = chatsRepository.findChatsByUsers(a, b);
        chats.getChats().stream().filter(chat -> chat.getId().equals(id)).findFirst().ifPresent(chat -> chats.getChats().remove(chat));
        chatsRepository.save(chats);
    }

    @Override
    @Transactional
    public void editChatForUsers(User a, User b, Chat chat) {
        Chats chats = chatsRepository.findChatsByUsers(a, b);
        if (chats != null) {
            Chat targetChat = chats.getChats().stream()
                    .filter(c -> c.getId().equals(chat.getId()))
                    .findFirst()
                    .orElse(null);

            if (targetChat != null) {
                BeanUtils.copyProperties(chat, targetChat, "id", "users");
                chatsRepository.save(chats);
            }
        }
    }
}
