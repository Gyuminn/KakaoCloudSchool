package com.gyumin.redis0321;

import com.gyumin.redis0321.service.BiddingAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BiddingAdpaterTests {
    private final Long firstUserId = 1L;
    private final Long secondUserId = 2L;
    private final Long thirdUserId = 3L;
    private final Long fourthUserId = 4L;
    private final Long fifthUserId = 5L;

    @Autowired
    private BiddingAdapter biddingAdapter;

    @Test
    public void simulate() {
        // biddingAdapter.clear(1000L);

        biddingAdapter.createBidding(1000L, firstUserId, 100d);
        biddingAdapter.createBidding(1000L, secondUserId, 110d);
        biddingAdapter.createBidding(1000L, thirdUserId, 120d);
        biddingAdapter.createBidding(1000L, fourthUserId, 130d);
        biddingAdapter.createBidding(1000L, fifthUserId, 140d);

        biddingAdapter.createBidding(1000L, firstUserId, 150d);
        biddingAdapter.createBidding(1000L, thirdUserId, 190d);

        List<Long> topBidders = biddingAdapter.getTopBidders(1000L, 3);

        // 보통은 이렇게 결과를 확인하지만 실제 테스트 기법에서는 결과가 예상한 것과 일치하는지 확인한다.
        System.out.println(topBidders);

        Assertions.assertEquals(thirdUserId, topBidders.get(0));
        Assertions.assertEquals(firstUserId, topBidders.get(1));
        Assertions.assertEquals(fifthUserId, topBidders.get(2));
    }
}
