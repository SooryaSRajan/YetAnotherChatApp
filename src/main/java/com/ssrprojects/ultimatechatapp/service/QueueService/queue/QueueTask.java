package com.ssrprojects.ultimatechatapp.service.QueueService.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QueueTask {
    Task task;
    HashMap<String, String> properties;
}
