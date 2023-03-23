package com.gyumin.springevent0323;

import com.gyumin.springevent0323.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Springevent0323Application {

    public static void main(String[] args) {
        // Application context 찾아오기
        SpringApplicationBuilder appBuilder = new SpringApplicationBuilder(Springevent0323Application.class);
        SpringApplication application = appBuilder.build();
        ConfigurableApplicationContext context = application.run(args);

        // Bean 찾아오기
        // 서비스 메서드 호출
        UserService userService = context.getBean(UserService.class);
        userService.createUser("kimgyumin", "gyumin@kakao.com");
    }

}
