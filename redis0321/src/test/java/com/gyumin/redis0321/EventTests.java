package com.gyumin.redis0321;

import com.gyumin.redis0321.service.EventMessage;
import com.gyumin.redis0321.service.EventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class EventTests {
    @Autowired
    private EventPublisher eventPublisher;

    @Test
    public void testPubSub() throws InterruptedException {
        eventPublisher.sendMessage(new EventMessage("테스트"));
        TimeUnit.SECONDS.sleep(5);
    }
}
