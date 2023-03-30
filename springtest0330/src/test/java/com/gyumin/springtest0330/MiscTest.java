package com.gyumin.springtest0330;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

public class MiscTest {
    // HashSet은 중복된 데이터를 저장하지 않음.
    // 중복된 데이터를 삽입하더라도 HashSet의 size는 변경이 되면 안됨. - 시나리오
    @Test
    public void testHashSet() {
        // Given - 환경설정
        HashSet<Integer> set = new HashSet<>();
        int x = 3;
        int y = 3;

        // When - 테스트 하기 위한 작업 수행
        set.add(x);
        set.add(y);

        // Then
        // 에상값과 실제 숳애한 값이 맞는지 확인
        Assertions.assertEquals(1, set.size());
    }
}
