package com.gyumin.springevent0323.service;

import com.gyumin.springevent0323.dto.UserEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UserEventListener implements ApplicationListener<UserEvent> {

    // 이벤트가 게시되었을 때 호출될 메서드
    @Override
    public void onApplicationEvent(UserEvent event) {
        if (event.getType() == UserEvent.Type.CREATE) {
            System.out.println("생성 이벤트를 구독함");
            log.info("생성 이벤트를 구독함");
            // 수행할 작업
        }
    }
}
