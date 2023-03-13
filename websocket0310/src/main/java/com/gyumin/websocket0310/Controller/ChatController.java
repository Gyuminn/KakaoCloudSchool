package com.gyumin.websocket0310.Controller;

import com.gyumin.websocket0310.service.MailService;
import com.gyumin.websocket0310.service.WebPushService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {
    @GetMapping("/")
    public String chat() {
        return "chatting";
    }

    @GetMapping("textmail")
    public void textmail() {

    }

    private final MailService mailService;

    @PostMapping("textmail")
    public String textmail(HttpServletRequest request) throws Exception {
        mailService.sendMail(request);
        // 리다이렉트
        return "redirect:/";
    }

    private final WebPushService webPushService;


    @GetMapping("push")
    public void push(HttpServletRequest request, HttpServletResponse response) {
        webPushService.push(request, response);
    }
}
