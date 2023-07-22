package com.ssrprojects.ultimatechatapp.controller;

import com.ssrprojects.ultimatechatapp.model.Chat;
import com.ssrprojects.ultimatechatapp.model.User;
import com.ssrprojects.ultimatechatapp.service.ChatService.ChatService;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        chat.setMessage(message);

        chatService.sendChatFromUser(sendingUser, receivingUser, chat);

        return ResponseEntity.ok("User added successfully");
    }

    @GetMapping("/getChats/{senderID}/{receiverID}")
    public ResponseEntity<String> getChats(@PathVariable String senderID, @PathVariable String receiverID) {

        User sendingUser = userService.getUserByUsername(senderID);
        User receivingUser = userService.getUserByUsername(receiverID);

        chatService.getChatsForUsers(sendingUser, receivingUser);

        return ResponseEntity.ok("User added successfully");
    }
}
