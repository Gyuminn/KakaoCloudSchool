package com.gyumin.springtest0330;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@TestConfiguration
public class TestThreadPoolConfig {
    // 빈이 파괴될 때 shutdown 메서드를 호출하겠다.
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 스레드 풀에 만들어지는 스레드의 최대 개수
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setThreadNamePrefix("TestExecutor-");
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}
