package com.gyumin.apiclient0320.config;

import com.gyumin.apiclient0320.service.HttpInterfaceAPIService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpConfiguration {
    // 실제 다운로드를 위한 구현체 - WebClient가 많이 쓰임. 동기식 처리도 가능하기 때문이다.
    @Bean
    public WebClient client() {
        return WebClient.builder().baseUrl("http://localhost:9000").build();
    }

    // 빌더 객체 생성 - 객체를 생성하기 위한 객체(Factory, Builder)
    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient client) {
        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
    }

    @Bean
    // HttpInterface 객체 생성 - 실제 사용될 서비스 객체
    public HttpInterfaceAPIService httpInterfaceAPIService(HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(HttpInterfaceAPIService.class);
    }
}
