package com.ssrprojects.ultimatechatapp.controller;

import com.ssrprojects.ultimatechatapp.entity.Chat;
import com.ssrprojects.ultimatechatapp.entity.User;
import com.ssrprojects.ultimatechatapp.service.ChatService.ChatService;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    //sample URL: http://localhost:8080/chats/addChat/senderID/receiverID/message
    @GetMapping("/addChat/{senderID}/{receiverID}/{message}")
    public ResponseEntity<String> addChat(@PathVariable String senderID, @PathVariable String receiverID, @PathVariable String message) {
        User sendingUser = userService.getUserByUsername(senderID);
        User receivingUser = userService.getUserByUsername(receiverID);

        Chat chat = new Chat();
        chat.setSenderId(sendingUser.getId());
        chat.setReceiverId(receivingUser.getId());
        chat.setContent(message);

        chatService.sendChatFromUser(sendingUser, receivingUser, chat);

        return ResponseEntity.ok("Chat sent successfully");
    }

    //sample URL: http://localhost:8080/chats/getChats/senderID/receiverID
    @GetMapping("/getChats/{senderID}/{receiverID}")
    public ResponseEntity<List<Chat>> getChats(@PathVariable String senderID, @PathVariable String receiverID) {
        User sendingUser = userService.getUserByUsername(senderID);
        User receivingUser = userService.getUserByUsername(receiverID);

        return ResponseEntity.ok(chatService.getChatsForUsers(sendingUser, receivingUser));
    }
}
