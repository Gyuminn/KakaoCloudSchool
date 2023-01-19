package com.kakao.securityapp0119;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordTests {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void testEncode() {
        String password = "1111";
        String enPw = passwordEncoder.encode(password);
        System.out.println("enPw: " + enPw);
        enPw = passwordEncoder.encode(password);
        System.out.println("enPw: " + enPw);
        // 해싱 기법을 달리해서 enPw가 다르게 나온다.
        // 그렇지만 matches로 비교하면 같은지 다른지 알 수 있다.

        // 비교하는 테스트를 원할 때 matches를 이용하면 된다.
        boolean result = passwordEncoder.matches(password, enPw);
        System.out.println("result:" + result);

    }
}
