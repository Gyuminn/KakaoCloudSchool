package com.gyumin.apiclient0320.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class IdentityHeaderInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution excution)
        throws IOException {
        // 헤더가 존재하면 그대로 두고 없으면 추가
        request.getHeaders().addIfAbsent("X-COMPONENT-ID", "GYUMIN-API");
        return excution.execute(request, body);
    }
}
