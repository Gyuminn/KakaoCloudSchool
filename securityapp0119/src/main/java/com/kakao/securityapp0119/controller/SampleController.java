package com.kakao.securityapp0119.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class SampleController {
    @GetMapping("/")
    public String index() {
        log.info("메인");
        return "/index";
    }

    @GetMapping("/sample/all")
    public void main() {
        log.info("모두 허용");
    }

    @GetMapping("/sample/member")
    public void member() {
        log.info("멤버만 허용");
    }

    @GetMapping("/sample/admin")
    public void admin() {
        log.info("관리자만 허용");
    }
}
