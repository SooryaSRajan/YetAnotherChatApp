package com.ssrprojects.ultimatechatapp.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {


    @MessageMapping("/sendMessage")
    @SendTo("/user/queue/receiveMessage")
    public String receiveMessage(@Payload String message) {
        return "Server received: " + message;
    }

}
