package com.example.apiserver.config;

import com.example.apiserver.security.APIUserDetailsService;
import com.example.apiserver.security.filter.APILoginFilter;
import com.example.apiserver.security.filter.RefreshTokenFilter;
import com.example.apiserver.security.filter.TokenCheckFilter;
import com.example.apiserver.security.handler.APILoginSuccessHandler;
import com.example.apiserver.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class CustomSecurityConfig {
    //CORS 설정을 위한 Bean을 생성
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        //모든 요청에 설정
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        //메서드 설정
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET",
                "POST", "PUT", "DELETE"));
        //헤더 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization",
                "Cache-Control", "Content-Type"));
        //인증 설정
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private final JWTUtil jwtUtil;
    //로그인 처리를 위한 객체를 추가
    private final APIUserDetailsService apiUserDetailsService;

    //비밀번호 암호화를 위해서 필요
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //TokenCheckFilter 클래스의 객체를 생성해주는 메서드
    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil){
        return new TokenCheckFilter(jwtUtil);
    }

    @Bean
    //Security가 동작할 때 같이 동작할 필터를 등록하는 메서드
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("-----------------configure--------------");

        //인증 관리자 설정
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(apiUserDetailsService)
                .passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        //필터 등록 - 토큰을 생성할 때 동작
        APILoginFilter apiLoginFilter =
                new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);
        //로그인 필터 적용
        http.addFilterBefore(apiLoginFilter,
                UsernamePasswordAuthenticationFilter.class);
        //Access 토큰 검증 필터 적용
        http.addFilterBefore(tokenCheckFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class);
        //Refresh 토큰 검증 필터 적용
        http.addFilterBefore(
                new RefreshTokenFilter("/refreshToken", jwtUtil),
                TokenCheckFilter.class);

        //APILoginFilter 다음에 동작할 핸들러 설정
        //로그인 성공 과 실패에 따른 핸들러를 설정할 수 있음
        APILoginSuccessHandler successHandler =
                new APILoginSuccessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        //csrf 기능 중지
        http.csrf().disable();
        //세션 사용 중지
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(
                    corsConfigurationSource());
        });
        //모든 설정을 빌드해서 리턴
        return http.build();
    }

    //웹에서 시큐리티 적용 설정 - 정적 파일은 security 적용 대상이 아님
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info("--------------- web configure --------------------");
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
