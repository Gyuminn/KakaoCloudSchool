package com.kakao.springview0109.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController {
    private final Logger LOGGER = LoggerFactory.getLogger(PageController.class);

    @GetMapping("/")
    public String main(Model model){
        Map<String, Object> map = new HashMap<>();
        map.put("title", "개발자 이야기");
        map.put("content", "개발은 창조적인 것이다");
        map.put("author", "gyumin");

        model.addAttribute("map", map);

        List<String> list = new ArrayList<>();
        list.add("Developer");
        list.add("Developer");
        list.add("MLOps");
        list.add("DevOps");
        list.add("DBA");

        model.addAttribute("list", list);

        return "main";
    }

    @GetMapping("/article")
    // 리턴 타입이 void이면 출력하는 뷰 이름은 요청 URL
    // view의 이름은 article
    public void article(Model model) {
        LOGGER.info("article 요청이 들어왔습니다.");
    }
}
