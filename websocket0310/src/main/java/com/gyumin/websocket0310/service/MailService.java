package com.gyumin.websocket0310.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    // 주입 받아서 쓰자는 마인드
    private final JavaMailSender javaMailSender;

    // 메일을 보내기 위한 메서드
    // 항상 DTO를 썻는데 기본은 서블릿이다.
    public void sendMail(HttpServletRequest request) throws Exception{
        // 파라미터 인코딩 설정
        // setCharacterEncoding은 예외처리를 강제하는데
        // 우리가 만든 경우에는 try catch 구문을 쓰는 것이 좋고
        // 그게 아니라면 throws Exception으로 Spring이 처리하도록 넘기자
        request.setCharacterEncoding("utf-8");

        // 보내는 사람 설정
        String setfrom = "rhkdtlrtm12@gmail.com";

        // 파라미터 읽기
        String tomail = request.getParameter("tomail");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(setfrom);
            message.setTo(tomail);
            message.setSubject(title);
            message.setText(content);

            // 메일 전송
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
