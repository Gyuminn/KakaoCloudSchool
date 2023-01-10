package com.kakao.springbootjpa0110;

import com.kakao.springbootjpa0110.domain.Memo;
import com.kakao.springbootjpa0110.persistence.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class RepositoryTest {
    @Autowired
    MemoRepository memoRepository;

    // 삽입 테스트
    @Test
    public void testInsert() {
        // 데이터 100개 삽입
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder()
                    .memoText("Sample..." + i)
                    .build();
            memoRepository.save(memo);
        });
    }
}
