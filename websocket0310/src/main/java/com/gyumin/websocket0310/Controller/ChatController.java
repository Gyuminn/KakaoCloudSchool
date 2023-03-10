package com.gyumin.websocket0310.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {
    @GetMapping("/")
    public String chat() {
        return "chatting";
    }
}
