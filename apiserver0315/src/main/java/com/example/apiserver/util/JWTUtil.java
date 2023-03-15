package com.example.apiserver.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {
    //application.yml 파일의 변수 가져오기

    @Value("${com.kakao.apiserver.secret}")
    private String key;

    //토큰 생성
    public String generateToken(Map<String, Object> valueMap, int days){
        log.info("토큰 생성");

        //헤더 생성
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload 생성
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);
        //유효시간 - 유효 시간을 짧게 설정 - 테스트 할 때 만료 시간이 지났을 때는 확인해보기 위해서
        int time = (10) * days;

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(
                        ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;
    }

    //토큰의 유효성 검사 메서드
    public Map<String, Object> validateToken(String token){
        Map<String, Object> claim = null;

        claim = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claim;
    }
}
