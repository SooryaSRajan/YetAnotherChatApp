package com.ssrprojects.ultimatechatapp.service.QueueService;

import com.ssrprojects.ultimatechatapp.service.QueueService.queue.QueueTask;
import com.ssrprojects.ultimatechatapp.service.QueueService.queue.Task;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import com.ssrprojects.ultimatechatapp.utils.Constants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class QueueConsumer {

    final UserService userService;

    public QueueConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = Constants.QUEUE_NAME)
    public void consumeMessageFromQueue(QueueTask queueTask) {
        System.out.println("Message Received from queue: " + queueTask);

        if (queueTask.getTask() == Task.REMOVE_UNVERIFIED_USER) {
            System.out.println("Removing unverified user");
            HashMap<String, String> properties = queueTask.getProperties();
            System.out.println("User id: " + properties.get("userId"));

            userService.removeUnverifiedUser(properties.get("userId"));
        } else {
            System.out.println("Unknown task");
        }

    }
}
