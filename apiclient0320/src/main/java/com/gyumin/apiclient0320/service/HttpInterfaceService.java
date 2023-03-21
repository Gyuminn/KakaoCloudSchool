package com.gyumin.apiclient0320.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HttpInterfaceService {
    private final HttpInterfaceAPIService httpInterfaceAPIService;

    public String getName() {
        return httpInterfaceAPIService.getName();
    }
}
