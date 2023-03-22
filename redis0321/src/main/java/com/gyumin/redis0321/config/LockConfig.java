package com.gyumin.redis0321.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
public class LockConfig {
    @Bean
    public RedisConnectionFactory lockedRedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
        return new LettuceConnectionFactory(configuration);
    }

    // Bean의 이름을 설정
    @Bean(name="lockRedisTempate")
    public RedisTemplate<String, Long> lockedRedisTemplate() {
        RedisTemplate<String, Long> lockedRedisTemplate = new RedisTemplate<>();
        lockedRedisTemplate.setConnectionFactory(lockedRedisConnectionFactory());
        lockedRedisTemplate.setKeySerializer(new GenericToStringSerializer<>(Long.class));
        lockedRedisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        return lockedRedisTemplate;
    }
}
