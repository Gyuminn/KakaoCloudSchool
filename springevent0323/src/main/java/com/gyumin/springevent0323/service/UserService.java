package com.gyumin.springevent0323.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    // 게시자 클래스 주입
    private final UserEventPublisher userEventPublisher;

    public Boolean createUser(String userName, String emailAddress) {
        System.out.println("회원가입");
        log.info("회원가입");
        // 메시지를 게시
        userEventPublisher.publishUserCreated(1234567890L, emailAddress);
        return true;
    }
}
