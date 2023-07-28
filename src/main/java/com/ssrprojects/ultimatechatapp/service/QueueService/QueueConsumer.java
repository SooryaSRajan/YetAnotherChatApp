package com.ssrprojects.ultimatechatapp.service.QueueService;

import com.ssrprojects.ultimatechatapp.service.QueueService.queue.QueueTask;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import com.ssrprojects.ultimatechatapp.utils.Constants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumer {

    final UserService userService;

    public QueueConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = Constants.QUEUE_NAME)
    public void consumeMessageFromQueue(QueueTask queueTask) {

    }
}
