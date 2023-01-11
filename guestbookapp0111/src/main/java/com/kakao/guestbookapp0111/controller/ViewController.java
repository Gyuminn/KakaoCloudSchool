package com.kakao.guestbookapp0111.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
public class ViewController {
    @GetMapping({"/", "/guestbook/list"})
    public String list() {
        log.info("메인 화면....");
        return "/guestbook/list";
    }
}
