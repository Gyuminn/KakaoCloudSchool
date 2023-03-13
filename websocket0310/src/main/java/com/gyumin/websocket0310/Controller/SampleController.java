package com.gyumin.websocket0310.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/sample")
public class SampleController {
    @GetMapping("/gyumin")
    public List<String> gyumin() {
        return Arrays.asList("스프링", "도커", "쿠버네티스");
    }
}
