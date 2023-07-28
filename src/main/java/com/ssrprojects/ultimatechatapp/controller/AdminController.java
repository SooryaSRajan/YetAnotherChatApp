package com.ssrprojects.ultimatechatapp.controller;

import com.ssrprojects.ultimatechatapp.service.QueueService.QueueService;
import com.ssrprojects.ultimatechatapp.service.QueueService.queue.QueueTask;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    final QueueService queueService;

    public AdminController(QueueService queueService) {
        this.queueService = queueService;
    }

    //sample URL: http://localhost:8080/api/admin/addQueueTask
    @PostMapping("/addQueueTask")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody QueueTask queueTask) {
        queueService.sendMessage(queueTask);
        return ResponseEntity.ok("Message sent to queue");
    }
}
