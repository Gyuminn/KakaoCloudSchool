package com.example.apiserver;

import com.example.apiserver.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTTests {
    @Autowired
    private JWTUtil jwtUtil;

    @Test
    //생성 확인
    public void testGenerate(){
        Map<String, Object> claimMap =
                Map.of("mid", "ABCDE");
        String jwtStr = jwtUtil.generateToken(claimMap, 1);
        System.out.println(jwtStr);
    }

    //유효한 토큰인지 확인
    @Test
    public void testValidate(){
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Nzg3NjAxMzQsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNjc4NzYwMDc0fQ.PQfFFZqzdXIQ7Ytpk0cJmj4Mv5_ipv5sXNoe5squ6fq";
        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);
        System.out.println(claim);
    }
}
