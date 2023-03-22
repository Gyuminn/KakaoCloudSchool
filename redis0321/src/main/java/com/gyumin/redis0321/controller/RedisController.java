package com.gyumin.redis0321.controller;

import com.gyumin.redis0321.service.MemberRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class RedisController {
    private final MemberRedisService memberRedisService;

    @PostMapping("/addmember")
    public ResponseEntity<String> add() {
        memberRedisService.addMember();
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    // Redis를 사용할 수 있는 bean 주입
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/redistest")
    public ResponseEntity<?> addRedisString() {
        // 문자열을 사용할 수 있는 객체 생성
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        // 반 영구적 저장
        vop.set("yellow", "banana");
        // 30초 동안만 유효한 저장
        vop.set("blue", "블루베리", Duration.ofSeconds(10));

        // id와 ip와 운영체제 종류
        // id:ip:os 이렇게 하고 split 하면 된다.
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/redistest/{key}")
    public ResponseEntity<?> getRedisKey(@PathVariable String key) {
        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        String value = vop.get(key);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }
}
