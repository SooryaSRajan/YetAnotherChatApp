package com.ssrprojects.ultimatechatapp.service.ChatService;

import com.ssrprojects.ultimatechatapp.model.Chat;
import com.ssrprojects.ultimatechatapp.model.ChatRelationship;
import com.ssrprojects.ultimatechatapp.model.User;
import com.ssrprojects.ultimatechatapp.model.UserChats;
import com.ssrprojects.ultimatechatapp.model.keys.ChatRelationshipKey;
import com.ssrprojects.ultimatechatapp.repository.ChatRelationshipRepository;
import com.ssrprojects.ultimatechatapp.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRelationshipRepository chatRelationshipRepository;

    public ChatServiceImpl(ChatRepository chatRepository, ChatRelationshipRepository chatPreferencesRepository) {
        this.chatRepository = chatRepository;
        this.chatRelationshipRepository = chatPreferencesRepository;
    }

    private UserChats provisionOrGetUserChatForUsers(User userA, User userB) {

        if(userA.getId().equals(userB.getId())) {
            throw new IllegalArgumentException("User A and User B cannot be the same");
        }

        Optional<UserChats> userChats = chatRepository.findByParticipatingUsers(userA.getId(), userB.getId());

        if (userChats.isPresent()) {
            return userChats.get();
        } else {
            UserChats userChatsNew = new UserChats();
            userChatsNew.addParticipatingUser(userA.getId());
            userChatsNew.addParticipatingUser(userB.getId());

            chatRepository.save(userChatsNew);

            return userChatsNew;
        }

    }

    private void provisionChatRelationship(User userA, User userB, String chatId) {

        if(userA.getId().equals(userB.getId())) {
            throw new IllegalArgumentException("User A and User B cannot be the same");
        }

        ChatRelationship chatRelationship = new ChatRelationship();
        ChatRelationshipKey chatRelationshipKey = new ChatRelationshipKey();
        chatRelationshipKey.setUserAId(userA.getId());
        chatRelationshipKey.setUserBId(userB.getId());
        chatRelationship.setChatRelationshipKey(chatRelationshipKey);
        chatRelationship.setChatId(chatId);

        chatRelationshipRepository.save(chatRelationship);

        chatRelationship = new ChatRelationship();
        chatRelationshipKey = new ChatRelationshipKey();
        chatRelationshipKey.setUserAId(userA.getId());
        chatRelationshipKey.setUserBId(userB.getId());
        chatRelationship.setChatRelationshipKey(chatRelationshipKey);
        chatRelationship.setChatId(chatId);

        chatRelationshipRepository.save(chatRelationship);
    }


    @Override
    @Transactional
    public UserChats provisionOrGetChatsForUsers(User userA, User userB) {

        if(userA.getId().equals(userB.getId())) {
            throw new IllegalArgumentException("User A and User B cannot be the same");
        }

        Optional<ChatRelationship> chatRelationshipAtoB = chatRelationshipRepository
                .findByChatRelationshipKeyUserAIdAndChatRelationshipKeyUserBId(userA.getId(), userB.getId());

        Optional<ChatRelationship> chatRelationshipBtoA = chatRelationshipRepository
                .findByChatRelationshipKeyUserAIdAndChatRelationshipKeyUserBId(userB.getId(), userA.getId());

        //verify if record exists
        if (chatRelationshipAtoB.isPresent() && chatRelationshipBtoA.isPresent()) {
            //verify if their ID is the same
            if (chatRelationshipAtoB.get().getChatId().equals(chatRelationshipBtoA.get().getChatId())) {
                //get chat, IDs are the same
                Optional<UserChats> userChats = chatRepository.findById(chatRelationshipAtoB.get().getChatId());
                if (userChats.isPresent()) {
                    return userChats.get();
                }
            }
        }

        UserChats userChats = provisionOrGetUserChatForUsers(userA, userB);

        chatRelationshipAtoB.ifPresent(chatRelationshipRepository::delete);
        chatRelationshipBtoA.ifPresent(chatRelationshipRepository::delete);

        provisionChatRelationship(userA, userB, userChats.getId());

        return userChats;

    }

    public UserChats getUserChats(User userA, User userB) {

        if(userA.getId().equals(userB.getId())) {
            throw new IllegalArgumentException("User A and User B cannot be the same");
        }

        Optional<ChatRelationship> chatRelationshipAtoB = chatRelationshipRepository
                .findByChatRelationshipKeyUserAIdAndChatRelationshipKeyUserBId(userA.getId(), userB.getId());

        if (chatRelationshipAtoB.isPresent()) {
            Optional<UserChats> userChats = chatRepository.findById(chatRelationshipAtoB.get().getChatId());
            return userChats.orElseGet(() -> provisionOrGetChatsForUsers(userA, userB));
        } else {
            return provisionOrGetChatsForUsers(userA, userB);
        }

    }

    @Override
    public List<Chat> getChatsForUsers(User userA, User userB) {
        UserChats existingUserChats = getUserChats(userA, userB);

        if (existingUserChats != null) {
            return existingUserChats.getChats().stream().sorted(Comparator.comparing(Chat::getCreatedAt)).toList();
        }

        return List.of();
    }

    @Override
    public void addChatForUsers(User userA, User userB, Chat chat) {
        UserChats existingUserChats = getUserChats(userA, userB);

        if (existingUserChats != null) {
            existingUserChats.addToChats(chat);
            chatRepository.save(existingUserChats);
        } else {
            throw new RuntimeException("Unable to add chat for users");
        }
    }

    @Override
    public void sendChatFromUser(User userA, User userB, Chat chat) {
        addChatForUsers(userA, userB, chat);

        //TODO: Add message queue and delivery logic.
    }


    @Override
    public void deleteChatForUsersById(User userA, User userB, String id) {
        UserChats existingUserChats = getUserChats(userA, userB);

        if (existingUserChats != null) {
            existingUserChats.getChats().removeIf(chat -> chat.getId().equals(id));
            chatRepository.save(existingUserChats);
        } else {
            throw new RuntimeException("Unable to delete chat for users");
        }
    }

    @Override
    public void deleteChatForUsers(User userA, User userB, Chat chat) {
        deleteChatForUsersById(userA, userB, chat.getId());
    }


    @Override
    public void editChatForUsers(User userA, User userB, Chat chat) {
        UserChats existingUserChats = getUserChats(userA, userB);

        if (existingUserChats != null) {
            existingUserChats.getChats().removeIf(existingChat -> existingChat.getId().equals(chat.getId()));
            existingUserChats.getChats().add(chat);
            existingUserChats.getChats().sort(Comparator.comparing(Chat::getCreatedAt));
            chatRepository.save(existingUserChats);
        } else {
            throw new RuntimeException("Unable to edit chat for users");
        }
    }
}
