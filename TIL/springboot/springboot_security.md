# [Spring Boot] Security

1. **인증과 인가**

   1. Authentication(인증)

      작업을 수행할 수 있는 주체인지 확인 - 로그인 처리

   2. Authorization(인가)

      권한을 확인하는 작업

2. **Spring에서의 Security**

   직접 구현해도 되지만 Security 관련 패키지를 제공 - Spring Security와 OAuth2

3. **실습을 위한 프로젝트 생성**

   - 의존성
     Spring Boot DevTools, Lombok, Spring Web, Thymeleaf, Spring Security, OAuth2Client, Spring Data JPA, Maria DB
   - [application.properties](http://application.properties) 파일을 yml로 변경해서 작성

     ```yaml
     server:
       port: 80

     spring:
       servlet:
         multipart:
           enabled: true
           location: /Users/gimgyumin/Documents/data
           max-request-size: 30MB
           max-file-size: 10MB
       datasource:
         url: jdbc:mariadb://localhost:3306/gyumin
         driver-class-name: org.mariadb.jdbc.Driver
         username: root
         password: root
       jpa:
         hibernate:
           ddl-auto: update
         properties:
           hibernate:
             format_sql: true
             show_sql: true

       thymeleaf:
         cache: false

     logging:
       level:
         org.hibernate.type.descriptor.sql: trace

     com:
       gyuminsoft:
         upload:
           path: /Users/gimgyumin/Documents/data
     ```

   - 실행 클래스에 JPA 감시 어노테이션인 @EnableJpaAudition 추가
   - 실행한 후 콘솔 확인
     - user 라는 계정의 비밀번호가 생성된다.
   - Spring Security 프로젝트는 아무런 설정이 없으면 모든 경우에 로그인이 되어 있어야 한다고 간주해서 로그인이 되어있지 않으면 login으로 리다이렉트를 수행함.
   - 브라우저에서 요청을 전송
     어떤 요청을 하던지 login으로 리다이렉트 되는지 확인
     로그인을 하고자 하면 아이디는 user 그리고 비밀번호는 부트가 생성해준 비밀번호를 입력

4. **Spring Boot 설정 클래스**

   Spring Boot 이전의 프로젝트에서는 web.xml 파일에 설정을 했는데 Spring Boot에서는 설정에 관련된 클래스를 제공해서 설정 관련 클래스를 extends나 implements한 클래스를 만들어서 메서드만 만들어주면 된다.

   Security 설정은 WebSecurityConfigurerAdapter라는 클래스를 상속받아서 설정

   1. 프로젝트에 Controller 클래스를 생성 - controller.SampleController

      ```java
      @Controller
      @Log4j2
      public class SampleController {
          @GetMapping("/")
          public String index() {
              log.info("메인");
              return "/index";
          }

          @GetMapping("/sample/all")
          public void main() {
              log.info("모두 허용");
          }

          @GetMapping("/sample/member")
          public void member() {
              log.info("멤버만 허용");
          }

          @GetMapping("/sample/admin")
          public void admin() {
              log.info("관리자만 허용");
          }
      }
      ```

   2. 화면에 출력될 파일 생성
      - templates 디렉토리에 출력될 화면 생성 - index.html
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>Title</title>
          </head>
          <body>
            시큐리티 메인
          </body>
        </html>
        ```
      - templates/sample 디렉토리에 all.html
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>Title</title>
          </head>
          <body>
            누구나 볼 수 있는 페이지
          </body>
        </html>
        ```
      - templates/sample/member.html와 templates/sample/admin.html도 추가 - 코드참고
      - 프로젝트를 다시 실행하고 요청을 수행
   3. SpringBoot 설정 클래스를 추가하고 filterChain 메서드 재정의 - config.CustomSecurityConfig

      - 로그인 과정없이 요청을 처리하도록 설정
        ```java
        @Configuration
        @Log4j2
        @RequiredArgsConstructor
        public class CustomSecurityConfig {
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.info("필터 환경 설정");
                return http.build();
            }
        }
        ```
      - 설정 클래스에 메서드를 추가해서 정적 파일에서는 시큐리티가 동작하지 않도록 설정

        ```java
        @Configuration
        @Log4j2
        @RequiredArgsConstructor
        public class CustomSecurityConfig {
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.info("필터 환경 설정");
                return http.build();
            }

            @Bean
            public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
            }
        }
        ```

5. **Spring Security Customizing**

   1. Password Encoder

      함호화해서 복호화가 불가능한 암호화를 수행해주는 클래스

      복호화는 불가능하지만 비교는 가능

      Spring Boot에서는 내부적으로 BCryptPasswordEncoder를 사용

      - CustomSecurityConfig 클래스에 PasswordEncoder 빈을 생성해주는 코드를 작성
        ```java
        ...
        @Bean
            PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
            }
        ```
      - test

        ```java
        @SpringBootTest
        public class PasswordTests {
            @Autowired
            PasswordEncoder passwordEncoder;

            @Test
            public void testEncode() {
                String password = "1111";
                String enPw = passwordEncoder.encode(password);
                System.out.println("enPw: " + enPw);
                enPw = passwordEncoder.encode(password);
                System.out.println("enPw: " + enPw);
                // 해싱 기법을 달리해서 enPw가 다르게 나온다.
                // 그렇지만 matches로 비교하면 같은지 다른지 알 수 있다.

                // 비교하는 테스트를 원할 때 matches를 이용하면 된다.
                boolean result = passwordEncoder.matches(password, enPw);
                System.out.println("result:" + result);

            }
        }
        ```

   2. UserDetailsService

      - 일반적인 로그인 처리는 회원 아이디와 비밀번호를 가지고 데이터를 조회하고 올바른 데이터가 있으면 세션이나 쿠키로 처리하는 형식
      - Spring Security에서는 회원 정보를 User라고 하고 아이디 대신에 username이라는 용어를 사용
      - 아이디를 가지고 데이터를 먼저 조회하고(UserDetailsService가 수행) 그 후 비밀번호를 비교하는 형식으로 로그인을 수행하고 비밀번호가 틀리면 Bad Cridential 이라는 결과를 만들어 냄.
      - 로그인에 성공하면 자원에 접근할 수 있는 권한이 있는지 확인하고 권한이 없으면 Access Denied를 만들어 냄.
      - loadUserByUsername 라는 메서드만 소유
        username을 가지고 User 정보를 찾아오는 메서드로 리턴 타입은 UserDetails인데 이 클래스를 이용하면 권한(Authorities, Password, Username, 계정 만료 여부, 계정 잠김 여부)을 알아낼 수 있다.
      - 리턴 타입을 만드는 방법은 DTO 클래스에 UserDetails를 구현하는 방법이 있고 별도로 DTO 클래스를 생성하는 방법이 있다.
      - formLogin()
        인가나 인증 절차에서 문제가 발생했을 때 로그인 페이지를 보여주도록 해주는 메서드로 연달아서 loginPage(”/로그인 URL”)를 호출해서 로그인하는 URL을 설정할 수 있고 loginProcessingUrl(”/로그인을 처리할 URL”)을 호출해서 로그인 처리 URL을 설정할 수 있다.
      - CustomSecurityConfigure 클래스의 SecurityFilterChain 메서드에 formLogin을 호출하는 코드를 추가
        ```java
        @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.info("필터 환경 설정");
                // 인증이나 인가에 문제가 발생하면 로그인 폼 출력
                http.formLogin();
        				...
        ```
      - 로그인 관련 로직을 처리해주는 클래스를 생성하고 메서드를 오버라이딩(**사실상 구현임!! 인터페이스만 만들고 클래스에서 새롭게 생성하는건 사실상 implements해서 구현하는건데 클래스를 상속받던 인터페이스를 상속받던 자바에서는 오버라이딩한다.**) - security.CustomUserDetailService
        ```java
        @Log4j2
        @Service
        @RequiredArgsConstructor
        public class CustomUserDetailService implements UserDetailsService {
            // 아이디를 입력하고 로그인 요청을 하게 되면 아이디에 해당하는 데이터를 찾아오는 메서드
            // 로그인 처리를 해주어야 함.
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("loadUserByUsername: " + username);
                return null;
            }
        }
        ```
      - security.CustomUserDetailService를 수정해서 로그인 처리를 하도록 수정
        어노테이션을 지우고 직접 생성자 생성도 해준다.

        ```java
        @Log4j2
        @Service
        public class CustomUserDetailService implements UserDetailsService {
            private PasswordEncoder passwordEncoder;

            public CustomUserDetailService() {
                this.passwordEncoder = new BCryptPasswordEncoder();
            }
            // 아이디를 입력하고 로그인 요청을 하게 되면 아이디에 해당하는 데이터를 찾아오는 메서드
            // 로그인 처리를 해주어야 함.
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("loadUserByUsername: " + username);

                // 로그인에 성공한 경우 생성
                // 실제로는 데이터베이스에서 읽어서 설정
                // 일단 지금은 강제로 넣어서 로그인 실패는 없다.
                UserDetails userDetails = User.builder()
                        .username("user1")
                        .password(passwordEncoder.encode("1111"))
                        .authorities("ROLE_USER")
                        .build();
                return userDetails;
            }
        }
        ```

        어플리케이션을 실행하고 [localhost/login](http://localhost/login) 요청을 하고 아이디와 비번(1111)을 입력한 후 콘솔 창을 확인하는데 비번이 틀리면 자격 증명에 실패했다고 메시지를 출력

   3. 인가 설정

      - 어노테이션으로 권한을 설정하려면 설정 관련 클래스에 @EnableGlobalMethodSecurity를 추가하고 Controller에서 @PreAuthorize 어노테이션을 이용해서 설정
      - CustomSecurityConfig 클래스에 어노테이션을 추가
        ```java
        @EnableGlobalMethodSecurity(prePostEnabled = true)
        ```
      - SampleController 수정

        ```java
        @Controller
        @Log4j2
        public class SampleController {
        		...

            // 로그인한 유저만 접속이 가능
            @PreAuthorize("hasRole('USER')")
            @GetMapping("/sample/member")
            public void member() {
                log.info("멤버만 허용");
            }

            @PreAuthorize("hasRole('ADMIN')")
            @GetMapping("/sample/admin")
            public void admin() {
                log.info("관리자만 허용");
            }
        }
        ```

      - 실행을 한 후 쿠키를 삭제하고 /sample/all, /sample/member, /sample/admin 접속확인

   4. 인증 설정 방식

      - fromLogin
        인증이 필요한 경우 로그인 폼을 출력하도록 해주는 설정
      - loginPage
        로그인 페이지 URL 직접 설정
      - defaultSuccessUrl
        로그인 성공했을 때 리다이렉트할 URL을 설정(손을 잘 안댄다. 워낙 잘 만들어져 있음.)
      - usernameParameter
        username에 대한 파라미터 이름 설정
      - failureUrl
        로그인 실패했을 때 리다이렉트할 URL을 설정 - 기본은 login
      - 로그인 화면 직접 생성을 위해서 CustomSecurityConfig 클래스의 메서드를 수정
        ```java
        ...
        @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.info("필터 환경 설정");
                // 인증이나 인가에 문제가 발생하면 로그인 폼 출력
                http.formLogin().loginPage("/member/login");
                return http.build();
            }
        ```
      - 요청을 처리할 Controller를 생성하고 member/login을 처리하는 메서드를 생성 - controller.MemberController

        ```java
        @Controller
        @Log4j2
        @RequiredArgsConstructor
        @RequestMapping("/member")
        public class MemberController {
            @GetMapping("/login")
            // error는 로그인 실패했을 때의 파라미터
            // logout은 로그아웃한 후 로그인으로 이동했을 때의 파라미터
            public void login(String error, String logout) {
                if(error != null) {

                }
                if(logout != null) {

                }
            }
        }
        ```

      - templates 디렉토리에 member 디렉토리를 생성하고 login.html을 생성
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>Title</title>
          </head>
          <body>
            <form method="post">
              <div>
                <input type="text" name="username" value="admin" />
              </div>
              <div>
                <input type="password" name="password" value="admin" />
              </div>
              <div>
                <input type="submit" value="전송" />
              </div>
            </form>
          </body>
        </html>
        ```
      - 애플리케이션을 실행하고 login 페이지 출력

   5. CSRF(Cross Site Request Forgery - 크로스 사이트 요청 위조)
      - CSRF 공격
        사용자의 등급을 변경하는 URI를 알고 이 때 필요한 파라미터를 안다면 직접 로그인을 하지 않고 img 태그나 form 태그를 이용해서 URI와 파라미터를 기록해 둔 상태에서 관리자가 이 링크를 클릭하게 되면 공격자가 관리자 등급으로 변경되서 공격하는 기법
      - 방어 방법
        - referrer를 체크(어디에서 왔는가)
        - PUT이나 DELETE 같은 방식을 사용
        - CSRF 토큰을 활용
      - csrf 토큰 비활성화: http.csrf().disable()
   6. 현재 상태에서는 로그인이 수행이 안되기 때문에 CustomSecurityConfig 클래스의 config 메서드에 설정을 추가

      ```java
      public class CustomSecurityConfig {
          @Bean
          public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
              log.info("필터 환경 설정");
              // 인증이나 인가에 문제가 발생하면 로그인 폼 출력
              http.formLogin().loginPage("/member/login");
              http.csrf().disable();
              return http.build();
          }
      		...
      }
      ```

   7. logout
      - 기본적으로 제공되는 URL인 logout을 호출해도 되고 Spring Security는 쿠키와 세션을 이용해서 로그인을 관리하고 Tomcata은 JSESSIONID로 시작하는 쿠키를 생성하기 때문에 이 쿠키를 삭제해도 된다.
      - 로그 아웃이 되면 자동으로 login 요청을 수행하고 이 때 logout이라는 문자열 파라미터가 전달된다.
      - logout이 되서 login 요청으로 이동하는 것을 구분하기 위해서 MemberController의 login 요청을 수정
        ```java
        @Controller
        @Log4j2
        @RequiredArgsConstructor
        @RequestMapping("/member")
        public class MemberController {
            @GetMapping("/login")
            // error는 로그인 실패했을 때의 파라미터
            // logout은 로그아웃한 후 로그인으로 이동했을 때의 파라미터
            public void login(String error, String logout) {
                if (logout != null) {
                    log.info("로그아웃");
                }
            }
        }
        ```
      - login.html 파일에 출력을 추가
        ```html
        <th:block th:if="${param.logout != null}">
          <h2>로그아웃</h2>
        </th:block>
        ```
   8. 자동 로그인 - remeber me

      - 쿠키를 이용해서 브라우저에 로그인했던 정보를 유지하는 방식으로 구현
      - 이 기능을 사용하기 위해서는 현재 프로젝트가 사용하는 데이터베이스에 persistent_logins라는 테이블을 추가해야 함.
        ```sql
        create table persistent_logins(
        	username varchar(64) not null,
        	series varchar(64) primary key,
        	token varchar(64) not null,
        	last_used timestamp not null
        );
        ```
      - CustomSecurityConfig 클래스 수정

        ```java
        @Configuration
        @Log4j2
        @RequiredArgsConstructor
        @EnableGlobalMethodSecurity(prePostEnabled = true)
        public class CustomSecurityConfig {
            private final DataSource dataSource;
            private final CustomUserDetailService userDetailService;

            @Bean
            public PersistentTokenRepository persistentTokenRepository() {
                JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
                repo.setDataSource(dataSource);
                return repo;
            }

            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.info("필터 환경 설정");
                // 인증이나 인가에 문제가 발생하면 로그인 폼 출력
                http.formLogin().loginPage("/member/login");
                http.csrf().disable();

                http.rememberMe()
                        .key("12345678")
                        .tokenRepository(persistentTokenRepository())
                        .userDetailsService(userDetailService)
                        .tokenValiditySeconds(60 * 60 * 24 * 30);

                return http.build();
            }
        		...
        }
        ```

      - login.html 파일에서 remember-me를 전송할 수 있도록 체크박스를 추가
        ```html
        <div>
          <input type="checkbox" name="remember-me" />
          <label>자동 로그인</label>
        </div>
        ```

   9. 화면에서 인증 정보 사용과 컨트롤러에서 사용
      - 화면에는 authentication이라는 객체를 이용해서 전달된다.
      - Controller에서는 SecurityContextHolder.getContext().getAuthentication()이라는 메서드를 입력해서 Authentication 타입으로 사용할 수 있다.
      - member.html 파일에 로그인한 유저의 정보를 사용기 위한 스크립트 추가
        ```html
        ...
        <script>
          const auth = [[${#authentication.principal}]];
          console.log(auth);
        </script>
        ...
        ```
      - SampleController에서 member 요청을 처리하는 메서드 수정
        ```java
        @PreAuthorize("hasRole('USER')")
        @GetMapping("/sample/member")
            public void member() {
                log.info("멤버만 허용");
                // 로그인한 유저 정보 확인
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                log.info("로그인한 유저: ", authentication.getPrincipal());
            }
        ...
        ```
   10. 403 에러 처리

       - 403 에러
         서버에 클라이언트의 요청이 도달했는데 서버가 클라이언트의 접근을 거부할 때 반환하는 요청 코드
       - 403 에러 처리를 위한 핸들러 클래스 생성

         ```java
         @Log4j2
         public class Custom403Handler implements AccessDeniedHandler {

             @Override
             public void handle(HttpServletRequest request,
                                HttpServletResponse response,
                                AccessDeniedException accessDeniedException) throws IOException, ServletException {
                 log.info("---Access Denied---");
                 response.sendRedirect("/member/login?error=ACCESS_DENIED");
             }
         }
         ```

       - CustomSecurityConfig에서 메서드 수정

         ```java
         @Bean
             public AccessDeniedHandler accessDeniedHandler() {
                 return new Custom403Handler();
             }

         @Bean
             public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         				...
                 http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
         				...
             }
         ```

6. **Spring Security JPA**

   1. Entity 구성
      - 회원정보 - ClubMember
        - 아이디: mid
        - 비밀번호: mpw
        - 이메일: email
        - 이름: name
        - 소셜 가입 여부: social
        - 탈퇴 여부: del
        - 가입일
        - 수정일
      - 권한정보 - ClubMemberRole
        - USER
        - ADMIN
   2. Entity 생성

      - 공통 속성을 갖는 Entity - model.BaseEntity

        ```java
        @MappedSuperclass
        @EntityListeners(value = {AuditingEntityListener.class})
        public class BaseEntity {
            @CreatedDate
            @Column(name = "regdate", updatable = false)
            private LocalDateTime regDate;

            @LastModifiedDate
            @Column(name = "moddate")
            private LocalDateTime modDate;
        }
        ```

      - 권한을 나타내는 enum을 생성 - model.ClubMemberRole

        ```java
        package com.kakao.securityapp0119.model;

        public enum ClubMemberRole {
            USER, ADMIN
        }
        ```

      - 회원 정보를 나타내는 ClubMember Entity 생성

        ```java
        @Entity
        @Getter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @ToString(exclude = "roleSet")
        public class ClubMember extends BaseEntity {
            @Id
            private String mid;

            private String mpw;

            private String email;

            private String name;

            private boolean del;

            private boolean social;

            // 권한 - 여러 개의 권한을 소유
            @ElementCollection(fetch = FetchType.LAZY)
            @Builder.Default
            private Set<ClubMemberRole> roleSet = new HashSet<>();

            public void changePassword(String mpw) {
                this.mpw = mpw;
            }

            public void changeEmail(String email) {
                this.email = email;
            }

            public void changeDel(boolean del) {
                this.del = del;
            }

            // 권한 추가
            public void addRole(ClubMemberRole memberRole) {
                this.roleSet.add(memberRole);
            }

            // 권한 삭제
            public void clearRole() {
                this.roleSet.clear();
            }

            public void changeSocial(boolean social) {
                this.social = social;
            }
        }
        ```

      - 실행한 후 테이블 확인
        club_member 테이블과 club_member_role_set이 만들어진다.
        RoleSet이 Collection이므로 1:N 관계가 되어서 별도의 테이블로 생성된다.

   3. ClubMember Entity에 데이터베이스 작업을 위한 Repository 인터페이스를 생성하고 메서드를 선언

      ```java
      public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {
          // mid를 매개변수로 받아서
          // social의 값이 false인 데이터를 전부 찾아오는 메서드
          // SQL
          // select * from club_member m, club_member_role_set s
          // where m.mid = s.mid and m.mid=? and m.social=false

          @EntityGraph(attributePaths = "roleSet")
          @Query("select m from ClubMember m where m.mid = :mid and m.social = false")
          Optional<ClubMember> getWithRoles(String mid);

      }
      ```

      - Repository test

        ```java
        @SpringBootTest
        public class RepositoryTests {
            @Autowired
            private ClubMemberRepository clubMemberRepository;

            @Autowired
            private PasswordEncoder passwordEncoder;

            // 샘플 데이터 삽입
            @Test
            public void insertMembers() {
                IntStream.rangeClosed(1, 100).forEach(i -> {
                    ClubMember clubMember = ClubMember.builder()
                            .mid("member" + i)
                            .mpw(passwordEncoder.encode("1111"))
                            .email("user" + i + "@gmail.com")
                            .name("사용자" + i)
                            .social(false)
                            .roleSet(new HashSet<ClubMemberRole>())
                            .build();

                    clubMember.addRole(ClubMemberRole.USER);
                    if(i > 90) {
                        clubMember.addRole(ClubMemberRole.ADMIN);
                    }
                    clubMemberRepository.save(clubMember);
                });
            }

            // mid를 이용해서 조회하는 메서드
            @Test
            public void testRead() {
                Optional<ClubMember> result = clubMemberRepository.getWithRoles("member101");
                if (result.isPresent()) {
                    System.out.println(result);
                    System.out.println(result.get());
                } else {
                    System.out.println("존재하지 않는 아이디");
                }
            }
        }
        ```

   4. 소셜로 로그인한 경우 email을 가지고 로그인 여부를 판단하도록 하는 메서드 생성 및 확인
      - ClubMemberRepository 인터페이스에 메서드 선언
        ```java
        @EntityGraph(attributePaths = "roleSet"
                    , type = EntityGraph.EntityGraphType.LOAD)
            @Query("select m from ClubMember m where m.email = :email")
            Optional<ClubMember> findByEmail(@Param("email") String email);
        ```
      - test
        ```java
        @Test
            public void testReadEmail() {
                Optional<ClubMember> clubMember = clubMemberRepository.findByEmail("user95@email.com");
                System.out.println(clubMember.get().getRoleSet());
            }
        ```
   5. 로그인 처리 결과

      - Spring Security를 사용하는 로그인 처리에서는 로그인 처리 결과를 User 클래스를 상속받는 클래스에 저장
      - 로그인 처리 결과로 사용할 클래스를 생성 - security.dto.ClubMemberSecurityDTO

        ```java
        @Getter
        @Setter
        @ToString
        public class ClubMemberSecurityDTO extends User {
            private String mid;
            private String mpw;
            private String email;
            private String name;
            private boolean del;
            private boolean social;

            public ClubMemberSecurityDTO(String username, String password, String email, String name, boolean del, boolean social,
                                         Collection<? extends GrantedAuthority> authorities) {
                super(username, password, authorities);

                this.mid = username;
                this.mpw = password;
                this.email = email;
                this.name = name;
                this.del = del;
                this.social = social;
            }
        }
        ```

      - CustomUserDetailService 클래스를 수정

        ```java
        @Log4j2
        @Service
        @RequiredArgsConstructor
        public class CustomUserDetailService implements UserDetailsService {
            private final ClubMemberRepository clubMemberRepository;

            // 아이디를 입력하고 로그인 요청을 하게 되면 아이디에 해당하는 데이터를 찾아오는 메서드
            // 로그인 처리를 해주어야 함.
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                log.info("loadUserByUsername: " + username);

                Optional<ClubMember> result = clubMemberRepository.getWithRoles(username);

                if (result.isEmpty()) {
                    throw new UsernameNotFoundException("없는 사용자 이름");
                }
                // 존재하는 사용자 찾아오기
                ClubMember member = result.get();
                ClubMemberSecurityDTO clubMemberSecurityDTO = new ClubMemberSecurityDTO(
                        member.getMid(),
                        member.getMpw(),
                        member.getEmail(),
                        member.getName(),
                        member.isDel(),
                        false,
                        member.getRoleSet().stream().map(
                                memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toList()
                        ));

                return clubMemberSecurityDTO;
            }
        }
        ```

      - 실행한 후 데이터베이스로부터 로그인 확인
      - html 파일에서 sec로 시작하는 DTD를 이용하면 로그인 여부(isAuthenticated), 권한 소유 여부(hasRole(권한)), authentication 등을 이용해서 사용자 정보를 추출할 수 있다.)
      - member.html 파일을 수정해서 로그인 정보 출력
      - Controller에서 로그인 한 유저의 정보를 확인 - SampleController 클래스

   6. 회원 가입 처리

      - 회원 가입을 위한 DTO 클래스를 생성 - dto.ClubMemberJoinDTO

        ```java
        package com.kakao.securityapp0119.dto;

        import lombok.Data;

        @Data
        public class ClubMemberJoinDTO {
            private String mid;

            private String mpw;

            private String email;

            private boolean del;

            private boolean social;
        }
        ```

      - 회원 관련 처리를 위한 서비스 인터페이스를 생성하고 회원 가입 처리 메서드를 선언 - service.MemberService

        ```java
        public interface MemberService {
            // 회원이 존재하는 경우 발생시킬 예외 클래스
            static class MidExistException extends Exception{

            }
            void join(ClubMemberJoinDTO memberJoinDTO) throws MidExistException;
        }
        ```

      - 회원 관련 처리를 위한 서비스 클래스를 생성하고 회원 가입 처리 메서드를 구현 - service.MemberServiceImpl

        ```java
        @Log4j2
        @Service
        @RequiredArgsConstructor
        public class MemberServiceImpl implements MemberService {
            private final ClubMemberRepository clubMemberRepository;
            private final PasswordEncoder passwordEncoder;

            @Override
            public void join(ClubMemberJoinDTO memberJoinDTO) throws MidExistException {
                // 아이디 중복 확인
                String mid = memberJoinDTO.getMid();
                boolean exist = clubMemberRepository.existsById(mid);
                if (exist) {
                    throw new MidExistException();
                }

                // 회원 가입을 위해서 입력받은 정보를 가지고 ClubMember Entity를 생성
                ClubMember member = ClubMember.builder()
                        .mid(memberJoinDTO.getMid())
                        .mpw(memberJoinDTO.getMpw())
                        .email(memberJoinDTO.getEmail())
                        .name(memberJoinDTO.getName())
                        .del(memberJoinDTO.isDel())
                        .social(memberJoinDTO.isSocial())
                        .build();

                // 비밀번호 암호화
                member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));

                // 권한 설정
                member.addRole(ClubMemberRole.USER);
                log.info(member);
                log.info(member.getRoleSet());

                clubMemberRepository.save(member);
            }
        }
        ```

      - 회원 가입을 위한 요청 처리 코드를 MemberController 클래스에 추가

        ```java
        @Controller
        @Log4j2
        @RequiredArgsConstructor
        @RequestMapping("/member")
        public class MemberController {
        		...

            private final MemberService memberService;

            // 회원 가입 페이지로 이동
            @GetMapping("/join")
            public void join() {
                log.info("회원 가입 페이지로 이동");
            }

            // 회원 가입 처리
            @PostMapping("/join")
            public String join(ClubMemberJoinDTO memberJoinDTO, RedirectAttributes rattr) {
                log.info(memberJoinDTO);

                try {
                    memberService.join(memberJoinDTO);
                    // 성공
                } catch (Exception e) {
                    rattr.addFlashAttribute("error", "mid");
                    return "redirect:/member/join";
                }
                rattr.addFlashAttribute("result", "success");
                return "redirect:/member/login";
            }
        }
        ```

      - 회원 가입을 위한 html 파일을 생성하고 작성 - member.join.html

        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>회원가입</title>
          </head>
          <body>
            <div>
              <div>
                <h2>회원가입</h2>
              </div>
              <form id="registerForm" action="/member/join" method="post">
                <div>
                  <span>아이디</span>
                  <input type="text" name="mid" />
                </div>
                <div>
                  <span>비밀번호</span>
                  <input type="password" name="mpw" />
                </div>
                <div>
                  <span>이메일</span>
                  <input type="text" name="email" />
                </div>
                <div>
                  <span>이름</span>
                  <input type="text" name="name" />
                </div>
                <div>
                  <button type="submit">회원가입</button>
                  <button type="reset">폼 초기화</button>
                </div>
              </form>
            </div>
            <script th:inline="javascript">
              const error = [[${error}]]
              if (error) {
                  alert("중복된 아이디 입니다.")
              }

              const dto = [[${dto}]];
              if (dto) {
                  document.getElementById("mid").value = dto.mid;
              }
            </script>
          </body>
        </html>
        ```

7. OAuth

   1. 개요

      인증 서비스를 제공하는 업체들의 공통된 인증 방식(Open Authorization)

   2. Kakao Login

      - developers.kakao.com에 접속을 해서 애플리케이션을 등록하고 REST API 키를 복사
      - 플랫폼 등록 - Web - http://localhost
      - 왼쪽 화면에서 카카오 로그인을 클릭하고 카카오 로그인을 활성화
      - 하단으로 내려서 Redirect URL 설정 - [http://localhost/login/oauth2/code/kakao](http://localhost/login/oauth2/code/kakao)
      - 왼쪽 상단의 메뉴 중에서 동의항목을 클릭해서 수집하고자 하는 정보를 선택
      - 왼쪽에서 보안 탭을 클릭하고 Client Secret을 눌러서 코드를 발급 받아서 저장
      - build.gradle 파일의 Oauth2 라이브러리의 의존성이 설정되어있지 않으면 추가
        `implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'`
      - application.yml 파일에 설정 추가
        ```yaml
        spring:
          security:
            oauth2:
              client:
                registration:
                  kakao:
                    client-id: ***************************
                    client-secret: ***************************
                    redirect-uri: http://localhost/login/oauth2/code/kakao
                    authorization-grant-type: authorization_code
                    client-authentication-method: POST
                    client-name: Kakao
                    scope:
                      - profile_nickname
                      - account_email
                provider:
                  kakao:
                    authorization-uri: https://kauth.kakao.com/oauth/authorize
                    token-uri: https://kauth.kakao.com/oauth/token
                    user-info-uri: https://kapi.kakao.com/v2/user/me
                    user-name-attribute: id
        ```
      - CustomSecurityConfig 클래스의 filterChain 메서드에 OAuth2가 사용하는 로그인 추가
        ```java
        ...
        @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                log.info("필터 환경 설정");
                // 인증이나 인가에 문제가 발생하면 로그인 폼 출력
                http.formLogin().loginPage("/member/login");
                http.oauth2Login().loginPage("/member/login");
        				...
        ```
      - login.htm에 카카오 로그인 링크 추가
      - 여기까지 작업을 하고 애플리케이션을 실행하면 카카오 로그인까지는 수행됨.
      - OAuth2 로그인 방식으로 로그인된 경우 처리할 서비스 클래스를 추가하고 작성 - security.CustomOAuth2UserService

        ```java
        @Log4j2
        @Service
        @RequiredArgsConstructor
        public class CustomOAuth2UserService extends DefaultOAuth2UserService {
            // 카카오 로그인 성공 후 넘어오는 데이터를 이용해서 email을 추출해서 리턴하는 메서드
            private String getKakaoEmail(Map<String, Object> paramMap) {
                // 카카오 계정 정보가 있는 Map을 추출
                Object value = paramMap.get("kakao_account");
                LinkedHashMap accountMap = (LinkedHashMap) value;
                String email = (String) accountMap.get("email");
                log.info("카카오 계정 이메일: " + email);
                return email;
            }

            // 로그인 성공했을 때 호출되는 메서드
            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                // 로그인에 성공한 서비스의 정보 가져오기
                ClientRegistration clientRegistration = userRequest.getClientRegistration();
                String clientName = clientRegistration.getClientName();
                log.info("Service Name: " + clientName);

                // 계정에 대한 정보 가져오기
                OAuth2User oAuth2User = super.loadUser(userRequest);
                Map<String, Object> paramMap = oAuth2User.getAttributes();

                String email = null;
                switch (clientName.toLowerCase()) {
                    case "kakao":
                        email = getKakaoEmail(paramMap);
                        break;
                }
                log.info("email: " + email);
                return oAuth2User;
            }
        }
        ```

      - 애플리케이션을 실행하고 카카오 로그인을 수행한 후 콘솔 확인

   3. OAuth2를 사용했을 때 문제점
      - 매번 새로운 유저로 판단
        email 같은 정보를 데이터베이스 테이블에 저장해서 이전에 로그인 한 적이 있는 유저인지 판단.
      - 여러가지 로그인 방법을 제시하는 경우 서로 다른 유저로 판단하는 문제
        재가입을 시키는 형태로 해결할 수 있는데 이 떄 이메일을 전부 등록을 해서 소셜로 로그인을 했을 때 아이디를 찾아주는 방식
   4. 카카오로 로그인 성공했을 때 데이터베이스에 등록하기

      - Spring Security가 사용하고 있는 ClubMemberSecurityDTO 클래스를 수정

        ```java
        @Getter
        @Setter
        @ToString
        public class ClubMemberSecurityDTO extends User implements OAuth2User {
        		...
            // 소셜 로그인을 위한 코드
            private Map<String, Object> props;

            @Override
            public Map<String, Object> getAttributes() {
                return this.getProps();
            }

            @Override
            public String getName() {
                return this.mid;
            }
        }
        ```

      - 소셜 로그인을 수행한 후 이메일을 가진 사용자를 찾아보고 없는 경우에는 회원가입을 하고 있는 경우에는 그 정도를 리턴하도록 CustomOAuth2UserService 클래스 수정

        ```java
        @Log4j2
        @Service
        @RequiredArgsConstructor
        public class CustomOAuth2UserService extends DefaultOAuth2UserService {
            private final ClubMemberRepository memberRepository;
            private final PasswordEncoder passwordEncoder;

            // 카카오 로그인 성공 후 넘어오는 데이터를 이용해서 email을 추출해서 리턴하는 메서드
            private String getKakaoEmail(Map<String, Object> paramMap) {
                // 카카오 계정 정보가 있는 Map을 추출
                Object value = paramMap.get("kakao_account");
                LinkedHashMap accountMap = (LinkedHashMap) value;
                String email = (String) accountMap.get("email");
                log.info("카카오 계정 이메일: " + email);
                return email;
            }

            // email 정보가 있으면 그에 해당하는 DTO를 리턴하고 없으면
            // 회원가입하고 리턴하는 사요자 정의 메서드
            private ClubMemberSecurityDTO generateDTO(String email, Map<String, Object> params) {
                // email을 가지고 데이터 찾아오기
                Optional<ClubMember> result = memberRepository.findByEmail(email);
                if (result.isEmpty()) {
                    // email이 존재하지 않는 경우로 회원 가입
                    ClubMember member = ClubMember.builder()
                            .mid(email)
                            .mpw(passwordEncoder.encode("1111"))
                            .email(email)
                            .social(true)
                            .build();
                    member.addRole(ClubMemberRole.USER);
                    memberRepository.save(member);

                    ClubMemberSecurityDTO memberSecurityDTO = new ClubMemberSecurityDTO(
                            email,
                            "1111",
                            email,
                            null,
                            false,
                            true,
                            Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    memberSecurityDTO.setProps(params);
                    return memberSecurityDTO;
                } else {
                    // email이 존재하는 경우
                    ClubMember member = result.get();
                    ClubMemberSecurityDTO memberSecurityDTO = new ClubMemberSecurityDTO(
                            member.getMid(),
                            member.getMpw(),
                            member.getEmail(),
                            member.getName(),
                            member.isDel(),
                            member.isSocial(),
                            member.getRoleSet().stream()
                                    .map(memberRole ->
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + memberRole.name()))
                                    .collect(Collectors.toList()));
                    return memberSecurityDTO;
                }
            }

            // 로그인 성공했을 때 호출되는 메서드
            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                // 로그인에 성공한 서비스의 정보 가져오기
                ClientRegistration clientRegistration = userRequest.getClientRegistration();
                String clientName = clientRegistration.getClientName();
                log.info("Service Name: " + clientName);

                // 계정에 대한 정보 가져오기
                OAuth2User oAuth2User = super.loadUser(userRequest);
                Map<String, Object> paramMap = oAuth2User.getAttributes();

                String email = null;
                switch (clientName.toLowerCase()) {
                    case "kakao":
                        email = getKakaoEmail(paramMap);
                        break;
                }
                log.info("email: " + email);

                return generateDTO(email, paramMap);
            }
        }
        ```

      - 실행한 후 소셜로 로그인했을 때 회원 가입이 되어 있는지 확인
