package com.example.apiserver.security.handler;

import com.example.apiserver.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.awt.*;
import java.io.IOException;
import java.util.Map;

//로그인 성공 후 호출되는 핸들러
@Log4j2
//final로 설정된 데이터를 생성자에서 대입받아서 주입
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
    //생성자를 이용한 주입
    //이 클래스의 객체를 생성할 때 스프링의 자동 생성을 이용하면 아무런 문제가 없지만
    //직접 객체를 생성하는 생성자를 호출하는 경우에는 이 클래스의 객체를 주입받아야 합니다.
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
    throws IOException {
        log.info("로그인 성공");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("로그인 한 유저 이름:" + authentication.getName());
        //토큰 생성 정보 생성
        Map<String, Object> claim = Map.of("mid", authentication.getName());
        //토큰 생성
        String accessToken = jwtUtil.generateToken(claim, 10); //액세스 토큰
        String refreshToken = jwtUtil.generateToken(claim, 100); //리프레시 토큰
        //생성한 토큰을 하나의 문자열로 변경
        Gson gson = new Gson();
        Map<String, String> keyMap = Map.of("accessToken", accessToken,
                "refreshToken", refreshToken);
        String jsonStr = gson.toJson(keyMap);
        //클라이언트에게 전송
        response.getWriter().println(jsonStr);
    }
}
