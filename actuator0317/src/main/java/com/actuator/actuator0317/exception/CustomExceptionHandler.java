package com.actuator.actuator0317.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

// RestController에서 예외가 발생했을 때 데이터를 넘겨주기 위한 예외 클래스
@RestControllerAdvice
@Log4j2
public class CustomExceptionHandler {
    // 이 예외가 발생한 경우 처리
    @ExceptionHandler(value=RuntimeException.class)
    // DTO가 없으니 Map 으로 처리
    public ResponseEntity<Map<String, String>> handleException(RuntimeException e, HttpServletRequest request) {
        // 어떤 요청이 왔을 때 어떤 예외가 발생하는지 로깅
        log.error("예외 발생:{}{}", request.getRequestURI(), e.getMessage());

        // 헤더 생성
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("name", "gyumin");

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        // 전송할 데이터 생성
        Map<String, String> map = new HashMap<>();
        map.put("error type", HttpStatus.ACCEPTED.getReasonPhrase());
        map.put("code", "400");
        map.put("message", e.getMessage());

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}
