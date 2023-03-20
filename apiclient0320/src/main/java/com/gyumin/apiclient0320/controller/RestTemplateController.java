package com.gyumin.apiclient0320.controller;

import com.gyumin.apiclient0320.dto.MemberDTO;
import com.gyumin.apiclient0320.service.RestTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest-template")
public class RestTemplateController {
    private final RestTemplateService restTemplateService;

    // @Autowired - setter를 이용한 주입
    // @Autowired는 아래 메서드를 자동 생성
    /*
    속성을 생성하기 위한 생성자 - 최근에는 RequiredArgsContructor로 대체
    public RestTemplateController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }
     */

    @GetMapping
    public String getName() {
        System.out.println("아무거나");
        return restTemplateService.getName();
    }

    @GetMapping("/path-variable")
    public String getNameWithPathVariable() {
        return restTemplateService.getNameWithPathVariable();
    }

    @GetMapping("/parameter")
    public String getNameWithParameter() {
        return restTemplateService.getNameWithParameter();
    }

    @PostMapping
    public ResponseEntity<MemberDTO> postDTO() {
        return restTemplateService.postWithParamAndBody();
    }

    @PostMapping("/header")
    public ResponseEntity<MemberDTO> postWithHeader() {
        return restTemplateService.postWithHeader();
    }
}
