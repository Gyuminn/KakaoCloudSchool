package com.actuator.actuator0317.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exception")

public class ExceptionController {

    @GetMapping
    public ResponseEntity<String> getRuntimeException(@RequestParam("su") int su) {
        if (su == 1) {
            // 강제로 예외 발생
            throw new RuntimeException("예외 발생");
        }
        return new ResponseEntity<>("요청 처리 성공", HttpStatus.OK);
    }

    // Controller 안에서 RuntimeException이 발생하면 호출될 메서드
    @ExceptionHandler(value= RuntimeException.class)
    public ResponseEntity<String> handleException(RuntimeException e, HttpServletRequest request) {
        return new ResponseEntity<>("요청 처리 실패", HttpStatus.BAD_REQUEST);
    }
}
