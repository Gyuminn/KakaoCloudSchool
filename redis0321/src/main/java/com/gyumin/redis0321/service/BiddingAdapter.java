package com.gyumin.redis0321.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BiddingAdapter {
    private final RedisTemplate<String, Long> redisTemplate;

    public Boolean createBidding(Long hotelId, Long userId, Double amount) {
        String key = hotelId + "";
        return redisTemplate.opsForZSet().add(key, userId, amount);
    }

    public List<Long> getTopBidders(Long hotelId, Integer fetchCount) {
        String key = hotelId + "";
        return redisTemplate
                .opsForZSet()
                .reverseRangeByScore(key, 0D, Double.MAX_VALUE, 0, fetchCount)
                .stream()
                .collect(Collectors.toList());
    }

    public Double getBidAmount(Long hotelId, Long userId) {
        String key = hotelId + "";
        return redisTemplate.opsForZSet().score(key, userId);
    }

    public void clear(Long hotelId) {
        String key = hotelId + "";
        redisTemplate.delete(key);
    }
}
