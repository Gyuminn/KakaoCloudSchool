# [Spring Boot] Server Communications

1. **Micro Service Architecture**
   - 애플리케이션이 가지고 있는 기능이 하나의 비즈니스 범위만 가지는 형태
   - MSA 환경에서는 기능에 따라 분리된 여러 컴포넌트가 존재하게 되고 컴포넌트 성격에 따라 적합한 데이터 저장소를 선택하게 된다. 데이터 저장소가 물리적 또는 논리적으로 분리되어서 서비스가 복잡해지면 분산 저장된 데이터를 통합해야 하는 상황이 발생
   - 모놀리식은 하나의 데이터 저장소를 사용하기 때문에 Join이나 클래스 안에 의존성을 설정해서 해결을 하지만 MSA 아키텍처에서는 데이터 저장소가 나누어져 있을 수 있고 Foreign Key를 설정하지 않으므로 모놀리식과 같은 방법을 사용할 수 없음.
   - MSA에서 데이터를 통합하기 위해서 사용하는 방식은 `Remote API Call(RPC - Remote Procedure Call)` → ESB라고 한다.
   - REST-API, gRPC, XML-RPC, SOAP 같은 프로토콜을 사용할 수 있는데 예전에는 SOAP나 XML-RPC를 많이 사용했고 최근에는 REST-API를 많이 사용함.
2. **방법**
   1. Java API를 이용하는 방법 - 네트워크에서 데이터를 읽어오는 API
      - URLConnection
      - HttpClient - JDK 11 버전에서 추가
   2. Spring API를 이용하는 방법 - API를 호출해서 받은 데이터를 다시 API로 제공하기 위해서 등장
      - RestTemplate
        동기식 처리 - 작업이 시작되면 작업이 종료될 때 까지 다른 작업은 중지
        프로그래밍이 편하지만 성능은 떨어질 가능성이 높음
        `bean을 하나만 생성해도 된다.`
      - WebClient
        비동기식 처리 - 작업이 진행 중이라도 다른 작업이 수행될 수 있다.
        프로그래밍은 어렵지만 성능은 우수해질 가능성이 높음
        너무 많은 작업을 동시에 수행하면 성능이 저하(`Context Switching - 문맥 교환` , 현재 작업의 정보를 저장하고 다른 작업의 정보를 가져오는 것)
        Spring에서 비동기적(`rx` 라고 붙여서 말하기도 함)으로 동작하는 애플리케이션을 만들 때는 `Web Flux` 라는 라이브러리의 의존성을 사용하는데 이러한 프로그래밍 방식을 reactive programming이라고 한다.
        최근에는 라이브러리 이름에 reactive를 붙여서 붙여서 만든다.
      - HttpInterface - Spring 6에서 추가
        동기식과 비동기식 모두 가능한데 현재는 RestTemplate이나 WebClient를 생성한 후 대입하는 형태로 사용
3. **HttpURLConnection을 이용한 Download**

   1. Controller 클래스에 작성 - Kakao Open API 데이터 가져오기

      파라미터는 UTF-8 인코딩 되어야 한다.

      URLEncoder.encode(”인천”)

      카카오 오픈 API는 Authorization 이라는 헤더에 인증키를 삽입해야 한다.

      setRequestProperty(”Authorization”, “인증키”);

      `URL: https://dapi.kakao.com/vc/search/book?query=책제목`

   2. Java에서 JSON Parsing을 위한 의존성을 추가

4. **Server Communication API**

   MicroS Service 형태로 개발을 하다보면 하나 또는 여러 개의 저장소를 공유하게 되고 이 떄 여러 개의 저장소를 사용하게 되면 Join이나 Link 형태로 데이터를 사용하는게 어려워지기 때문에 이런 경우는 RPC(Remote Procedure Call)을 이용해서 데이터를 공유

   다른 서버의 API를 호출해서 사용: Domain 설계가 어려워짐

   Domain에서 직접 참조할 수 있는 키나 데이터를 생성해야 하기 때문

   1. 프로젝트 생성 - apiserver0320

      의존성은 DB 연동 안할거라 DevTools, lombok, Web 만 설정

   2. yml 파일은 포트만 설정

      ```yaml
      server:
        port: 9000
      ```

   3. DTO 생성 - dto.MemberDTO

      ```java
      package com.gyumin.apiserver0320.dto;

      import lombok.Getter;
      import lombok.Setter;
      import lombok.ToString;

      @Getter
      @Setter
      @ToString
      public class MemberDTO {
          private String name;
          private String email;
          private String organization;
      }
      ```

   4. Controller 생성 - controller.CrudController

      ```java
      package com.gyumin.apiserver0320.controller;

      import com.gyumin.apiserver0320.dto.MemberDTO;
      import org.springframework.http.HttpStatus;
      import org.springframework.http.ResponseEntity;
      import org.springframework.web.bind.annotation.*;

      @RestController
      @RequestMapping("/api/v1/crud-api")
      public class CrudController {
          @GetMapping
          public String getName() {
              return "gyumin";
          }

          @GetMapping(value = "/{variable}")
          public String getVariable(@PathVariable String variable) {
              return variable;
          }

          // @RequestParam이 없어도 파라미터를 변수로 받을 수 있다.
          // 변수가 여러 개이면 @RequestParam이 없으면 어떤 변수에 어떤 파라미터가 대입될 지 알 수 없다.
          // @RequestParam(파라미터 이름) 변수를 작성하면 파라미터 이름과 일치하는 파라미터를 찾아서 변수에 대입
          // 지금은 하나니까 안써줌
          // DTO(Command Instance)로 여러 개를 한꺼번에 받을 수 있는데 이 경우도 파라미터 이름과 일치하는 속성을 찾아서 대입을 시켜주는데
          // 예전에는 이 방법을 잘 사용안함. 가독성이 떨어지기 때문
          // 그래서 최근에는 Command Instance와 @RequestParam을 같이 설정 (@RequestParam String name, MmberDTO dto) 처럼 쓴다.
          @GetMapping(value = "/param")
          public String getParameter(@RequestParam String name) {
              return "Hello" + name + "!!!";
          }

          // JSON 응답을 하고자 하는 경우에는
          // DTO나 MAP 또는 Collection을 리턴하면 되는데 이 경우에는 데이터만 전송이 된다.
          // 하지만 ResponseEntity를 이용하게 되면 상태를 같이 전송할 수 있다.
          // 모든 프로젝트를 혼자 개발하는 경우가 아니라면 Collection을 리턴하는 것은 지양해야 한다.
          // 최근에는 Map 보다는 DTO를 권장한다.
          // @RequestBody는 클라이언트가 데이터를 body에 포함시켜 전송한 경우 받는 방법이다.
          @PostMapping
          public ResponseEntity<MemberDTO> getMember(
                  @RequestBody MemberDTO request
          ) {
              System.out.println(request);
              return ResponseEntity.status(HttpStatus.OK).body(request);
          }

          // 헤더 받기
          @PostMapping(value = "/add-header")
          public ResponseEntity<MemberDTO> addHeader(
                  @RequestHeader("my-header") String header,
                  @RequestBody MemberDTO memberDTO
          ) {
              System.out.println(header);
              return ResponseEntity.status(HttpStatus.OK).body(memberDTO);
          }
      }
      ```

5. **RestTemplate**

   1. 개요
      - org.springframework.http.client 패키지에 존재하는 REST-API 호출을 위한 메서드를 제공하는 클래스
      - HttpClient를 이용하는 것은 Java에서 제공하는 범용 라이브러리고 RestTemplate 클래스는 HttpClient를 추상화해서 제공
      - 다른 서버의 REST-API를 호출할 수 있도록 URI를 설정하고 GET이나 POST같은 HTTP 요청 메서드를 사용할 수 있고 요청 메시지의 헤더와 바디를 구성할 수 있다. 그리고 응답 메시지의 헤더, 상태코드, 바디를 조회할 수 있는 메서드를 제공
      - JSON이나 XML 변호나 Converter를 제공
      - 인터셉트 기능을 제공 - 중간에 멈추고 다른 작업을 수행할 수 있다.
      - 멀티 스레드에 안전(작업을 할 떄 다른 작업이 수행 중이면 자신은 잠시 대기) - 스프링 빈으로 객체를 생성하고 필요한 곳에 주입해서 사용
   2. APIClient 프로젝트 생성 - apiclient0320
      - 의존성은 DevTools, Lombok, Web, Reactive Web
   3. 서버의 응답과 요청에 사용할 dto 클래스 생성 - dto.MemberDTO - apiserver0320에서 만든 DTO와 똑같을 가능성이 높음

      ```java
      package com.gyumin.apiserver0320.dto;

      import lombok.Getter;
      import lombok.Setter;
      import lombok.ToString;

      @Getter
      @Setter
      @ToString
      public class MemberDTO {
          private String name;
          private String email;
          private String organization;
      }
      ```

   4. RestTemplate 클래스를 작성하고 메서드 구현 - service.RestTemplateService

      ```java
      package com.gyumin.apiclient0320.service;

      import com.gyumin.apiclient0320.dto.MemberDTO;
      import org.springframework.http.RequestEntity;
      import org.springframework.http.ResponseEntity;
      import org.springframework.stereotype.Service;
      import org.springframework.web.client.RestTemplate;
      import org.springframework.web.util.UriComponentsBuilder;

      import java.net.URI;

      @Service
      public class RestTemplateService {
          // 파라미터가 없는 GET 방식 요청 처리 - 응답 타입은 String
          public String getName() {
              // 요청 URI 생성
              URI uri = UriComponentsBuilder
                      .fromUriString("http://localhost:9000")
                      .path("/api/v1/crud-api")
                      .encode()
                      .build()
                      .toUri();

              // 요청 객체 생성
              RestTemplate restTemplate = new RestTemplate();
              // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
              ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

              // 내용 리턴
              return responseEntity.getBody();
          }

          // PathVariable이 있는 경우
          // 값을 넣을 떄 expand
          public String getNameWithPathVariable() {
              URI uri = UriComponentsBuilder
                      .fromUriString("https://localhost:9000")
                      .path("/api/v1/crud-api/{name}")
                      .encode()
                      .build()
                      .expand("규민")
                      .toUri();
              // PathVariable이 여러 개라면 expand에 나열하면 된다.

              // 요청 객체 생성
              RestTemplate restTemplate = new RestTemplate();
              // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
              ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

              // 내용 리턴
              return responseEntity.getBody();
          }

          // 파라미터가 있는 경우
          public String getNameWithParameter() {
              URI uri = UriComponentsBuilder
                      .fromUriString("https://localhost:9000")
                      .path("/api/v1/crud-api/param")
                      .queryParam("name", "itstudy")
                      .encode()
                      .build()
                      .toUri();
              // 파라미터가 여러 개라면 queryParam을 연속해서 호출하거나
              // 다른 모양의 queryParam을 이용
              // 요청 객체 생성

              // 요청 객체 생성
              RestTemplate restTemplate = new RestTemplate();
              // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
              ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

              // 내용 리턴
              return responseEntity.getBody();
          }

          // 파라미터와 body가 존재하는 POST 요청
          public ResponseEntity<MemberDTO> postWithParamAndBody() {
              URI uri = UriComponentsBuilder
                      .fromUriString("http://localhost:9000")
                      .path("/api/v1/crud-api")
                      .queryParam("name", "규민")
                      .queryParam("email", "abc@kakao.com")
                      .queryParam("organization", "kakao")
                      .encode()
                      .build()
                      .toUri();
              // queryParam은 Body에 포함되지 않기 때문에 파라미터로 읽어야 한다.

              // Body에 저장해서 보내기
              // Body 객체 생성
              MemberDTO memberDTO = new MemberDTO();
              memberDTO.setName("gyumin");
              memberDTO.setEmail("abc@kakao.com");
              memberDTO.setOrganization("카카오");
              RestTemplate restTemplate = new RestTemplate();

              // POST 방식으로 전송할 때 2번쨰 매개변수로 Body를 설정
              ResponseEntity<MemberDTO> responseEntity = restTemplate.postForEntity(uri, memberDTO, MemberDTO.class);
              return responseEntity;
          }

          // body와 header를 모두 전송하는 POST 요청
          public ResponseEntity<MemberDTO> postWithHeader() {
              URI uri = UriComponentsBuilder
                      .fromUriString("http://localhost:9000")
                      .path("/api/v1/crud-api")
                      .encode()
                      .build()
                      .toUri();
              // queryParam은 Body에 포함되지 않기 때문에 파라미터로 읽어야 한다.

              // Body에 저장해서 보내기
              // Body 객체 생성
              MemberDTO memberDTO = new MemberDTO();
              memberDTO.setName("gyumin");
              memberDTO.setEmail("abc@kakao.com");
              memberDTO.setOrganization("카카오");

              // 헤더를 같이 보내기 위한 객체를 생성
              RequestEntity<MemberDTO> requestEntity = RequestEntity
                      .post(uri)
                      .header("my-header", "gyumin-API")
                      .body(memberDTO);

              RestTemplate restTemplate = new RestTemplate();

              // POST 방식으로 전송할 때 요청 객체를 전송
              ResponseEntity<MemberDTO> responseEntity = restTemplate.exchange(requestEntity, MemberDTO.class);
              return responseEntity;
          }
      }
      ```

   5. Controller 클래스를 만들어서 확인 - controller.RestTemplateController

      ```java
      package com.gyumin.apiclient0320.controller;

      import com.gyumin.apiclient0320.dto.MemberDTO;
      import com.gyumin.apiclient0320.service.RestTemplateService;
      import lombok.RequiredArgsConstructor;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.http.ResponseEntity;
      import org.springframework.web.bind.annotation.GetMapping;
      import org.springframework.web.bind.annotation.PostMapping;
      import org.springframework.web.bind.annotation.RequestMapping;
      import org.springframework.web.bind.annotation.RestController;

      @RestController
      @RequiredArgsConstructor
      @RequestMapping("/rest-template")
      public class RestTemplateController {
          private final RestTemplateService restTemplateService;

          // @Autowired - setter를 이용한 주입
          // @Autowired는 아래 메서드를 자동 생성
          /*
          속성을 생성하기 위한 생성자 - 최근에는 RequiredArgsContructor로 대체
          public RestTemplateController(RestTemplateService restTemplateService) {
              this.restTemplateService = restTemplateService;
          }
           */

          @GetMapping
          public String getName() {
              System.out.println("아무거나");
              return restTemplateService.getName();
          }

          @GetMapping("/path-variable")
          public String getNameWithPathVariable() {
              return restTemplateService.getNameWithPathVariable();
          }

          @GetMapping("/parameter")
          public String getNameWithParameter() {
              return restTemplateService.getNameWithParameter();
          }

          @PostMapping
          public ResponseEntity<MemberDTO> postDTO() {
              return restTemplateService.postWithParamAndBody();
          }

          @PostMapping("/header")
          public ResponseEntity<MemberDTO> postWithHeader() {
              return restTemplateService.postWithHeader();
          }
      }
      ```

6. **위 방식의 문제점**
   - 동기식 처리는 하나의 작업이 종료될 때까지 다른 작업이 대기하기 때문에 서버에 문제가 발생해서 응답이 지연되거나 없다면 현재 프로젝트는 무한 대기에 빠질 수 있다.
   - 지금은 지역 변수로 객체를 생성해서 사용하기 때문에 한꺼번에 요청이 몰리면 너무 많은 객체를 생성해서 메모리에 부담을 줄 수 있다.
   - **위와 같은 무제를 해결하기 위해서는 별도의 Bean을 생성해서 처리하도록 해주고 대기 시간과 읽는 시간의 최대값을 설정해주면 된다.**
   - **스레드 Pool을 이용해서 미리 스레드를 만들어두고 사용하는 방법을 이용하면 훨씬 효율이 좋아진다.**
     스레드 풀은 미리 스레드를 생성해두고 필요할 때 풀에서 빌려쓰고 반납하는 방식이다.
     데이터베이스 커넥션은 스레드 풀을 이용하는 방식이다.
     스프링이 애플리케이션이 구동될 때 데이터베이스 커넥션을 미리 만들어두고 개발자의 코드는 스레드를 빌려서 사용하고 반납하는 방식을 이용한다.
7. **따라서 Interceptor와 Bean을 이용한 RestTemplate을 사용**

   Srping에서 Interceptor는 Controller가 요청을 처리하기 전이나 처리한 후에 동작하는 인터페이스로 필터의 기능을 수행하는 것인데 Interceptor는 Controller에만 반응하기 때문에 웹 프로젝트에 한해서만 사용이 가능하다.

   **AOP는 메서드에 반응하는 것이기 때문에 어떤 프로젝트에서도 사용이 가능하다.**

   **Filter는 JavaEE의 Spec이라서 Spring Bean에 접근을 못함.**

   1. 인터셉터 클래스 생성 - interceptor.IdentityHeaderInterceptor

      ```java
      package com.gyumin.apiclient0320.interceptor;

      import org.springframework.http.HttpRequest;
      import org.springframework.http.client.ClientHttpRequestExecution;
      import org.springframework.http.client.ClientHttpRequestInterceptor;
      import org.springframework.http.client.ClientHttpResponse;

      import java.io.IOException;

      public class IdentityHeaderInterceptor implements ClientHttpRequestInterceptor {
          @Override
          public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution excution)
              throws IOException {
              // 헤더가 존재하면 그대로 두고 없으면 추가
              request.getHeaders().addIfAbsent("X-COMPONENT-ID", "GYUMIN-API");
              return excution.execute(request, body);
          }
      }
      ```

   2. RestTemplate의 Config 클래스 생성 - config.RestTemplateConfig

      ```java
      package com.gyumin.apiclient0320.config;

      import com.gyumin.apiclient0320.interceptor.IdentityHeaderInterceptor;
      import org.springframework.context.annotation.Bean;
      import org.springframework.context.annotation.Configuration;
      import org.springframework.http.client.ClientHttpRequestFactory;
      import org.springframework.http.client.SimpleClientHttpRequestFactory;
      import org.springframework.web.client.DefaultResponseErrorHandler;
      import org.springframework.web.client.RestTemplate;

      @Configuration
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
      ```

   3. 그냥 실행시키면 안되고 코드를 수정해야 함. 지역변수 없애기 - RestTemplateService

      **@RequierdArgsConstructor 추가하고 private final RestTemplate을 추가하고 지역변수 모두 삭제시켜줌!**

      ```java
      package com.gyumin.apiclient0320.service;

      import com.gyumin.apiclient0320.dto.MemberDTO;
      import lombok.RequiredArgsConstructor;
      import org.springframework.http.RequestEntity;
      import org.springframework.http.ResponseEntity;
      import org.springframework.stereotype.Service;
      import org.springframework.web.client.RestTemplate;
      import org.springframework.web.util.UriComponentsBuilder;

      import java.net.URI;

      @Service
      @RequiredArgsConstructor
      public class RestTemplateService {
          private final RestTemplate restTemplate;

          // 파라미터가 없는 GET 방식 요청 처리 - 응답 타입은 String
          public String getName() {
              // 요청 URI 생성
              URI uri = UriComponentsBuilder
                      .fromUriString("http://localhost:9000")
                      .path("/api/v1/crud-api")
                      .encode()
                      .build()
                      .toUri();

              // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
              ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

              // 내용 리턴
              return responseEntity.getBody();
          }

          // PathVariable이 있는 경우
          // 값을 넣을 떄 expand
          public String getNameWithPathVariable() {
              URI uri = UriComponentsBuilder
                      .fromUriString("https://localhost:9000")
                      .path("/api/v1/crud-api/{name}")
                      .encode()
                      .build()
                      .expand("규민")
                      .toUri();
              // PathVariable이 여러 개라면 expand에 나열하면 된다.

              // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
              ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

              // 내용 리턴
              return responseEntity.getBody();
          }

          // 파라미터가 있는 경우
          public String getNameWithParameter() {
              URI uri = UriComponentsBuilder
                      .fromUriString("https://localhost:9000")
                      .path("/api/v1/crud-api/param")
                      .queryParam("name", "itstudy")
                      .encode()
                      .build()
                      .toUri();
              // 파라미터가 여러 개라면 queryParam을 연속해서 호출하거나
              // 다른 모양의 queryParam을 이용
              // 요청 객체 생성

              // 요청을 전송하고 응답 객체의 본문을 String으로 변환해서 받기
              ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

              // 내용 리턴
              return responseEntity.getBody();
          }

          // 파라미터와 body가 존재하는 POST 요청
          public ResponseEntity<MemberDTO> postWithParamAndBody() {
              URI uri = UriComponentsBuilder
                      .fromUriString("http://localhost:9000")
                      .path("/api/v1/crud-api")
                      .queryParam("name", "규민")
                      .queryParam("email", "abc@kakao.com")
                      .queryParam("organization", "kakao")
                      .encode()
                      .build()
                      .toUri();
              // queryParam은 Body에 포함되지 않기 때문에 파라미터로 읽어야 한다.

              // Body에 저장해서 보내기
              // Body 객체 생성
              MemberDTO memberDTO = new MemberDTO();
              memberDTO.setName("gyumin");
              memberDTO.setEmail("abc@kakao.com");
              memberDTO.setOrganization("카카오");

              // POST 방식으로 전송할 때 2번쨰 매개변수로 Body를 설정
              ResponseEntity<MemberDTO> responseEntity = restTemplate.postForEntity(uri, memberDTO, MemberDTO.class);
              return responseEntity;
          }

          // body와 header를 모두 전송하는 POST 요청
          public ResponseEntity<MemberDTO> postWithHeader() {
              URI uri = UriComponentsBuilder
                      .fromUriString("http://localhost:9000")
                      .path("/api/v1/crud-api")
                      .encode()
                      .build()
                      .toUri();
              // queryParam은 Body에 포함되지 않기 때문에 파라미터로 읽어야 한다.

              // Body에 저장해서 보내기
              // Body 객체 생성
              MemberDTO memberDTO = new MemberDTO();
              memberDTO.setName("gyumin");
              memberDTO.setEmail("abc@kakao.com");
              memberDTO.setOrganization("카카오");

              // 헤더를 같이 보내기 위한 객체를 생성
              RequestEntity<MemberDTO> requestEntity = RequestEntity
                      .post(uri)
                      .header("my-header", "gyumin-API")
                      .body(memberDTO);

              // POST 방식으로 전송할 때 요청 객체를 전송
              ResponseEntity<MemberDTO> responseEntity = restTemplate.exchange(requestEntity, MemberDTO.class);
              return responseEntity;
          }
      }
      ```

8. **Thread Pool을 사용하는 RestTemplate 클래스의 bean 생성**

   1. 의존성 추가

      ```java
      implementation 'org.apache.httpcomponents.client5:httpclient5:5.1.3'
      ```

   2. RestTemplate 빈 생성 클래스 추가 - config.PoolingRestTemplateConfig

      ```java
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
      ```

   3. **이전에 만든 RestTemplate 설정 파일은 동작하지 않도록 설정**

      @Configuration만 주석처리하면 된다.

      ```java
      // PoolingRestTemplateConfig에서도 RestTemplate을 만들어줬으므로 2개가 된다. 그래서 Configuration을 주석처리
      // @Configuration
      public class RestTemplateConfig {
          // 팩토리 클래스의 bean 설정
          @Bean
      		...
      ```

9. **HttpClient**

   1. 개요
      - Spring WebFlux에서는 Http 요청을 수행하는 클라이언트로 WebClient를 제공
      - 스레드 풀 같은걸 설정하지 않아도 됨.
      - Reactor 기반으로 동작하는 API - Netty Client의 Worker Thread로 동작
      - 스레드와 동시성 문제를 벗어나 비동기 형식으로 사용 가능
   2. 특징
      - Non-Blocking IO를 지원: 하나의 작업이 종료되기 전에 다른 작업을 수행할 수 있는 방식
      - Reactive Stream의 **Back Pressure**(생산자와 소비자가 불균형일 때 일어나는 현상)를 지원
      - 적은 하드웨어 리소스로 동시성을 지원
      - 함수형 API를 지원
      - 동기와 비동기 상호 작용을 지원
      - 스트리밍을 지원
      - 사용하기 위해서는 reactive-web 의존성이 설정되어야 함.
      - **빈을 반들거나 스레드 풀을 설정하지 않아도 됨!**
   3. WebClientService 생성

      ```java
      package com.gyumin.apiclient0320.service;

      import org.springframework.http.HttpHeaders;
      import org.springframework.http.HttpStatus;
      import org.springframework.http.MediaType;
      import org.springframework.http.ResponseEntity;
      import org.springframework.stereotype.Service;
      import org.springframework.web.reactive.function.client.WebClient;
      import reactor.core.publisher.Mono;

      @Service
      public class WebClientService {
          // 매개변수 없고 String을 리턴
          public String getName() {
              WebClient webClient = WebClient.builder()
                      .baseUrl("http://localhost:9000")
                      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                      .build();

              // 동기식 동작
              return webClient.get()
                      .uri("/api/v1/crud-api")
                      .retrieve()
                      .bodyToMono(String.class)
                      .block();
          }

          // PathVariable이 있는 경우
          public String getNameWithPathVariable() {
              WebClient webClient = WebClient.create("http://localhost:9000");

              // 빌더를 안쓰고 create 로 만들어도 됨.
              ResponseEntity<String> responseEntity = webClient.get()
                      .uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api/{name}")
                              .build("gyumin"))
                      .retrieve()
                      .toEntity(String.class)
                      .block();

              // 이건 또 다른 방법!!!!!!!!!
              ResponseEntity<String> responseEntity2 = webClient.get()
                      .uri("/api/v1/crud-api/{name}", "gyumin")
                      .retrieve().toEntity(String.class).block();

              return responseEntity.getBody();
          }

          public String getNameWithParameter() {
              WebClient webClient = WebClient.create("http://localhost:9000");
              return webClient.get().uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api")
                              .queryParam("name", "gyumin")
                              .build())
                      .exchangeToMono(clientResponse -> {
                          if (clientResponse.statusCode().equals(HttpStatus.OK)) {
                              return clientResponse.bodyToMono(String.class);
                          } else {
                              return clientResponse.createException().flatMap(Mono::error);
                          }
                      })
                      .block();
          }
      }
      ```

      **여기서 아직 POST를 안만들어줬지만 있다고 치고(혹은 RestTemplateService에 있는 POST 처리를 주석처리하거나) 기존에 RestTemplate을 WebClient로 바꿔준다면 잘 실행된다!**

10. **HttpInterface**

    1. 개요
       - Spring 6.0에서 추가된 Server Communication API
       - @HttpExchange라는 어노테이션을 가진 메서드를 소유한 자바 인터페이스를 Http 서비스로 만들어주는 API
       - 인터페이스를 구현하는 프록시를 통해서 HTTP 요청을 수행
         인터페이스나 클래스를 작성하면 프레임워크가 우리가 작성한 코드를 기반으로 별도의 인스턴스를 만들어서 서비스를 제공하는 패턴
       - 요청을 처리할 메서드와 URL을 설정하고 다운로드 받을 구현체를 대입하면 프록시 패턴으로 메서드를 완성해서 사용할 수 있도록 해주는 API
       - 기존 apiclint0320 프로젝트에서 apiserver0320으로 호출하는 기본 GET API 정보는
         http://localhost:9000/api/vi/crud-api
    2. HttpInterface를 이용한 API 요청 - service.HttpInterfaceAPIService

       ```java
       package com.gyumin.apiclient0320.service;

       import org.springframework.web.service.annotation.GetExchange;

       public interface HttpInterfaceAPIService {
           @GetExchange("/api/v1/crud-api")
           public String getName();
       }
       ```

    3. HttpInterface의 구현체를 생성하고 Bean으로 생성해주는 설정 파일(@Configuration) 생성 - config.HttpConfiguration

       ```java
       package com.gyumin.apiclient0320.config;

       import com.gyumin.apiclient0320.service.HttpInterfaceAPIService;
       import org.springframework.context.annotation.Bean;
       import org.springframework.web.reactive.function.client.WebClient;
       import org.springframework.web.reactive.function.client.support.WebClientAdapter;
       import org.springframework.web.service.invoker.HttpServiceProxyFactory;

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

           // HttpInterface 객체 생성 - 실제 사용될 서비스 객체
           public HttpInterfaceAPIService httpInterfaceAPIService(HttpServiceProxyFactory httpServiceProxyFactory) {
               return httpServiceProxyFactory.createClient(HttpInterfaceAPIService.class);
           }
       }
       ```

    4. HttpInterface를 사용할 클래스를 생성 - HttpInterfaceService

       ```java
       package com.gyumin.apiclient0320.service;

       import lombok.RequiredArgsConstructor;
       import org.springframework.stereotype.Service;

       @Service
       @RequiredArgsConstructor
       public class HttpInterfaceService {
           private final HttpInterfaceAPIService httpInterfaceAPIService;

           public String getName() {
               return httpInterfaceAPIService.getName();
           }
       }
       ```

    5. Controller에서 Service 메서드 호출

       ```java
       package com.gyumin.apiclient0320.controller;

       import com.gyumin.apiclient0320.service.HttpInterfaceAPIService;
       import lombok.RequiredArgsConstructor;
       import org.springframework.web.bind.annotation.GetMapping;
       import org.springframework.web.bind.annotation.RequestMapping;
       import org.springframework.web.bind.annotation.RestController;

       @RestController
       @RequiredArgsConstructor
       @RequestMapping("/httpinterface")
       public class HttpInterfaceController {
           private final HttpInterfaceAPIService httpInterfaceAPIService;

           @GetMapping
           public String getName() {
               return httpInterfaceAPIService.getName();
           }
       }
       ```
