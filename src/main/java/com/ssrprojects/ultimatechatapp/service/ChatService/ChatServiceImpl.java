package com.ssrprojects.ultimatechatapp.service.ChatService;

import com.ssrprojects.ultimatechatapp.model.Chat;
import com.ssrprojects.ultimatechatapp.model.User;
import com.ssrprojects.ultimatechatapp.model.UserChats;
import com.ssrprojects.ultimatechatapp.repository.ChatRepository;
import com.ssrprojects.ultimatechatapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatServiceImpl(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserChats provisionChatsForUsers(User userA, User userB) {
        UserChats existingUserChats = chatRepository.findByParticipatingUsers(userA.getId(), userB.getId());

        if (existingUserChats != null) {
            return existingUserChats;
        }

        //UserChats builder
        UserChats userChats = UserChats.builder()
                .participatingUsers(List.of(userA.getId(), userB.getId()))
                .build();

        //add chat ID to List of chats
        List<String> chatIds = userA.getUserChats();

        if (chatIds == null) {
            chatIds = new ArrayList<>();
        }

        chatIds.add(userChats.getId());
        userA.setUserChats(chatIds);

        chatIds = userB.getUserChats();

        if (chatIds == null) {
            chatIds = new ArrayList<>();
        }

        chatIds.add(userChats.getId());
        userB.setUserChats(chatIds);

        userRepository.save(userA);
        userRepository.save(userB);

        return chatRepository.save(userChats);
    }

    @Override
    public List<Chat> getChatsForUsers(User userA, User userB) {
        UserChats existingUserChats = provisionChatsForUsers(userA, userB);

        if (existingUserChats != null) {
            return existingUserChats.getChats().stream().sorted(Comparator.comparing(Chat::getCreatedAt)).toList();
        }

        return List.of();
    }

    @Override
    public void addChatForUsers(User userA, User userB, Chat chat) {
        UserChats existingUserChats = provisionChatsForUsers(userA, userB);

        if (existingUserChats != null) {
            existingUserChats.getChats().add(chat);
            chatRepository.save(existingUserChats);
        }

        throw new RuntimeException("Unable to add chat for users");
    }

    @Override
    public void sendChatFromUser(User userA, User userB, Chat chat) {
        addChatForUsers(userA, userB, chat);

        //TODO: Add message queue and delivery logic.
    }


    @Override
    public void deleteChatForUsersById(User userA, User userB, String id) {
        UserChats existingUserChats = provisionChatsForUsers(userA, userB);

        if (existingUserChats != null) {
            existingUserChats.getChats().removeIf(chat -> chat.getId().equals(id));
            chatRepository.save(existingUserChats);
        }

        throw new RuntimeException("Unable to delete chat for users");
    }

    @Override
    public void deleteChatForUsers(User userA, User userB, Chat chat) {
        deleteChatForUsersById(userA, userB, chat.getId());
    }


    @Override
    public void editChatForUsers(User userA, User userB, Chat chat) {

        UserChats existingUserChats = provisionChatsForUsers(userA, userB);

        if (existingUserChats != null) {
            existingUserChats.getChats().removeIf(existingChat -> existingChat.getId().equals(chat.getId()));
            existingUserChats.getChats().add(chat);
            existingUserChats.getChats().sort(Comparator.comparing(Chat::getCreatedAt));
            chatRepository.save(existingUserChats);
        }

        throw new RuntimeException("Unable to edit chat for users");

    }
}
