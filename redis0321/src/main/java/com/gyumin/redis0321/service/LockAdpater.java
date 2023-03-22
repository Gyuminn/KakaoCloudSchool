package com.gyumin.redis0321.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

// Component, Configuration, Controller(RestController), Service, Repository
// Bean 아래 있는 메서드가 리턴하는 데이터
// Bean 자동 객체 - 클래스의 객체를 생성해서 Spring Bean으로 등록
// Configuration은 여러 Bean을 생성하고자 하는 경우 자동 생성을 위해서 사용
// Component는 명확하게 목적을 정하기 어려운 경우 사용
@Component
@Log4j2
public class LockAdpater {
    private final RedisTemplate<String, Long> lockRedisTemplate;
    private ValueOperations<String, Long> lockOperation;

    public LockAdpater(RedisTemplate<String, Long> lockRedisTemplate) {
        this.lockRedisTemplate = lockRedisTemplate;
        lockOperation = lockRedisTemplate.opsForValue();
    }

    // 락 획득 메서드
    public Boolean holdLock(String hotelId, Long userId) {
        String lockKey = "key";
        // 매칭되는 데이터가 없을 때 저장
        return lockOperation.setIfAbsent(lockKey, userId, Duration.ofSeconds(10));
    }

    // 락 확인하는 메서드
    public Long checkLock(String hotelId) {
        String lockKey = "key";
        return lockOperation.get(lockKey);
    }

    // 락을 헤제하는 메서드
    public void clearLock(String hotelId) {
        lockRedisTemplate.delete("key");
    }
}
