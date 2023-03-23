package com.gyumin.springevent0323.service;

import com.gyumin.springevent0323.dto.UserEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // 게시하는 메서드
    public void publishUserCreated(Long userId, String emailAddress) {
        // 메시지 생성
        UserEvent userEvent = UserEvent.created(this, userId, emailAddress);
        System.out.println("게시");

        // 이벤트 게시
        applicationEventPublisher.publishEvent(userEvent);
    }
}
