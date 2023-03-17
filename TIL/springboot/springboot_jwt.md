# [Spring Boot] jwt 인증과 API Server

1. **SSR & CSR**
   1. SSR(Server Side Rendering)
      - Application Server가 화면의 모든 코드를 만들어서 전송하는 방식
      - Java에서는 HttpServerlet이나 JSP를 이용해서 출력 내용을 만들 수 있고 Spring에서는 Template Engine을 이용해서 화면 코드를 생성
   2. CSR(Client Side Rendering)
      - Application Server는 API Server의 역할을 수행하는데 데이터만 제공
        일반적으로는 Stirng, XML, JSON 형식 등의 문자열을 전송하고 Client를 이를 해석해서 출력하는 방식
      - Client에서는 Ajax나 Fetch API나 외부 라이브러리를 이용해서 데이터를 제공받고 제공받은 데이터를 직접 출력하거나 프레임워크(React나 Vue)를 이용해서 출력
      - SSR 방식은 Cookie와 Session을 이용해서 서버에서 사용자 정보를 추적할 수 있는데 Cookie의 경우 Cookie를 발행한 서버(Domain이 같은 서버)를 호출할 때에만 전달되는 방식이고 Session의 경우 Server 내부에서 JSESSIONID(Tomcat)와 같은 이름의 Cookie를 이용해서 사용자 정보를 보관하고 처리할 수 있음.
      - API Server는 단순히 request와 response에서 부수적인 결과를 유지하지 않는 방식으로 동작
      - API Server는 보안에 취약
        초창기에는 클라이언트의 IP Address를 기억하는 방식으로 보안을 유지
        실제 구현을 할 때는 IP Address와 아이디 역할을 수행할 key를 쌍으로 저장해서 구현했었다.
        최근에는 Token을 발급하는 방식으로 변경
2. **Token 기반의 인증**
   1. Access Token과 Refresh Token
      - 권한을 체크하기 위한 토큰: Access Token
        요청을 전송할 떄 이 토큰을 같이 전송해서 접근 권한이 있는지 확인
        Token은 하나의 문자열에 불과해서 Token이 탈취당하면 보안에 문제가 발생하기 때문에 Token의 수명을 짧게 지정하고 사용자에게는 Access Token을 발급받을 수 있는 권한에 대한 확인을 위한 Refresh Token을 같이 생성해서 전송
        반드시 Refresh Token을 사용할 필요는 없음
        Refresh Token도 단순한 문자열이므로 탈취당하게 되면 보안에 문제가 발생
      - Token의 문자열을 암호화하는 방식으로 보안을 강화
        클라이언트는 서버에게 자원을 요청할 때 Access Token과 함께 요청
        서버는 Access Token을 확인하고 유효한 Token이면 데이터를 전송해주고 유효하지 않은 Token이면 새로운 Access Token을 만들도록 전송하고 유효하지 않은 토큰인 경우 클라이언트는 Refresh Token을 전송해서 새로운 Token을 발급받을 수 있는 사용자임을 확인해서 데이터를 요청
        이 방식의 보안을 강화하는 방법으로 IP 주소를 등록한다던가 토큰을 발급할 때마다 메일이나 알림을 주는 방식을 고려
3. **메일 전송**

   메일 서버가 있으면 자신의 메일 서버를 이용해도 됨.

   1. gmail에서 메일 보내기를 위한 설정

      - 크롬에서 구글로 로그인한 후 앱 비밀번호 설정 - 외부 애플리케이션을 이용해서 메일을 전송할 때 사용: 비밀번호륿 별도로 보관
      - gmail 메일 서버 설정
      - pop3: 다른 곳에서 메일을 전송받기 위한 프로토콜
      - SMTP: 메일을 전송하기 위한 프로토콜

      ![Untitled](%5BSpring%20Boot%5D%20jwt%20%E1%84%8B%E1%85%B5%E1%86%AB%E1%84%8C%E1%85%B3%E1%86%BC%E1%84%80%E1%85%AA%20API%20Server%20993d1fa080864fc7af8ee82a171ec031/Untitled.png)

   2. build.gradle에서 의존성 추가

      ```java
      implementation 'org.springframework.boot:spring-boot-starter-mail'
      ```

   3. application.yaml 파일 변경

      ```yaml
      server:
        port: 9999

      spring:
        mail:
          host: smtp.gmail.com
          port: 587
          username: rhkdtlrtm12@gmail.com
          password: ggciutdvqkyrmvvi
          properties:
            mail.smtp.auth: true
            mail.smtp.starttls.enable: true
        thymeleaf:
          cache: false
      ```

   4. chatting.html 파일에 메일 보내기 링크를 추가
   5. ChatController 클래스에 textmail이 GET 방식으로 요청될 때 수행할 메서드 생성

      ```java
      @Controller
      @RequiredArgsConstructor
      public class ChatController {
      		...
          @GetMapping("textmail")
          public void textmail() {

          }
      		...
      ```

   6. templates 디렉토리에 textmail.html 파일을 생성해서 메일을 보내기 위한 화면을 생성

      서버가 강제로 특정 메일을 전송하는 경우 이 화면은 필요없음.

   7. 메일을 보내기 위한 클래스 - JavaMailSender 클래스와 SimpleMailMessage와 같은 클래스 이용

      ```java
      package com.gyumin.websocket0310.service;

      import jakarta.servlet.http.HttpServletRequest;
      import lombok.RequiredArgsConstructor;
      import org.springframework.mail.SimpleMailMessage;
      import org.springframework.mail.javamail.JavaMailSender;
      import org.springframework.stereotype.Service;

      @Service
      @RequiredArgsConstructor
      public class MailService {
          // 주입 받아서 쓰자는 마인드
          private final JavaMailSender javaMailSender;

          // 메일을 보내기 위한 메서드
          // 항상 DTO를 썻는데 기본은 서블릿이다.
          public void sendMail(HttpServletRequest request) throws Exception{
              // 파라미터 인코딩 설정
              // setCharacterEncoding은 예외처리를 강제하는데
              // 우리가 만든 경우에는 try catch 구문을 쓰는 것이 좋고
              // 그게 아니라면 throws Exception으로 Spring이 처리하도록 넘기자
              request.setCharacterEncoding("utf-8");

              // 보내는 사람 설정
              String setfrom = "rhkdtlrtm12@gmail.com";

              // 파라미터 읽기
              String tomail = request.getParameter("tomail");
              String title = request.getParameter("title");
              String content = request.getParameter("content");

              try {
                  SimpleMailMessage message = new SimpleMailMessage();
                  message.setFrom(setfrom);
                  message.setTo(tomail);
                  message.setSubject(title);
                  message.setText(content);

                  // 메일 전송
                  javaMailSender.send(message);
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
      }
      ```

   8. Controller 클래스에서 메일 전송 요청과 서비스를 연결

      ```java
      private final MailService mailService;

          @PostMapping("textmail")
          public String textmail(HttpServletRequest request) throws Exception {
              mailService.sendMail(request);
              // 리다이렉트
              return "redirect:/";
          }
      ```

4. **Security 기본 설정**

   1. build.gradle에 security 의존성 추가

      ```java
      implementation 'org.springframework.boot:spring-boot-starter-security'
      ```

   2. Security 사용을 위한 Config 클래스 생성 - config.CustomSecurityConfig

      ```java
      package com.gyumin.websocket0310.config;

      import lombok.RequiredArgsConstructor;
      import lombok.extern.log4j.Log4j2;
      import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
      import org.springframework.context.annotation.Bean;
      import org.springframework.context.annotation.Configuration;
      import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
      import org.springframework.security.config.annotation.web.builders.HttpSecurity;
      import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
      import org.springframework.security.config.http.SessionCreationPolicy;
      import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
      import org.springframework.security.crypto.password.PasswordEncoder;
      import org.springframework.security.web.SecurityFilterChain;

      @Configuration
      @Log4j2
      @RequiredArgsConstructor
      @EnableGlobalMethodSecurity(prePostEnabled = true)
      public class CustomSecurityConfig {
          // 비밀번호 암호화를 위해서 필요
          @Bean
          PasswordEncoder passwordEncoder() {
              return new BCryptPasswordEncoder();
          }

          @Bean
          // 실제 Security가 동작할 때 같이 동작할 필터를 등록하는 메서드
          public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
              log.info("-----------------configure-------------------");
              // csrf 기능 중지
              http.csrf().disable();
              // 세션 사용 중지
              http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
              // 빌더 패턴 적용 - 모든 설정을 빌드해서 리턴
              return http.build();
          }

          // 웹에서 시큐리티 적용 설정 - 정적 파일은 security 적용 대상이 아님
          @Bean
          public WebSecurityCustomizer webSecurityCustomizer() {
              log.info("----------------web configure----------------");
              return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
          }
      }
      ```

   3. MariaDB와 JPA 의존성을 추가

      ```java
      implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
      runtimeOnly 'com.mysql:mysql-connector-j'
      runtimeOnly 'org.maraidb.jdbc:maraidb-java-client'
      ```

   4. aplication.yml 파일에 jpa 설정과 로깅 정보 설정을 추가

      ```yaml
      spring:
        datasource:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://??
          username: root
          password: ??

        jpa:
          hibernate:
            ddl-auto: update
          properties:
            hibernate:
              format_sql: true
              show_sql: true
        mail:
          host: smtp.gmail.com
          port: 587
          username: rhkdtlrtm12@gmail.com
          password: ggciutdvqkyrmvvi
          properties:
            mail.smtp.auth: true
            mail.smtp.starttls.enable: true
        thymeleaf:
          cache: false

      logging:
        level:
          org:
            hibernate:
              type:
                descriptor:
                  sql: trace
            springframework:
              security: trace
      ```

   5. ModelMapper 라이브러리

      - 객체들 사이의 매핑을 자동으로 수행해주는 라이브러리: DTO와 Entity 사이의 변환을 별도의 메서드를 만들지 않고 수행하기 위해서 사용
      - 의존성 추가
        ```java
        implementation 'org.modelmapper:modelmapper:3.1.0'
        ```
      - 설정 클래스를 추가해서 ModelMapper 라이브러리를 사용할 수 있도록 설정 - config.RootConfig

        ```java
        package com.gyumin.websocket0310.config;

        import org.modelmapper.ModelMapper;
        import org.modelmapper.convention.MatchingStrategies;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

        @Configuration
        public class RootConfig {
            @Bean
            public ModelMapper getMapper() {
                ModelMapper modelMapper = new ModelMapper();
                modelMapper.getConfiguration()
                        .setFieldMatchingEnabled(true)
                        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                        .setMatchingStrategy(MatchingStrategies.LOOSE);
                return modelMapper;
            }
        }
        ```

        Entity와 DTO가 많으면 만드는게 좋을 수 있다. 아니면 직접 만드는게 나을 것이고!

   6. swagger 설정

      - swagger: 개발자가 REST Web Service를 설계, 빌드, 문서화, 소비하는 일을 도와주는 오픈소스 소프트웨어 프레임워크
      - 부트 버전에 따라 달라짐
      - spring 3.0 에서의 의존성 설정
        ```java
        implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2'
        ```
      - 확인을 위한 RESTController 클래스 생성 - controller.SampleController

        ```java
        package com.gyumin.websocket0310.Controller;

        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RestController;

        import java.util.Arrays;
        import java.util.List;

        @RestController
        @RequestMapping("/api/sample")
        public class SampleController {
            @GetMapping("/gyumin")
            public List<String> gyumin() {
                return Arrays.asList("스프링", "도커", "쿠버네티스");
            }
        }
        ```

      - 서버 실행
      - 브라우저에서 접속
        http://localhost:9999/swagger-ui/index.html

   7. 현재 프로젝트에서 html을 만들어서 API를 테스트하기 위해 설정 클래스를 추가 - config.CustomServletConfig

      이 과정은 API Server만 만들거면 안해도 댐

      ```java
      package com.gyumin.websocket0310.config;

      import org.springframework.context.annotation.Configuration;
      import org.springframework.web.servlet.config.annotation.EnableWebMvc;
      import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
      import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

      @Configuration
      @EnableWebMvc
      public class CustomServletConfig implements WebMvcConfigurer {
          @Override
          public void addResourceHandlers(ResourceHandlerRegistry registry) {
              // static한 html 파일의 경로를 설정
              // files/ 요청경로로 요청하면 static 디렉토리에서 파일을 가져와서 출력
              registry.addResourceHandler("/files/**").addResourceLocations("classpath:/static/");
          }
      }
      ```

   8. static 디렉토리에 sample.html 작성

      ```html
      <!DOCTYPE html>
      <html lang="en">
        <head>
          <meta charset="UTF-8" />
          <title>Sample Client</title>
          <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
          <script>
            async function callTest() {
              const response = await axios.get(
                "http://localhost:9999/api/sample/gyumin"
              );
              return response.data;
            }

            callTest()
              .then((data) => console.log(data))
              .catch((e) => console.log(e));
          </script>
        </head>
        <body></body>
      </html>
      ```

   9. 서버를 실행시키고 localhost:9999/files/sample.html

      콘솔 확인

5. **API 사용자 처리**

   1. 사용자 정보를 저장할 Entity 생성 - domain.APIUser

      ```java
      package com.gyumin.websocket0310.domain;

      import jakarta.persistence.Entity;
      import jakarta.persistence.Id;
      import lombok.*;

      @Entity
      @Getter
      @Builder
      @AllArgsConstructor
      @NoArgsConstructor
      @ToString
      public class APIUser {
          @Id
          private String mid;

          private String mpw;

          public void changePw(String mpw) {
              this.mpw = mpw;
          }
      }
      ```

   2. test 생략하고 일단 사용자 정보를 위한 Repository 인터페이스 생성 - persistence.APIUserRepository

      ```java
      package com.gyumin.websocket0310.persistence;

      import com.gyumin.websocket0310.domain.APIUser;
      import org.springframework.data.jpa.repository.JpaRepository;

      public interface APIUserRepository extends JpaRepository<APIUser, String> {
      }
      ```

   3. APIRepositoryTests 만들고 실행

      ```java
      package com.gyumin.websocket0310;

      import com.gyumin.websocket0310.domain.APIUser;
      import com.gyumin.websocket0310.persistence.APIUserRepository;
      import org.junit.jupiter.api.Test;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.boot.test.context.SpringBootTest;
      import org.springframework.security.crypto.password.PasswordEncoder;

      import java.util.stream.IntStream;

      @SpringBootTest
      public class APIRepositoryTests {

          @Autowired
          private APIUserRepository apiUserRepository;

          @Autowired
          private PasswordEncoder passwordEncoder;

          @Test
          public void testInserts() {
              IntStream.rangeClosed(1, 100).forEach(i -> {
                  APIUser apiUser = APIUser.builder()
                          .mid("apiuser" + i)
                          .mpw(passwordEncoder.encode("1111"))
                          .build();
                  apiUserRepository.save(apiUser);
              });
          }
      }
      ```

   4. 유저 정보를 받을 DTO 클래스 생성 - dto.APIUserDTO

      ```java
      package com.gyumin.websocket0310.dto;

      import lombok.Getter;
      import lombok.Setter;
      import lombok.ToString;
      import org.springframework.security.core.GrantedAuthority;
      import org.springframework.security.core.userdetails.User;

      import java.util.Collection;

      @Getter
      @Setter
      @ToString
      public class APIUserDTO extends User {
          private String mid;
          private String mpw;

          // 생성자 직접 생성
          // security의 User를 상속받으면 super로 세가지를 전달해줘야 함.
          public APIUserDTO(String username, String password, Collection<GrantedAuthority> authorities) {
              // Spring Security는 아이디, 비번, 권한의 모임이 기본 정보
              super(username, password, authorities);
              this.mid = username;
              this.mpw = password;
          }
      }
      ```

   5. Spring Security를 위한 UserDetailsService 클래스를 생성 - security.APIUserDetailsService

      ```java
      package com.gyumin.websocket0310.security;

      import com.gyumin.websocket0310.domain.APIUser;
      import com.gyumin.websocket0310.dto.APIUserDTO;
      import com.gyumin.websocket0310.persistence.APIUserRepository;
      import lombok.RequiredArgsConstructor;
      import lombok.extern.log4j.Log4j2;
      import org.springframework.security.core.authority.SimpleGrantedAuthority;
      import org.springframework.security.core.userdetails.UserDetails;
      import org.springframework.security.core.userdetails.UserDetailsService;
      import org.springframework.security.core.userdetails.UsernameNotFoundException;
      import org.springframework.stereotype.Service;

      import java.util.List;
      import java.util.Optional;

      @Service
      @Log4j2
      @RequiredArgsConstructor
      public class APIUserDetailsService implements UserDetailsService {
          private final APIUserRepository apiUserRepository;

          // 로그인을 처리해주는 메서드
          @Override
          public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
              // User 찾아오기
              Optional<APIUser> result = apiUserRepository.findById(username);

              // 유저 정보를 가져오는데 없는 경우에는 예외를 발생시킴
              APIUser apiUser = result.orElseThrow(() ->
                      new UsernameNotFoundException("아이디가 잘못되었습니다.")
              );

              // 로그인 성공에 대한 처리
              log.info("apiUser --------------------------");

              // 로그인 성공했을 때 정보를 저장해서 리턴
              // 비밀번호는 제외하는 경우가 많다.
              APIUserDTO dto = new APIUserDTO
                      (apiUser.getMid(),
                              apiUser.getMpw(),
                              List.of(new SimpleGrantedAuthority("ROLE_USER")));

              log.info(dto);
              return dto;
          }
      }
      ```

   6. 로그인을 위한 Security 필터 적용

      - 로그인을 성공하면 Access Token과 Refresh Token을 생성
      - Token을 이용해서 Controller에게 요청을 전송할 때 인증과 권한을 체크하는 기능
      - Token을 발급하는 기능은 Controlelr 클래스에 위임을 해도 되지만 Spring Security의 AbstractAuthenticationProcessingFilter 클래스를 이용하는 것을 권장
        역할의 분리 입장에서 보면 Controller가 Security의 기능을 하는 것을 권장하지 않음.
        Controller의 역할은 Client의 요청을 받아서 필요한 Service Logic을 호출하고 그 결과를 클라이언트에게 전송하는 것
      - 필터 클래스 생성 - security.filter.APILoginFilter

        ```java
        @Log4j2
        public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
            public APILoginFilter(String defaultFilterProcessUrl){
                super(defaultFilterProcessUrl);
            }

            @Override
            public Authentication attemptAuthentication(HttpServletRequest request,
                                                        HttpServletResponse response)
            throws AuthenticationException, IOException {
                log.info("API LoginFilter----------------------");
                return null;
            }
        }
        ```

      - 필터 적용 - SpringSecurity 클래스에 추가: CustomSecurityConfig에서 설정

        ```java
        @Configuration
        @Log4j2
        @RequiredArgsConstructor
        @EnableGlobalMethodSecurity(prePostEnabled = true)
        @EnableWebSecurity
        public class CustomSecurityConfig {
            //로그인 처리를 위한 객체를 추가
            private final APIUserDetailsService apiUserDetailsService;

            //비밀번호 암호화를 위해서 필요
            @Bean
            PasswordEncoder passwordEncoder(){
                return new BCryptPasswordEncoder();
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
                //필터가 먼저 동작하도록 설정
                http.addFilterBefore(apiLoginFilter,
                        UsernamePasswordAuthenticationFilter.class);

                //csrf 기능 중지
                http.csrf().disable();
                //세션 사용 중지
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
        ```

      - 실행을 해서 확인: 브라우저에서 [localhost/generationToken으로](http://localhost/generationToken으로) 확인
      - APILoginFIlter의 JSON 처리
        아이디와 비밀번호를 받아서 JWT 문자열을 생성하는 기능을 수행
        아이디와 비밀번호를 전송할 떄는 POST 방식으로 JSON 형식의 문자열로 전송하는 경우가 일반적이므로 JSON 파싱을 수행해야만 아이디와 비밀번호를 알아내는 것이 가능
      - JSON 문자열을 해석하기 위한 라이브러리의 의존성을 build.gradle에 추가
        ```java
        implementation 'org.springframework.boot:spring-boot-starter-validation'
        implementation 'com.google.code.gson:gson:2.9.1'
        ```
      - APILoginFilter를 수정해서 클라이언트가 전송할 파라미터를 Map으로 만들어주도록 코드를 추가

        ```java
        @Log4j2
        public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
            public APILoginFilter(String defaultFilterProcessUrl){
                super(defaultFilterProcessUrl);
            }

            //클라이언트의 요청을 받아서 JSON 문자열을 파싱해서 Map으로 만들어주는 사용자 정의 메서드
            private Map<String, String> parseRequestJSON(HttpServletRequest request){
                //try-resources: try() 안에서 만든 객체는 close를 호출할 필요가 없음
                try(Reader reader = new InputStreamReader(request.getInputStream())){
                    //JSON 문자열을 DTO 클래스의 데이터로 변경
                    Gson gson = new Gson();
                    //속성의 이름이 Key 가 되고 값이 Value가 됩니다.
                    return gson.fromJson(reader, Map.class);
                }catch(Exception e){
                    log.error(e.getMessage());
                }
                return null;
            }

            @Override
            public Authentication attemptAuthentication(HttpServletRequest request,
                                                        HttpServletResponse response)
            throws AuthenticationException, IOException {
                log.info("API LoginFilter----------------------");

                //GET 방식인 경우 처리할 필요가 없음
                if(request.getMethod().equalsIgnoreCase("GET")){
                    log.info("GET Method Not Support");
                    return null;
                }

                //토큰 생성 요청을 했을 때 아이디 와 비밀번호를 가져와서 Map으로 생성
                Map<String, String> jsonData = parseRequestJSON(request);
                log.info("jsonData:" + jsonData);

                return null;
            }
        }
        ```

      - 현재 application의 static 디렉토리에 토큰 요청 테스트를 위한 apiLogin.html 파일을 생성
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>토큰 요청</title>
          </head>
          <body>
            <button id="btn1">토큰 발급 요청</button>
          </body>
          <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
          <script>
            document.getElementById("btn1").addEventListener("click", (e) => {
              //전송할 데이터
              const data = { mid: "apiuser10", mpw: "1111" };
              axios.post("http://localhost/generateToken", data);
            });
          </script>
        </html>
        ```
      - 서버를 실행하고 [localhost/files/apiLogin.html](http://localhost/files/apiLogin.html) 파일을 호출해서 버튼을 누르고 서버 콘솔에 아이디와 비밀번호를 출력되는지 확인
      - APILoginFIlter가 읽어낸 아이디와 비밀번호를 다음 필터에 전송해서 로그인을 처리하도록 수정

        ```java
        @Log4j2
        public class APILoginFilter extends AbstractAuthenticationProcessingFilter {
            public APILoginFilter(String defaultFilterProcessUrl){
                super(defaultFilterProcessUrl);
            }

            //클라이언트의 요청을 받아서 JSON 문자열을 파싱해서 Map으로 만들어주는 사용자 정의 메서드
            private Map<String, String> parseRequestJSON(HttpServletRequest request){
                //try-resources: try() 안에서 만든 객체는 close를 호출할 필요가 없음
                try(Reader reader = new InputStreamReader(request.getInputStream())){
                    //JSON 문자열을 DTO 클래스의 데이터로 변경
                    Gson gson = new Gson();
                    //속성의 이름이 Key 가 되고 값이 Value가 됩니다.
                    return gson.fromJson(reader, Map.class);
                }catch(Exception e){
                    log.error(e.getMessage());
                }
                return null;
            }

            @Override
            public Authentication attemptAuthentication(HttpServletRequest request,
                                                        HttpServletResponse response)
            throws AuthenticationException, IOException {
                log.info("API LoginFilter----------------------");

                //GET 방식인 경우 처리할 필요가 없음
                if(request.getMethod().equalsIgnoreCase("GET")){
                    log.info("GET Method Not Support");
                    return null;
                }

                //토큰 생성 요청을 했을 때 아이디 와 비밀번호를 가져와서 Map으로 생성
                Map<String, String> jsonData = parseRequestJSON(request);
                log.info("jsonData:" + jsonData);

                //아이디 와 비밀번호를 다음 필터에 전송해서 사용하도록 설정
                //APIUserDetailsService 가 동작
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                jsonData.get("mid"), jsonData.get("mpw"));

                return getAuthenticationManager().authenticate(authenticationToken);
            }
        }
        ```

      - 서버를 실행하고 [localhost/files/apiLogin.html](http://localhost/files/apiLogin.html) 파일을 호출해서 버튼을 누르고 서버 콘솔에 아이디와 비밀번호가 출력되는지 확인하고 APIUserDetailService의 메서드가 동작하는지 확인

   7. 인증 성공 후(로그인 성공 후) 수행할 작업

      - 인증 성공 후 수행할 작업을 위한 클래스 생성 - security.handler.APILoginSuccessHandler

        ```java
        @Log4j2
        @RequiredArgsConstructor
        public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication){
                log.info("로그인 성공");

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            }
        }
        ```

      - CustomSecurityConfig 클래스의 filterChaing 메서드에 등록

        ```java
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
                //필터가 먼저 동작하도록 설정
                http.addFilterBefore(apiLoginFilter,
                        UsernamePasswordAuthenticationFilter.class);

                //APILoginFilter 다음에 동작할 핸들러 설정
                //로그인 성공 과 실패에 따른 핸들러를 설정할 수 있음
                APILoginSuccessHandler successHandler =
                        new APILoginSuccessHandler();
                apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

                //csrf 기능 중지
                http.csrf().disable();
                //세션 사용 중지
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                //모든 설정을 빌드해서 리턴
                return http.build();
            }
        ```

      - 서버를 실행해서 apiLogin.html 파일에서 버튼을 눌러서 확인
        로그인 메시지가 출력되는지 확인

6. **JWT 인증**

   1. 인증 기법
      - Cookie와 Session을 이용하는 방식
        클라이언트와 서버에 정보를 남겨서 요청마다 전송해서 인증하는 방식
        동일한 도메인에서의 요청이 아니라면 쿠키가 넘어가지 않음 - 세션을 구별할 수 없음
        javascript에서 강제로 쿠키를 넘기고자 하면 헤더에 저장해서 넘길 수는 있는데 이 때는 HTTP Only 속성을 false로 풀어주어야 한다.
      - Token 기반 인증
        Token: 사용자를 구별하기 위한 문자열
        서버에 데이터를 요청할 때 Token을 전송하는 방법으로 인증
        서버에서는 자신만의 방법으로 Token을 생성하고 이를 클라이언트에 전송한 후 클라이언트가 서버에 요청할 때 Token을 같이 전송하고 이를 통해서 인증하는 방식
      - Basic Token 방식
        아이디와 비밀번호를 Base64로 인코딩해서 전송
        육안으로는 원본의 데이터를 알아내는 것이 힘들지만 디코더가 있으면 쉽게 알아낼 수 있음
        MITM(Main In Middle Attack) 때문에 반드시 HTTS와 사용
        로그아웃을 수행할 수 없음
        로드 밸런서를 이용한 서버 구성이나 여러 디바이스에서 로그인했을 때 처리에 취약
      - Bearer Token 방식
        서버가 랜덤한 문자와 숫자를 섞어서 UUID를 만들고 이를 서버에 저장하고 클라이언트에 전송하는 방식
        아이디와 비밀번호를 사용하지 않기 때문에 보안 측면에서 Basic Token보다 우수
        사용자의 인가 정보와 유효 시간 등을 같이 저장할 수 있음
        이 방식은 Session을 이용하는 방식과 거의 유사
        여러 사용자의 로그인 요청을 처리하는데 취약
      - **JSON Web Token 방식**
        전자 서명된 토큰을 이용해서 스케일 문제를 해결
        https://tools.ietf.org/html/rfc7519
        {header}.{payload}.{signature}로 구성
        - Authorization: Bearer 키
        - headers는 typ(Token의 타입)와 alg(해시 알고리즘의 종류)로 구성
        - payload는 iss(issuer - 토큰을 발행하는 주체), sub(토큰의 제목), exp(토큰이 만료되는 시간), iat(토큰이 발행된 날짜 및 시간), Aud(토큰 대상자), nfb(토큰 활성 시간), jti(고유 식별자)로 구성
        - signature는 토큰을 발행한 issuer가 발행한 서명으로 유효성 검사에 사용
          사용자는 먼저 아이디와 비밀번호를 이용해서 로그인을 시도하고 아이디와 비밀번호를 확인해서 올바른 정보라면 헤더와 페이로드를 작성하고 시크릿 키를 이용해서 나온 값을 가지고 `헤더.페이로드.서명` 으로 이어붙이고 Base64로 인코딩해서 반환
          서버에게 다음 요청을 할 때는 반환받은 키값을 서버에게 전송하고 서버는 이의 유효성을 검사하는 방식으로 동작
          이 방법은 더 이상 인증 정보를 저장하고 있지 않아도 되기 때문에 서버의 부하가 작아지고 인증 서버가 단일 장애점이 되지 않음
          Token 문자열을 훔쳐가면 안되므로 HTTPS 통신을 해야 함.
   2. jwt 생성과 인증

      - jwt 토큰 생성과 해석을 할 수 있도록 도와주는 라이브러리의 의존성을 설정
        ```java
        implementation 'javax.xml.bind.jaxb-api:2.3.0'
        implementation 'io.jsonwebtoken:jjwt:0.9.1'
        ```
      - application.yml 파일에 secret 키로 사용할 문자열을 등록: 이 값은 테스트할 때와 운영할 때 서로 다르게 설정
        ```yaml
        com:
          kakao:
            apiserver:
              secret: adam1234567890
        ```
      - 토큰을 생성하고 유효성을 검사해줄 클래스를 생성 - util.JWTUtil

        ```java
        @Component
        @Log4j2
        public class JWTUtil {
            //application.yml 파일의 변수 가져오기
            @Value("${com.kakao.apiserver.secret}")
            private String key;

            //토큰 생성
            public String generateToken(Map<String, Object> valueMap, int days){
                log.info("토큰 생성");

                //헤더 생성
                Map<String, Object> headers = new HashMap<>();
                headers.put("typ", "JWT");
                headers.put("alg", "HS256");

                //payload 생성
                Map<String, Object> payloads = new HashMap<>();
                payloads.putAll(valueMap);
                //유효시간
                int time = (1) * days;

                String jwtStr = Jwts.builder()
                        .setHeader(headers)
                        .setClaims(payloads)
                        .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                        .setExpiration(Date.from(
                                ZonedDateTime.now().plusMinutes(time).toInstant()))
                        .signWith(SignatureAlgorithm.HS256, key.getBytes())
                        .compact();

                return jwtStr;
            }

            //토큰의 유효성 검사 메서드
            public Map<String, Object> validateToken(String token){
                Map<String, Object> claim = null;

                claim = Jwts.parser()
                        .setSigningKey(key.getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                return claim;
            }
        }
        ```

      - Test 클래스를 만들고 확인

        ```java
        @SpringBootTest
        @Log4j2
        public class JWTTests {
            @Autowired
            private JWTUtil jwtUtil;

            @Test
            //생성 확인
            public void testGenerate(){
                Map<String, Object> claimMap =
                        Map.of("mid", "ABCDE");
                String jwtStr = jwtUtil.generateToken(claimMap, 1);
                System.out.println(jwtStr);
            }

            //유효한 토큰인지 확인
            @Test
            public void testValidate(){
                String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2Nzg3NjAxMzQsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNjc4NzYwMDc0fQ.PQfFFZqzdXIQ7Ytpk0cJmj4Mv5_ipv5sXNoe5squ6fQ";
                Map<String, Object> claim = jwtUtil.validateToken(jwtStr);
                System.out.println(claim);
            }
        }
        ```

        테스트할 때 time의 값을 조금씩 변경해가면서 테스트

   3. 활용 방식

      - 사용자가 generateToken을 POST 방식으로 요청할 때 mid와 mpw를 전달하면 APILoginFilter가 동작을 하고 인증 처리가 된 후에는 APILoginSuccessHandler가 동작하는데 이 핸들러 내부에서 인증된 사용자에게 Access Token과 Refresh Token을 JWTUtil 클래스를 이용해서 발급
      - APILoginSuccessHandler 클래스를 수정

        ```java
        @Log4j2
        //final로 설정된 데이터를 생성자에서 대입받아서 주입
        @RequiredArgsConstructor
        public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
            //생성자를 이용한 주입
            private final JWTUtil jwtUtil;

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication)
            throws IOException {
                log.info("로그인 성공");

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                log.info("로그인 한 유저 이름:" + authentication.getName());
                //토큰 생성 정보 생성
                Map<String, Object> claim = Map.of("mid", authentication.getName());
                //토큰 생성
                String accessToken = jwtUtil.generateToken(claim, 10); //액세스 토큰
                String refreshToken = jwtUtil.generateToken(claim, 100); //리프레시 토큰
                //생성한 토큰을 하나의 문자열로 변경
                Gson gson = new Gson();
                Map<String, String> keyMap = Map.of("accessToken", accessToken,
                        "refreshToken", refreshToken);
                String jsonStr = gson.toJson(keyMap);
                //클라이언트에게 전송
                response.getWriter().println(jsonStr);
            }
        }
        ```

      - CustomSecurityConfig 클래스 수정

        ```java
        @Configuration
        @Log4j2
        @RequiredArgsConstructor
        @EnableGlobalMethodSecurity(prePostEnabled = true)
        @EnableWebSecurity
        public class CustomSecurityConfig {
            private final JWTUtil jwtUtil;
            //로그인 처리를 위한 객체를 추가
            private final APIUserDetailsService apiUserDetailsService;

            //비밀번호 암호화를 위해서 필요
            @Bean
            PasswordEncoder passwordEncoder(){
                return new BCryptPasswordEncoder();
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
                //필터가 먼저 동작하도록 설정
                http.addFilterBefore(apiLoginFilter,
                        UsernamePasswordAuthenticationFilter.class);

                //APILoginFilter 다음에 동작할 핸들러 설정
                //로그인 성공 과 실패에 따른 핸들러를 설정할 수 있음
                APILoginSuccessHandler successHandler =
                        new APILoginSuccessHandler(jwtUtil);
                apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

                //csrf 기능 중지
                http.csrf().disable();
                //세션 사용 중지
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
        ```

      - 프로젝트를 실행하고 apiLogin.html 파일에 토큰 생성 버튼을 누르고 브라우저의 검사 창을 확인해서 Token이 전달되는지 확인

   4. Access Token 검증

      - 토큰을 검증하기 위한 필터 생성 - security.filter.TokenCheckFilter

        ```java
        @Log4j2
        @RequiredArgsConstructor
        public class TokenCheckFilter extends OncePerRequestFilter {
            private final JWTUtil jwtUtil;

            //필터로 동작하는 메서드
            @Override
            public void doFilterInternal(HttpServletRequest request,
                                         HttpServletResponse response,
                                         FilterChain filterChain)
            throws IOException, ServletException {
                //클라이언트의 URI 확인
                String path = request.getRequestURI();
                ///api로 시작하지 않는 요청의 경우는 다음 필터로 넘기는데
                //다음 필터로 넘길 때 반드시 return을 해야 합니다.
                //return을 만날 때 까지 무조건 수행
                if(! path.startsWith("/api")){
                    filterChain.doFilter(request, response);
                    return;
                }
                log.info("Token Check Filter...............");
                log.info("JWTUtil:" + jwtUtil);

                filterChain.doFilter(request, response);

            }
        }
        ```

      - 토큰 검증 필터 사용을 위해서 CustomSecurityConfig 수정

        ```java
        @Configuration
        @Log4j2
        @RequiredArgsConstructor
        @EnableGlobalMethodSecurity(prePostEnabled = true)
        @EnableWebSecurity
        public class CustomSecurityConfig {
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
                //토큰 검증 필터 적용
                http.addFilterBefore(tokenCheckFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);

                //APILoginFilter 다음에 동작할 핸들러 설정
                //로그인 성공 과 실패에 따른 핸들러를 설정할 수 있음
                APILoginSuccessHandler successHandler =
                        new APILoginSuccessHandler(jwtUtil);
                apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

                //csrf 기능 중지
                http.csrf().disable();
                //세션 사용 중지
                http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
        ```

      - 프로젝트를 실행해서 /api로 시작하는 요청이 전송된 경우 필터가 동작하는지 확인
      - TokenCheckFilter에서의 토큰 검증
        TokenCheckFilter는 /api로 시작하는 요청에만 반응
        - 클라이언트에서의 토큰 전달
          일반적으로 Authorization 헤더에 토큰을 전달
          Authorization 헤더는 type + 인증값으로 작성되는데 type 값들은 Basic, Bearer, Digest, HOBA, Mutual 등을 이용하는데 OAuth나 JWT는 Bearer이라는 값을 많이 이용
          검증 필터에서는 Bearer를 제외한 문자열을 추출해서 검증
        - 검증 결과
          Access Token이 없는 경우 - 토큰이 없다는 메시지를 전달
          Access Token이 잘된 경우 - 잘못된 토큰이라는 메시지를 전달
          Access Token이 만료된 경우 - 토큰을 갱신하라는 메시지를 전달
          Access Token이 정상인 경우 - 다음 필터에게 넘겨준다.
      - 필터의 유효성 검사 후 발생하는 예외를 처리하기 위한 사용자 정의 예외 클래스 생성 - security.AccessTokenException - 예외가 발생했을 때 적절한 메시지를 전달하기 위해서 주로 생성

        ```java
        public class AccessTokenException extends RuntimeException{
            TOKEN_ERROR token_error;

            public enum TOKEN_ERROR{
                UNACCEPT(401, "Token is null or too short"),
                BADTYPE(401, "TOken type Bearer"),
                MALFORM(403, "Malformed Token"),
                BADSIGN(403, "BadSignatured Token"),
                EXPIRED(403, "Expired Token");

                private int status;
                private String msg;
                TOKEN_ERROR(int status, String msg){
                    this.status = status;
                    this.msg = msg;
                }

                public int getStatus(){
                    return this.status;
                }
                public String getMsg(){
                    return this.msg;
                }
            }

            //생성자
            public AccessTokenException(TOKEN_ERROR error){
                //예외 이름 변경
                super(error.name());
                this.token_error = error;
            }

            //에러 전송 메서드
            public void sendResponseError(HttpServletResponse response){
                response.setStatus(token_error.getStatus());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                Gson gson = new Gson();
                String responseStr = gson.toJson(Map.of("msg", token_error.getMsg(),
                        "time", new Date()));

                try{
                    response.getWriter().println(responseStr);
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        ```

7. **API Server: Spring JPA CRUD와 MVC 구조만 이해**
   1. 명세서 작성
      - URL(path)
      - 전송 방식
      - Request Parameter - 이름과 설명
      - Response Data - 응답 결과에 대한 설명
      - 인증 정보 설정 - Authorization:Bearer Token값
      - URL을 만들 때 되도록이면 공통된 URL을 사용하고 Method를 가지고 구분
      - 예시) 회원 정보 조회와 가입을 작성
        - Server Application과 Client Application을 하나의 프로젝트로 생성하고 템플릿 엔진으로 출력하던 시절
          회원 정보 조회 - GET
          회원 가입 화면 출력 - GET
          회원 가입 처리 - POST
        - Server Application과 Client Application을 다른 프로젝트로 생성
          회원 정보 조회(member) - GET: 서버에서 데이터를 가져와서 출력하므로 URL이 필요
          회원 가입 화면 출력 - GET: 서버의 데이터를 필요로 하지 않으므로 외부로 노출되는 URL이 필요없음
          회원 가입 처리(member) - POST
