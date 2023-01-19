# [Spring Boot] 기초

1. **Spring Boot**

   단독 실행되는 상용화 가능한 수준의 스프링 기반 애플리케이션을 최소한의 설정으로 만들어 사용하기 위해서 등장

   1. 장점
      - 환경 설정 최소화
      - WAS를 내장해서 독립 실행이 가능한 스프링 애플리케이션 개발이 가능
      - Spring Boot Starter라는 의존성을 제공해서 기존의 Maven이나 Gradle 설정을 간소화
      - XML 설정 없이 자바 수준의 설정 방식 제공
      - JAR을 사용하여 자바 옵션만으로 배포 가능
      - 애플리케이션의 모니터링과 관리를 위한 Spring Actuator 제공
   2. 단점
      - 버전 변경이 너무 자주 일어남.
      - 특정 설정을 별도로 하거나 설정 자체를 변경하고자 하는 경우 내부의 설정 코드를 살펴보아야 함.

2. **프로젝트 생성**

   1. STS

      Spring Starter Project로 생성

   2. IntelliJ Ultimate Edition

      Spring Initializer 프로젝트 생성

   3. IntelliJ Community Edition

      [start.spring.io](http://start.spring.io) 사이트에 접속해서 옵션을 설정한 후 Generate를 클릭 - 압축된 파일이 다운로드 됨.

      압축을 해제한 후 IntelliJ에서 그 디렉토리를 열면 된다.

3. **프로젝트에 Controller 클래스를 만들어서 확인**

   1. SampleController

      ```java
      import org.springframework.web.bind.annotation.GetMapping;
      import org.springframework.web.bind.annotation.RestController;

      // View 대신에 문자열(CSV)나 JSON을 리턴하는 컨트롤러를 만들고자 할 떄 사용하는 어노테이션
      @RestController
      public class SampleController {
          // /hello라는 요청을 Get 방식으로 요청한 경우
          @GetMapping("/hello")
          // String을 리턴하면 일반 문자열로 출력
          // VO나 List를 리턴하면 JSON 문자열로 출력
          public String[] hello() {
              return new String[]{"STS", "IntelliJ"};
          }
      }
      ```

   2. 실행
   3. 브라우저에서 localhost:8080/hello

4. **포트 번호 변경**
   - Oracle과 같이 사용하는 경우나 다른 프로젝트를 같이 실행해야하는 경우 포트 충돌이 발생할 수 있음.
   - [application.properties](http://application.properties) 파일에 `server.posrt=포트번호`를 설정하면 포트 번호를 사용해서 실행이 된다.
   - 애플리케이션을 다시 실행하고 브라우저에 [localhost/80으로](http://localhost/80으로) 재실행
   - 설정 파일을 YAML로 변경 - 기존 설정 파일의 이름을 application.yml 로 수정
   - 서버 포트 설정 코드를 추가
     ```yaml
     server:
       port: 80
     ```
5. **REST API**

   1. REST(Representational State Transfer)
      - 분산 하이퍼미디어 시스템 아키텍쳐의 한 형식
      - 자원에 이름을 정하고(URL) URL에 명시된 HTTP Method(GET, POST, PUT, DELETE)를 통해서 해당 자원의 상태를 주고받는 것을 의미.
        - **동일한 자원에 대한 요청은 동일한 URL로 처리: Seamless 가능, 뷰를 만들지 말고 데이터를 전송**
        - URL은 소문자로만 작성
   2. REST API

      REST 아키텍쳐를 따르는 시스템/애플리케이션 인터페이스

      REST 아키텍쳐를 따르는 서비스를 RESTful 하다라고 표현

   3. 특징
      - 유니온 인터페이스 - 일관된 인터페이스
      - 무상태성 - 서버에 상태 정보를 따로 보관하거나 관리하지 않는다는 의미
        세션이나 쿠키 사용을 하지 않음. - 서버에 불필요한 정보를 저장하지 않음.
        Web Token이나 로컬 스토리지 사용으로 대체
      - 캐시 가능성
      - Layerd System
        서버는 네트워크 상의 여러 계층으로 구성될 수 있지만 클라이언트는 서버의 복잡도와 상관없이 End Point만 알면 됨.
      - 클라이언트 - 서버 아키텍쳐
        클라이언트 애플리케이션과 서버 애플리케이션을 별도로 설계하고 구현해서 서로에 대한 의존성을 낮추는 것.
   4. URL 설계 규칙
      - URI의 마지막은 /를 포함하지 않음.
      - 언더바 대신에 -(하이픈)을 사용.
      - URL에는 행위가 아닌 결과를 포함. - 행위는 HTTP 메서드로 표현
      - URL은 소문자로 작성
      - URL에 파일의 확장자를 표현하지 않음. - 파일의 확장자는 accept header를 이용

6. **GET API**

   1. GET 방식
      - 웹 애플리케이션 서버에서 값을 가져올 때 주로 이용
      - URL 매핑
        - `@RequestMapping(method=RequestMethod.GET, value=”URL”)`
        - `@GetMapping(”URL”)`
   2. 테스트
      - 브라우저에서 테스트 가능
      - API 요청 툴(POSTMAN 등)에서도 가능
   3. GET 요청을 처리하는 클래스를 만들어서 요청을 처리하는 메서드를 생성

      ```java
      package com.kakao.springboot0109;

      import org.springframework.web.bind.annotation.RequestMapping;
      import org.springframework.web.bind.annotation.RequestMethod;
      import org.springframework.web.bind.annotation.RestController;

      @RestController
      // 공통된 URL
      @RequestMapping("/api/v1/rest-api")
      public class JSONController {
          @RequestMapping(value="/hello", method= RequestMethod.GET)
          public String getHello() {
              return "GET Hello";
          }
      }
      ```

   4. 실행한 후 요청을 확인 - 브라우저에 localhost/api/v1/rest-api/hello
   5. Spring 4.3에서 추가된 요청 처리 어노테이션을 이용 - GetMapping

      ```java
      ...
      @GetMapping("/newhello")
          public String getNewHello() {
              return "Get New Hello";
          }
      ```

   6. URL에 포함된 파라미터 처리

      파라미터가 1개일 때는 파라미터를 URL에 포함시켜 전송할 수 있다.

      - GET이나 DELETE인 경우
        - 요청 처리 메서드의 URL을 설정할 때 파라미터로 사용된 부분을 {변수이름} 으로 설정하고 요청 처리 메서드의 매개변수로 `@PathVariable(변수이름) 자료형 이름` 을 추가해주면 된다.
        - 변수 이름은 일치해야 한다.
        - Controller 클래스에 요청 처리 메서드를 추가
          ```java
          ...
          @GetMapping("/product/{num}")
              public String getNum(@PathVariable("num") int num) {
                  return num + "";
              }
          ```
        - 데이터의 형 변환은 자동으로 수행하는데 자료형이 맞지 않으면 예외가 발생한다.
        - 파라미터를 대입하지 않으면 404 에러.

   7. 일반 파라미터 처리
      - HttpServletRequest 이용해서 처리(가장 권장하지 않음.)
        `String getParameter(String 파라미터이름)`와 `String[] getParameterValues(String 파라미터이름)`를 이용
      - @RequestParam 어노테이션 이용(중간 권장)
        요청 처리 메서드의 매개변수로 `@RequestParam(String 파라미터이름) 자료형 변수이름` 을 설정해서 처리
      - Command 객체 이용(가장 권장 - 근데 DTO가 많아짐.)
        파라미터 이름을 속성으로 갖는 클래스를 만들고 클래스의 참조형 변수를 요청 처리 메서드의 매개변수로 대입해서 처리
   8. GET 방식의 파라미터 설정
      - URL 뒤에 ?를 추가하고 이름=값(&이름=값…)
      - form을 만들고 method를 생략하거나 method에 get이라고 설정
      - ajax나 fetch api에서 전송 방식을 GET으로 설정
   9. name, email, organization을 GET 방식으로 전송했을 때 처리

      - Controller 클래스에 HttpServletRequest를 이용해서 처리하기 위한 메서드를 구현

        ```java
        ...
        @GetMapping("/param")
            public String getParam(HttpServletRequest request) {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String organization = request.getParameter("organization");

                return name + ":" + email + ":" + organization;
            }

        // http://localhost/api/v1/rest-api/param?name=gyumin&email=rhkdtlrtm12@gmail.com&organization=kakao
        // gyumin:rhkdtlrtm12@gmail.com:kakao // 결과
        ```

      - @RequestParam으로 처리하는 방법

        ```java
        ...
        @GetMapping("/param1")
            public String getParam(
                    @RequestParam("name") String name,
                    @RequestParam("email") String email,
                    @RequestParam("organization") String organization
            ) {
                return name + ":" + email + ":" + organization;
            }

        // 결과 동일
        ```

      - Command 객체를 이용해서 처리하는 방법 - dto.paramDTO

        ```java
        package com.kakao.springboot0109.dto;

        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        @Data
        public class paramDTO {
            private String name;
            private String email;
            private String organization;
        }
        ```

        ```java
        ...
        @GetMapping("/param2")
            public String getParam(
                    ParamDTO paramDTO
            ) {
                return paramDTO.getName() + ":" + paramDTO.getEmail() + ":" + paramDTO.getOrganization();
            }
        ```

      - 파라미터 이름을 모르는 경우에는 HttpServletRequest나 @RequestParam을 이용해서 처리 가능
      - HttpServletRequest의 경우는 getParameterMap 메서드나 getParameterNames 메서드를 활용해서 처리가 가능하고 @RequestParam Map<String, String> param을 이용해서 처리 가능 - 근데 사실상 말이 안됨. 클라이언트가 보내는 요청이 무엇인지 모르는 상태에서 서버가 처리하는 것은 말이 안되기 때문.

7. **POST API**

   - 리소스를 저장할 때 사용하는 요청 방식
   - POST 방식에서는 리소스나 값을 HTTP Body에 담아서 서버에 전달
   - 파라미터 처리는 HttpServletRequest나 @RequestParam 그리고 Command 객체를 이용한 처리가 모두 가능한데 일반적으로 Command 객체를 이용해서 처리할 때는 클래스 이름 앞에 @RequestBody를 추가해서 HttpBody의 내용을 객체에 매핑하겠다고 명시적으로 알려주는 경우를 권장.

   1. POST 방식에서의 파라미터 전송
      - form 태그의 method를 post로 설정해서 폼의 데이터 전송
      - ajax나 fetch api에서 method 속성의 값을 POST로 설정해서 전송
   2. POST 방식의 테스트

      ```java
      ...
      @PostMapping("/param")
          public String getPostParam(@RequestBody ParamDTO paramDTO) {
              return paramDTO.toString();
          }
      ```

      POSTMAN을 이용해서 테스트

8. **PUT API**

   데이터를 수정할 때 사용하는 방식(유사한 방식으로 PATCH도 존재)

   사용 방법은 POST와 유사

   1. 요청 방법
      - ajax나 fetch api에서는 method를 PUT으로 설정
      - form에서 method 속성은 GET과 POST만 설정 가능
        PUT으로 설정하면 GET으로 처리가 된다.(주의!)
        form에서 처리하고자 하는 경우에는 form 안에 **<input type=”hidden” name=”_method” value=”PUT” />**을 추가해서 전송해야 한다.
   2. PUT 요청을 처리하는 메서드를 Controller 클래스에 추가

      ```java
      ...
      @PostMapping("/param")
          public String getPostParam(@RequestBody ParamDTO paramDTO) {
              return paramDTO.toString();
          }
      ```

   3. Controller 클래스에 리턴 타입이 다른 메서드 추가

      ```java
      ...
      @PutMapping("/param1")
          public ParamDTO getPutParam1(@RequestBody ParamDTO paramDTO){
              return paramDTO;
          }

      // 결과 - JSON으로 출력
      // {
      //     "name": "규민",
      //     "email": "rhkdtlrtm12@gmail.com",
      //     "organization": "kakao"
      // }
      ```

   4. 리턴타입을 ResponseEntity로 설정하여 status 상태를 같이 넘겨줄 수 있다.

      ```java
      ...
      @PutMapping("/param2")
          public ResponseEntity<ParamDTO> getPutParam2(@RequestBody ParamDTO paramDTO) {
              return ResponseEntity.status(HttpStatus.ACCEPTED)
                      .body(paramDTO);
          }

      // 결과
      // 이 전에는 200 OK 이던 것이 202 Accepted로 바뀜.
      ```

9. **DELETE API**

   데이터를 삭제할 때 사용

   삭제를 할 때는 기본키값 하나 만으로 삭제하는 경우가 많기 때문에 GET 방식과 동일한 방식으로 처리

   1. Controller 클래스에 DELETE 요청 처리 메서드를 생성

      ```java
      ...
      @DeleteMapping("/product/{num}")
          public String deleteNum(@PathVariable("num") int num) {
              return num + "";
          }

      @DeleteMapping("/product")
          public String deleteParamNum(@RequestParam("num") int num) {
              return num + "";
          }
      ```

10. **로깅 라이브러리**

    1. Logging

       애플리케이션이 동작하는 동안 시스팀이의 상태 정보나 동작 정보를 시간 순으를 기록하는 것.

       - Loggin은 개발 영역 중 비기능 요구사항(**Common Concern**)에 속하지만 디버깅하거나 개발 이후 발생한 문제를 해결할 때 원인 분석에 꼭 필요한 요소
       - 자바 진영에서 가장 많이 사용되는 로깅 라이브러리는 Logback

    2. Logback
       - 104j 이후에 출시된 로깅 프레임워크로서 slf4j를 기반으로 구현되었으면 log4j 에 비해 성능이 향상됨.
       - spring-boot-starter-web에 내장
       - https://logback.quos.ch/manual/introduction.html
       - 5개의 로그 레벨 설정 가능
         - ERROR
           심각한 문제가 발생해서 애플리케이션의 동작이 불가능
         - WARN
           시스템 에러의 원인이 될 수 있느 경고 레벨
         - INFO
           상태 변경과 같은 정보 전달
         - DEBUG
           디버깅할 떄 메시지를 출력
    3. Controller 안에 로깅 설정

       ```java
       @RestController
       // 공통된 URL
       @RequestMapping("/api/v1/rest-api")
       public class JSONController {
           // 로깅 가능한 객체를 생성
           private final Logger LOGGER = LoggerFactory.getLogger(JSONController.class);

           @RequestMapping(value = "/hello", method = RequestMethod.GET)
           public String getHello() {
               LOGGER.info("hello 요청이 왔습니다. 2");
               return "GET Hello";
           }
       	...
       ```
