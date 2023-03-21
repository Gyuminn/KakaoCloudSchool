package com.gyumin.apiclient0320.controller;

import com.gyumin.apiclient0320.service.HttpInterfaceAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/httpinterface")
public class HttpInterfaceController {
    private final HttpInterfaceAPIService httpInterfaceAPIService;

    @GetMapping
    public String getName() {
        return httpInterfaceAPIService.getName();
    }
}
