package com.gyumin.redis0321.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class EventPublisher {
    private final RedisTemplate<String, String> redisTemplate;
    private final ChannelTopic topic;

    public EventPublisher(RedisTemplate<String, String> redisTemplate, ChannelTopic eventTopic) {
        this.redisTemplate = redisTemplate;
        this.topic = eventTopic;
    }

    // 메시지를 전송하는 메서드
    public void sendMessage(EventMessage eventMessage) {
        redisTemplate.convertAndSend(topic.getTopic(), eventMessage);
    }
}
