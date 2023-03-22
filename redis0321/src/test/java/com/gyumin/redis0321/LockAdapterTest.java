package com.gyumin.redis0321;

import com.gyumin.redis0321.service.LockAdpater;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LockAdapterTest {
    @Autowired
    private LockAdpater lockAdpater;

    private final Long firstUserId = 1L;
    private final Long secondUserId = 2L;
    private final Long thirdUserId = 3L;

    @Test
    @DisplayName("분산 락을 테스트합니다.")
    public void testLock() {
        final String hotelId = "마라무쌍";

        // clearLock은 처음 수행할 떄는 주석처리하고
        // 두 번쨰 테스트 할 때는 지우고 확인해야 하므로 주석을 해제
        // lockAdpater.clearLock(hotelId);

        new Thread() {
            public void run() {
                Boolean isSuccess = lockAdpater.holdLock(hotelId, firstUserId);
                // 결과가 맞는지 확인.
                // Assertions.assertTrue(isSuccess);
                System.out.println(isSuccess);
            }
        }.start();

        new Thread() {
            public void run() {
                Boolean isSuccess = lockAdpater.holdLock(hotelId, secondUserId);
                // 결과가 맞는지 확인.
                // Assertions.assertTrue(isSuccess);
                System.out.println(isSuccess);
            }
        }.start();

        new Thread() {
            public void run() {
                Boolean isSuccess = lockAdpater.holdLock(hotelId, thirdUserId);
                // 결과가 맞는지 확인.
                // Assertions.assertTrue(isSuccess);
                System.out.println(isSuccess);
            }
        }.start();

        // true가 하나고 false는 2개면 된다. (RedisController 에서 @Controller 주석처리하고 진행해야 에러가 안남)
    }
}
