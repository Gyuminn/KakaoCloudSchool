package com.gyumin.apiclient0320.config;

import org.apache.hc.client5.http.HttpRoute;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class PoolingRestTemplateConfig {
    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();

        // 최대 스레드 개수 설정
        manager.setMaxTotal(100);
        manager.setDefaultMaxPerRoute(5);

        // 연결점 생성
        HttpHost httpHost = new HttpHost("http://localhost", 9000);
        manager.setMaxPerRoute(new HttpRoute(httpHost), 10);

        return manager;
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(3000, TimeUnit.MILLISECONDS)
                .setConnectTimeout(3000, TimeUnit.MILLISECONDS)
                .build();

    }

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager())
                .setDefaultRequestConfig(requestConfig())
                .build();
    }

    // 위에 까지는 설정이었고 실제 만들어주는 메서드는 여기다.
    // 이제 수명주기 관리도 할 필요가 없어진다.
    @Bean
    public RestTemplate poolingRestTemplate() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient());
        return new RestTemplate();
    }
}
