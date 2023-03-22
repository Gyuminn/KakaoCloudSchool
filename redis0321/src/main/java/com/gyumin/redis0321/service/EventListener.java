package com.gyumin.redis0321.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

// 구독자 클래스
public class EventListener implements MessageListener {
    private RedisTemplate<String, String> redisTemplate;
    private RedisSerializer<EventMessage> valueSerializer;

    public EventListener(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueSerializer = (RedisSerializer<EventMessage>) redisTemplate.getValueSerializer();
    }

    // 게시자가 게시했을 때 호출될 메서드
    @Override
    public void onMessage(Message message, byte[] pattern) {
        EventMessage eventMessage = valueSerializer.deserialize(message.getBody());
        System.out.println("시간:" + eventMessage.getTimestamp());
        System.out.println(eventMessage.toString());
    }
}
