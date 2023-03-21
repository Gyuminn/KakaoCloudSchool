package com.gyumin.redis0321.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

// RedisRepository 사용 가능하도록 해주는 어노테이션
@EnableRedisRepositories
@Configuration
public class RedisConfig {
    // 레디스 설정 팩토리 클래스
    @Bean
    public RedisConnectionFactory basicCacheRedisConnectionFactory() {
        /// 센티널 방식 - 관리자가 따로 존재하는 방식
        /*
        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        configuration.setMaster("마스터");
        configuration.addSentinel("레플리카");
         */

        // 클러스터 방식 - 모든 노드가 Mesh로 연결
        /*
        RedisClusterConfiguration configuration = new RedisClusterConfiguration();
        configuration.setMaxRedirects(3); // 최대 연결 개수
        configuration.setClusterNodes(List.of(
                new RedisNode("IP", 1),
                new RedisNode("IP", 2),
                new RedisNode("IP", 3)
        ));
         */

        // 레디스 구성에 따라 달라짐 - 단독 서버
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);

        // 옵션 설정
        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(10)).build();
        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

        // 연결 팩토리
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofSeconds(5))
                .shutdownTimeout(Duration.ZERO)
                .build();
        return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);
    }

    // 실제 사용될 Template을 생성
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(basicCacheRedisConnectionFactory());
        // redis는 데이터를 보관할 때 byte 배열로 보관
        // 데이터를 읽고 쓸 때 byte가 아닌 실제 자료형을 설정해주어야 데이터를 올바르게 읽고 쓸 수 있다.
        // Serializer(직렬화) - 객체 단위로 읽고 쓸 수 있도록 해주는 기능
        // DTO를 만들 떄 implements Serializable을 해주는 경우가 있는데
        // DTO 객체 자체를 파일에 읽고 쓰기를 하거나 다른 시스템에 전송할 떄
        // Java <-> Java JSON
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
