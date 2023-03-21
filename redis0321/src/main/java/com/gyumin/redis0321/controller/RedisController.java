package com.gyumin.redis0321.controller;

import com.gyumin.redis0321.service.MemberRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {
    private final MemberRedisService memberRedisService;

    @PostMapping("/addmember")
    public ResponseEntity<String> add() {
        memberRedisService.addMember();
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
