package com.gyumin.apiclient0320.config;

import com.gyumin.apiclient0320.interceptor.IdentityHeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

// PoolingRestTemplateConfig에서도 RestTemplate을 만들어줬으므로 2개가 된다. 그래서 Configuration을 주석처리
// @Configuration
public class RestTemplateConfig {
    // 팩토리 클래스의 bean 설정
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // 대기 시간과 읽는 시간 최대값 설정
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(2000);
        factory.setBufferRequestBody(false);

        return factory;
    }

    // RestTemplate 빈 설정
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

        restTemplate.getInterceptors().add(new IdentityHeaderInterceptor());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return restTemplate;
    }
}
