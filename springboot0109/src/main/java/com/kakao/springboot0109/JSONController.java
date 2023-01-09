package com.kakao.springboot0109;

import org.springframework.web.bind.annotation.*;

@RestController
// 공통된 URL
@RequestMapping("/api/v1/rest-api")
public class JSONController {
    @RequestMapping(value="/hello", method= RequestMethod.GET)
    public String getHello() {
        return "GET Hello";
    }

    @GetMapping("/newhello")
    public String getNewHello() {
        return "Get New Hello";
    }

    @GetMapping("/product/{num}")
    public String getNum(@PathVariable("num") int num) {
        return num + "";
    }
}
