package com.gyumin.apiclient0320.service;

import org.springframework.web.service.annotation.GetExchange;

public interface HttpInterfaceAPIService {
    @GetExchange("/api/v1/crud-api")
    String getName();
}
