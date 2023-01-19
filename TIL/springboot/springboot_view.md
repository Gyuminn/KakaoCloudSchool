# [Spring Boot] View

1. **Java Web Application에서 화면을 출력하는 방법**
   1. JSP 이용 - 서버의 데이터를 출력하고자 하면 EL(내장)과 JSTL(외부 라이브러리)을 학습해야 함.
      - EL은 표현식으로 자바에서 전달한 데이터를 출력하는 문법: ${데이터}
      - JSTL은 Apache에서 제공하는 Custom Tag로 웹 프로그래밍에서 많이 사용하는 자바 기능을 태그 형태로 만들어 준 것
   2. Template Engine 이용
      - 서버의 데이터를 View로 출력하기 위해서 만든 문법
      - 일반적으로 확장자를 html을 사용
      - Thymeleaf, Velocity, FreeMaker, Mustache, Grooby 등이 있음
   3. 별도의 Front End Application 생성
      - Web Application이나 Mobile Application을 별도로 제작해서 Server의 데이터를 출력
      - javascript나 java, kotlin, swift 같은 언어로 제작
      - javascript를 이용할 것이라면 ajax, fetch api 또는 axios 라이브러리 사용법을 익혀야 한다.
        jQuery는 별도의 ajax 관련 함수를 제공하지만 react, vue, angular는 화면에 관련된 기능만 제공
      - 모바일의 경우는 서버에서 데이터를 받아오는 방법과 스레드를 미리 학습해야 한다.
2. **프로젝트 생성**

   의존성 설정

   Spring Boot DevTools, Lombok, Spring Web, Thymeleaf

3. **JSP 출력**

   spring boot에서는 jsp 출력을 지원하지 않음.

   1. build.gradle 파일에 의존성 추가

      ```java
      dependencies {
      	implementation 'org.apache.tomcat.embed:tomcat-embed-jasper'
      	...
      }
      ```

   2. [application.properties](http://application.properties) 파일을 application.yml로 변경하고 작성

      ```java
      // application.properties
      spring.mvc.view.prefix=/WEb-INF/views/
      spring.mvc.view.suffix=.jsp
      spring.thymeleaf.prefix=classpath:/templates/
      spring.thymeleaf.suffix=.html
      spring.thymeleaf.cache=false
      spring.thymeleaf.view-names=thymeleaf/*
      ```

      ```yaml
      // application.yml
      spring:
        mvc:
          view:
            prefix: /WEB-INF/views/
            suffix: .jsp

        thymeleaf:
          prefix: classpath:/templates/
          suffix: .html
          cache: false
          view-names: thymeleaf/*
      ```

   3. Controller 클래스를 추가하고 요청을 처리하는 메서드를 생성

      ```java
      @Controller
      public class PageController {
          @GetMapping("/")
          public String main(Model model){
              model.addAttribute("message", "Spring Boot 에서의 JSP");
              return "main";
          }
      }
      ```

   4. 뷰를 출력할 디렉토리를 생성
      - src/main 디렉토리 안에 webapp 디렉토리 생성
      - webapp 디렉토리 안에 WEB_INF 디렉토리 생성
      - WEB-INF 디렉토리 안에 views 디렉토리를 생성
   5. views 디렉토리 안에 main.jsp 파일을 생성하고 작성

      ```java
      <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
      <!DOCTYPE html>
      <html>
      <head>
          <title>Spring Boot에서 JSP 출력</title>
          <meta charset="UTF-8"/>
      </head>
      <body>
          메시지: <%=request.getAttribute("message")%>
      </body>
      </html>
      ```

   6. 실행

4. **Thymeleaf**

   1. 개요

      화면 출력을 위한 템플릿 엔진 중 하나

   2. 장점
      - 데이터 출력이 JSP의 EL과 유사하게 `${}` 를 이용
      - Model에 담긴 데이터를 화면에서 JavaScript로 처리하기 편리
      - 연산이나 포맷과 관련된 기능을 추가적인 라이브러리없이 지원
      - html 파일로 바로 출력이 가능 - 서버 사이드 렌더링을 하지 않고 출력 가능
   3. 도큐먼트: https://www.thymeleaf.org
   4. thymeleaf 출력을 위해서 application.yml 변경

      ```yaml
      server:
        port: 80

      spring:
        thymeleaf:
          cache: false
      ```

   5. thymeleaf 기본 출력
      - Controller 클래스에 요청 처리 메서드 작성
        ```java
        ...
        @GetMapping("/article")
            // 리턴 타입이 void이면 출력하는 뷰 이름은 요청 URL
            // view의 이름은 article
            public void article(Model model) {
                LOGGER.info("article 요청이 들어왔습니다.");
            }
        ```
      - thymeleaf는 templates 디렉토리에 작성 - article.html 파일을 만들고 작성
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>Title</title>
          </head>
          <body>
            <h1 th:text="${'Hello Thymeleaf'}"></h1>
          </body>
        </html>
        ```
   6. 문법

      - 속성이 아닌 곳에서 데이터 출력: `[[${데이터이름}]]`
      - 반복문
        - `th:each = “임시변수:${데이터목록}”`
        - 반목문을 사용하면 state 객체가 생성되는데 이 객체에 인덱스가 전달된다.
      - 분기문
        - `th:if ~ unless` 이용
        - `th:switch와 th:case` 제공
      - 삼항 연산자 사용 가능하고 마지막 항은 생략 가능
      - 데이터 출력을 위해서 Controller 의 기본 요청을 수정

        ```java
        ...
        @GetMapping("/")
            public String main(Model model){
                Map<String, Object> map = new HashMap<>();
                map.put("title", "개발자 이야기");
                map.put("content", "개발은 창조적인 것이다");
                map.put("author", "gyumin");

                model.addAttribute("map", map);

                List<String> list = new ArrayList<>();
                list.add("Developer");
                list.add("Developer");
                list.add("MLOps");
                list.add("DevOps");
                list.add("DBA");

                model.addAttribute("list", list);

                return "main";
            }
        ```

      - template 디렉토리에 main.html 파일을 생성하고 작성
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>데이터 출력</title>
            <style>
              table,
              tr,
              td,
              th {
                border: 1px solid #444444;
              }
            </style>
          </head>
          <body>
            <table>
              <tr>
                <th>제목</th>
                <th>본문</th>
                <th>저자</th>
              </tr>
              <tr>
                <td>[[${map.title}]]</td>
                <td>[[${map.content}]]</td>
                <td>[[${map.author}]]</td>
              </tr>
            </table>
            <table>
              <tr th:each="task:${list}">
                <td>[[${task}]]</td>
              </tr>
            </table>
          </body>
        </html>
        ```

   7. DTO의 List 출력

      - DTO 클래스 생성 - domain.SampleVO

        ```java
        package com.kakao.springview0109.domain;

        import lombok.Builder;
        import lombok.Data;

        import java.time.LocalDateTime;

        @Data
        @Builder
        public class SampleVO {
            private Long sno;
            private String first;
            private String last;
            private LocalDateTime regTime;
        }
        ```

      - Controller 클래스에 요청 처리 메서드 추가

        ```java
        ...
        @GetMapping("/sample")
            public void sample(Model model) {
                List<SampleVO> list = IntStream.range(1, 20)
                        .asLongStream()
                        .mapToObj(i -> {
                            SampleVO vo = SampleVO.builder()
                                    .sno(i)
                                    .first("Frist.." + i)
                                    .last("Last.." + i)
                                    .regTime(LocalDateTime.now())
                                    .build();
                            return vo;
                        }).collect(Collectors.toList());

                model.addAttribute("list", list);
            }
        ```

      - templates 디렉토리에 sample.html 파일을 만들어서 데이터를 출력
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>데이터 목록 출력</title>
          </head>
          <body>
            <ul>
              <li th:each="vo, state:${list}" th:if="${vo.sno % 5 == 0}">
                [[${state.index}]] -- [[${vo}]]
              </li>
            </ul>
          </body>
        </html>
        ```
      - sample.htlm 수정 - if와 unless 동시에 사용은 안되니까 안에다가 만들거나, 삼항 연산자 이용
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>데이터 목록 출력</title>
          </head>
          <body>
            <ul>
              <li th:each="vo, state:${list}">
                <span
                  th:text="${vo.sno % 5 == 0} ? ${vo.sno} : ${vo.first}"
                ></span>
              </li>
            </ul>
          </body>
        </html>
        ```

   8. th: block
      - 별도의 태그 없이 출력하고자할 때 사용
      - case에 boolean이 가능.
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>데이터 목록 출력</title>
          </head>
          <body>
            <ul>
              <li th:each="vo, state:${list}">
                <th:block th:switch="${vo.sno % 5 == 0}">
                  <span th:case="true" th:text="${vo.sno}"></span>
                  <span th:case="false" th:text="${vo.last}"></span>
                </th:block>
              </li>
            </ul>
          </body>
        </html>
        ```
   9. 링크 생성
      - 기본 형식: <a href=”url”>이미지나 텍스트</a>
      - Thymeleaf에서는 `@{ }` 로 href 설정
      - 파라미터 설정은 `(파라미터이름=${데이터})`
      - Controller에 URL 추가
        ```java
        ...
        @GetMapping("/exlink")
            public void exlink(Model model) {
                List<SampleVO> list = new ArrayList<>();
                for(long i = 0; i<10; i++) {
                    SampleVO vo = SampleVO.builder()
                            .sno(i)
                            .first("First.." + i)
                            .last("Last.." + i)
                            .regTime(LocalDateTime.now())
                            .build();
                    list.add(vo);
                }
                model.addAttribute("list", list);
            }
        ```
      - templates 디렉토리에 exlink.html 작성
        ```html
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <title>Link</title>
          </head>
          <body>
            <ul>
              <li th:each="vo:${list}">
                <a th:href="@{/exview}">[[${vo.first}]]</a>
                <a th:href="@{/exview(sno=${vo.sno})}">[[${vo.first}]]</a>
                <a th:href="@{/exview/{sno}(sno=${vo.sno})}">[[${vo.first}]]</a>
              </li>
            </ul>
          </body>
        </html>
        ```
   10. 출력 포맷 설정
       - 숫자의 경우는 #numbers를 이용해서 설정
       - 날짜의 경우는 #temporals를 이용해서 설정
       - /exformat URL 추가
         ```java
         ...
         @GetMapping({"/exlink", "/exformat"})
             public void exlink(Model model) {
         		...
         ```
       - templates 디렉토리에 exformat.html 작성
         ```java
         <!DOCTYPE html>
         <html lang="en">
         <head>
             <meta charset="UTF-8">
             <title>Title</title>
         </head>
         <body>
           <ul>
             <li th:each="vo:${list}">
               [[${#temporals.format(vo.regTime, 'yyyy/MM/dd')}]]
             </li>
           </ul>
         </body>
         </html>
         ```
   11. 레이아웃 설정
       - include 방식: 외부에서 내용을 가져와서 삽입하는 방식
         `th:insert`: 삽입
         `th:replace`: 교체
       - 레이아웃에 사용될 파일을 저장할 fragments 디렉토리를 templates 디렉토리에 생성
       - fragments 디렉토리에 포함될 내용을 가진 fragment1.html 파일을 만들고 작성
         ```java
         <!DOCTYPE html>
         <html lang="en">
         <head>
             <meta charset="UTF-8">
             <title>Title</title>
         </head>
         <body>
           <div th:fragment="part1">
             <h2>Part1</h2>
           </div>
           <div th:fragment="part2">
             <h2>Part2</h2>
           </div>
           <div th:fragment="part3">
             <h2>Part3</h2>
           </div>
         </body>
         </html>
         ```
       - exlayout.html
         ```java
         <!DOCTYPE html>
         <html lang="en">
         <head>
             <meta charset="UTF-8">
             <title>Title</title>
         </head>
         <body>
           <h1>Part1</h1>
           <div th:replace="~{/fragments/fragment1 :: part1}"></div>
         </body>
         </html>
         ```

5. **BootStrap**

   1. bootstrap
      - 반응형 웹을 만들기 쉽게 해주는 자바스크립트 라이브러리
      - 이벤트 처리에 jquery 이용 가능
   2. sidebar가 적용된 bootstrap

      https://startbootstrap.com/template/simple-sidebar

   3. 압축을 해제한 디렉토리의 모든 내용을 복사해서 static 디렉토리에 붙여넣기
   4. 압축을 해제한 디렉토리의 index.html 파일의 내용을 복사해서 template/layout 디렉토리에 basic.html 파일을 만들고 붙여넣기
