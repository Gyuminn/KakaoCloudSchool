package com.kakao.reviewapp0116.config;

import com.kakao.reviewapp0116.aop.MeasuringInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 웹 설정 클래스
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 인터셉터 설정 메서드
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MeasuringInterceptor())
                .addPathPatterns("/*"); // 인터셉터가 적용될 URL, 해당 경로에 접근하기 전에 인터셉터가 가로챈다.
    }
}
