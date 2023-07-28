package com.ssrprojects.ultimatechatapp.service.QueueService;

import com.ssrprojects.ultimatechatapp.service.QueueService.queue.QueueTask;
import com.ssrprojects.ultimatechatapp.utils.Constants;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public record QueueService(RabbitTemplate rabbitTemplate) {
    public void sendMessage(QueueTask message) throws AmqpException {
        rabbitTemplate.convertAndSend(Constants.EXCHANGE_NAME, Constants.ROUTING_KEY, message);
    }
}
