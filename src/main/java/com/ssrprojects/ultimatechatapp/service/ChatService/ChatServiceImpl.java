package com.ssrprojects.ultimatechatapp.service.ChatService;

import com.ssrprojects.ultimatechatapp.model.Chat;
import com.ssrprojects.ultimatechatapp.model.Chats;
import com.ssrprojects.ultimatechatapp.model.User;
import com.ssrprojects.ultimatechatapp.repository.ChatRepository;
import com.ssrprojects.ultimatechatapp.repository.ChatsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    public Chats provisionChatsForUsers(User a, User b) {
        Chats chats = new Chats();
        chats.setUsers(List.of(a, b));
        chatsRepository.save(chats);
        return chats;
    }

    @Override
    public void addChat(Chat chat) {

        Chats chats = chat.getChats();
        if (chats == null) {
            throw new RuntimeException("Chats not provisioned for users in chat");
        }

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

        if (chats == null) {
            chats = provisionChatsForUsers(a, b);
        }

        chats.getChats().add(chat);
        chatsRepository.save(chats);
    }

    @Override
    public void sendChatFromUser(User sender, User receiver, Chat chat) {
        Chats chats = chatsRepository.findChatsByUsers(sender, receiver);

        if (chats == null) {
            chats = provisionChatsForUsers(sender, receiver);
        }

        Chat newChat = new Chat();
        BeanUtils.copyProperties(chat, newChat);
        newChat.setSenderId(sender.getId());
        newChat.setReceiverId(receiver.getId());
        newChat.setSentAt(new Date());
        chats.getChats().add(newChat);
        chatsRepository.save(chats);

    }

    @Override
    @Transactional
    public void deleteChatForUsers(User a, User b, Chat chat) {
        deleteChatForUsersById(a, b, chat.getId());
    }

    @Override
    @Transactional
    public void deleteChatForUsersById(User sender, User receiver, Long id) {
        Chats chats = chatsRepository.findChatsByUsers(sender, receiver);

        if (chats == null) {
            throw new RuntimeException("Chats not provisioned for users in chat");
        }

        chats.getChats().stream().filter(chat -> chat.getId().equals(id)).findFirst().ifPresent(chat -> {
            if (!chat.getSenderId().equals(sender.getId())) {
                throw new RuntimeException("Sender ID does not match");
            }
        });
        chatsRepository.save(chats);
    }

    @Override
    @Transactional
    public void editChatForUsers(User sender, User receiver, Chat chat) {
        Chats chats = chatsRepository.findChatsByUsers(sender, receiver);

        if (chats == null) {
            chats = provisionChatsForUsers(sender, receiver);
        }

        if (chats != null) {
            Chat targetChat = chats.getChats().stream()
                    .filter(c -> c.getId().equals(chat.getId()))
                    .findFirst()
                    .orElse(null);

            if (targetChat != null) {

                if (!targetChat.getSenderId().equals(sender.getId()) || !targetChat.getReceiverId().equals(receiver.getId())) {
                    throw new RuntimeException("Sender and receiver do not match");
                }

                BeanUtils.copyProperties(chat, targetChat, "id", "users");
                targetChat.setIsEdited(true);
                chatRepository.save(targetChat);
            }
        }
    }
}
