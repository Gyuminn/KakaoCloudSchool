# [Spring Boot] Communication

1. **Web Socket**

   socket: 디바이스 간의 통신을 할 수 있도록 해주는 추상화(물리적으로는 NIC가 통신을 하지만 프로그래밍에서는 하드웨어를 직접 제어할 수 없기 때문에 운영체제 또는 Virtual Machine이 제공하는 API로 만든 것)된 클래스나 객체

   1. 통신 방식의 분류
      - Low level과 High Level 통신
        - Low Level 통신(Socket 통신)
          Socket API를 이용해서 직접 통신
        - High Level 통신
          Socket API를 직접 이용하지 않고 Wrapping된 API를 이용하는 방식
      - TCP와 UDP
        - TCP는 연결형 통신
          하나의 전송이 완료될 때 까지 연결을 유지하는 방식
          한 쪽이 다른 쪽에게 요청 메시지를 전송하고 데이터를 보내는 쪽이 이 메시지에 응답(Meta Data - 데이터를 위한 데이터)을 전송하고 받는 쪽에서 요청 메시지를 보내고 보내는 쪽에서 실제 데이터를 전송을 하고 받는 쪽은 받은 데이터에 대한 응답을 전송하는 방식
          http나 https는 이 한 번의 전송이 끝나면 연결을 해제(상태가 없다라는 표현)
        - UDP는 비연결형 통신
          보내는 쪽에서 받는 쪽으로 일방적으로 데이터를 전송하고 종료되는 방식
          SNMP나 DNS 등이 UDP 방식
          Google의 FCM(Firebase Cloud Messing) 서비스나 Apple의 APNS(Apple Push Notification Service)는 UDP 방식으로 구현
          중요하지 않은 메시지를 여러 곳에 전송하고자 할 때 이 방식을 사용
      - 매칭되는 방식에 따른 분류
        - Point To Poing: 1대 1로 매핑
        - Broadcast: 1대 전체로 매핑 → 방송 시스템
        - Multicast: 1대 Group으로 매핑 → 화상 회의
        - Anycast: 가까운 곳 하나와 매핑 → CDN(?)
   2. Web Socket
      - https나 https는 TCP 방식으로 통신하고 상태를 보존하는 형태의 통신 방식
        http나 https는 짧은 내용을 전송할 때 오버헤드가 큼
        `상태 유지가 되지 않음` - Session이나 Cookie 또는 LocalStorage를 이용한 전송 등의 방식을 이용해서 상태를 유지한다.
      - 웹 환경에서 실시간 양방향 통신을 할 수 있도록 하고 헤더의 부피를 줄인 Web Socket이 HTML5 Spec으로 제공
      - 사용하는 경우
        실시간 양방향 통신이 필요
        많은 수의 동시 접속자를 수용해야 하는 경우
        브라우저에서 TCP 기반의 통신으로 확장하고자 하는 경우
        SOA(Service Oriented Architecture) 기반으로 확장하고자 하는 경우
      - HTML5 Spec 이지만 다른 브라우저와 통신을 해야하기 때문에 Server Application이 필요

2. **Web Socket을 이용한 채팅 구현**

   1. 프로젝트 생성

      - 의존성: DevTools, Lombok, Spring Web, Thymeleaf, WebSocket
      - application.properties를 제거하고 applilcation.yml로 작성

        ```yaml
        server:
        	port: 9999

        spring:
        	thymeleaf:
        		cache: false
        ```

   2. 프로젝트에 WebSocket 클래스를 추가하고 작성 - Service.WebSocketChat

      ```java
      package com.gyumin.websocket0310.service;

      import jakarta.websocket.OnClose;
      import jakarta.websocket.OnMessage;
      import jakarta.websocket.OnOpen;
      import jakarta.websocket.Session;
      import jakarta.websocket.server.ServerEndpoint;
      import org.springframework.stereotype.Service;

      import java.util.*;

      @Service
      // chat이라는 URL을 처리할 수 있는 WebSocket 클래스
      @ServerEndpoint(value = "/chat")
      public class WebSocketChat {
          // 접속한 클라이언트(Session) 목록을 저장할 Collections
          // Collections.synchronizedSet를 이용하면 동기적으로 만들 수 있다. 여러 곳에서 건드는 것을 방지
          // 하나만 만들면 되므로 static을 붙여주자.
          private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

          // 여러 유저 중 한 명에게 귓말 기능을 줄 수 있는 경우
          // 로그인이 없어서 이렇게 하긴 했는데 중요한 건 이러한 구조가 특정 유저를 찾기에 빠르다는 것이다.
          private Map<String, Session> users = new HashMap<>();

          // 클라이언트가 접속했을 때
          @OnOpen
          public void onOpen(Session s) {
              // 클라이언트 정보 출력
              System.out.println("접속: " + s.toString());

              // 존재하는 Session인지 확인
              if (!clients.contains(s)) {
                  clients.add(s);
                  System.out.println("유저 접속");
              } else {
                  System.out.println("이미 접속된 유저");
              }
          }

          // 클라이언트가 메시지를 전송했을 때
          @OnMessage
          public void onMessage(String msg, Session session) throws Exception{
              // 여기서 DB에 저장할 수도 있음.
              System.out.println("받은 메시지: " + msg);

              // 브로드캐스트 통신
              for (Session s : clients) {
                  // 클라이언트에게 문자열 메시지를 전송
                  s.getBasicRemote().sendText(msg);
              }
          }

          // 클라이언트가 접속을 해제할 때
          @OnClose
          public void onClose(Session s) {
              System.out.println("접속 종료: " + s.toString());
              clients.remove();
          }
      }
      ```

   3. 웹 소켓 클래스는 클라이언트가 접속을 할 떄마다 인스턴스가 생성되는데 이렇게 동작하지 않도록 하기 위해 설정 클래스 추가 - config.WebSocketConfig

      ```java
      package com.gyumin.websocket0310.config;

      import org.springframework.context.annotation.Bean;
      import org.springframework.stereotype.Component;
      import org.springframework.web.socket.server.standard.ServerEndpointExporter;

      @Component
      public class WebSocketConfig {
          @Bean
          public ServerEndpointExporter serverEndpointExporter() {
              return new ServerEndpointExporter();
          }
      }
      ```

   4. Controller 클래스 생성 - controller.ChatController

      ```java
      package com.gyumin.websocket0310.Controller;

      import org.springframework.stereotype.Controller;
      import org.springframework.web.bind.annotation.GetMapping;

      @Controller
      public class ChatController {
          @GetMapping("/")
          public String chat() {
              return "chating";
          }
      }
      ```

   5. 화면에 출력될 뷰 파일을 생성 - templates.chatting.html

      ```html
      <!DOCTYPE html>
      <html lang="en">
        <head>
          <meta charset="UTF-8" />
          <title>WebSocket</title>
        </head>
        <body>
          <div id="chat">
            <h1>Web Chatting</h1>
            <input type="text" id="mid" value="noname" />
            <input type="button" value="접속" id="btnLogin" />
            <br />
            <!-- 데이터 출력 영역 -->
            <div id="talk"></div>
            <!-- 전송할 메시지 영역 -->
            <div id="sendZone">
              <textarea id="msg" value=""></textarea>
              <input type="button" value="전송" id="btnSend" />
            </div>
          </div>
        </body>
        <link rel="stylesheet" type="text/css" href="./css/chat.css" />
        <script src="./js/chat.js"></script>
      </html>
      ```

   6. 스타일 설정을 위한 파일 작성 - static/css/chat.css

      ```css
      @charset "UTF-8";

      * {
        box-sizing: border-box;
      }

      #chat {
        width: 800px;
        margin: 20px auto;
        overflow: scroll;
        border: 1px solid #aaa;
      }

      #chat #msg {
        width: 740px;
        height: 100px;
        display: inline-block;
      }

      #chat #sendZone > * {
        vertical-align: top;
      }

      #chat #btnSend {
        width: 54px;
        height: 100px;
      }

      #chat #talk div {
        width: 70%;
        display: inline-block;
        padding: 6px;
        border-radius: 6px;
      }

      #chat .me {
        backdrop-color: #ffc;
        magin: 1px 0px 2px 30%;
      }

      #chat .other {
        background-color: #eee;
        margin: 2px;
      }
      ```

   7. 클라이언트 파일 작성 - static/js/chat/.js

      ```jsx
      // id 찾아오는 함수
      function getId(id) {
        return document.getElementById(id);
      }

      let data = {}; // 전송 데이터

      let ws;
      let mid = getId("mid");
      let btnLogin = getId("btnLogin");
      let btnSend = getId("btnSend");
      let talk = getId("talk");
      let msg = getId("msg");

      // 로그인 처리 - 웹 소켓 연결
      btnLogin.addEventListener("click", (e) => {
        // 연결
        ws = new WebSocket("ws://" + "192.168.0.155:9999" + "/chat");
        // 메시지가 온 경우
        ws.addEventListener("message", (msg) => {
          // JSON 파싱
          let data = JSON.parse(msg.data);

          // 전송한 유저가 자신인지 아닌지에 따라 클래스를 다르게 적용
          let css;
          if (data.mid === mid.value) {
            css = "class=me";
          } else {
            css = "class=other";
          }

          // 출력할 메시지 생성
          let item = `<div ${css}><span><b>${data.mid}</b></span>[${data.date}]<br/><span>${data.msg}</span></div>`;

          // 출력
          talk.innerHTML += item;
          // 스크롤바 이동
          talk.scrollTop = talk.scrollHeight;
        });
      });

      // 데이터를 전송하는 함수
      function send() {
        if (msg.value.trim() != "") {
          // 전송할 데이터를 생성
          data.mid = getId("mid").value;
          data.msg = msg.value;
          data.date = new Date().toLocaleString();
          // 서버에 전송할 때는 객체를 JSON 문자열로 변환해서 전송
          let temp = JSON.stringify(data);
          ws.send(temp);
        }
        msg.value = "";
      }

      btnSend.addEventListener("click", (e) => {
        send();
      });

      msg.addEventListener("keyup", (e) => {
        if (e.keyup === 13) {
          send();
        }
      });
      ```

3. **Gradle 기반의 Spring Boot 프로젝트 도커 빌드**

   1. 프로젝트의 root 디렉토리에 Dockerfile을 만들고 작성

      ```docker
      FROM amazoncorretto:17
      CMD ["./mvnw", "clean", "package"]
      ARG JAR_FILE=target/*.jar
      COPY ./build/libs/*.jar app.jar
      ENTRYPOINT ["java", "-jar", "app.jar"]
      ```

   2. 터미널에서 현재 프로젝트 경로로 이동한 후 명령 수행

      ```bash
      docker build -f Dockerfile -t docker-example:0.0.1 .
      ```

      error가 뜨면 `./gradlew clean build` 를 수행한 후 다시 수행

      이미지 확인: `docker images`

   3. 컨테이너 생성

      ```bash
      docker run -p 9999:9999 docker-example:0.0.1
      ```

4. **Web Push(Server Sent Events)**

   클라이언트의 요청없이 서버가 클라이언트에게 메시지를 전송하는 기술

   1. service.WebServicePush 생성하고 작성

      ```java
      package com.gyumin.websocket0310.service;

      import jakarta.servlet.http.HttpServletRequest;
      import jakarta.servlet.http.HttpServletResponse;
      import org.springframework.stereotype.Service;

      import java.io.PrintWriter;
      import java.util.Random;

      @Service
      public class WebPushService {
          public void push(HttpServletRequest request, HttpServletResponse response) {
              PrintWriter pw = null;
              try {
                  response.setContentType("text/event-stream");
                  response.setCharacterEncoding("UTF-8");
                  pw = response.getWriter();
                  Random random = new Random();
                  pw.write("data: " + (random.nextInt(46) + 1) + "\n\n");
                  Thread.sleep(5000);
              } catch (Exception e) {
                  System.out.println(e.getLocalizedMessage());
              } finally {
                  pw.close();
              }
          }
      }
      ```

   2. Controller 클래스에 push 서비스를 등록(추가)

      ```java
      package com.gyumin.websocket0310.Controller;

      import com.gyumin.websocket0310.service.WebPushService;
      import jakarta.servlet.http.HttpServletRequest;
      import jakarta.servlet.http.HttpServletResponse;
      import lombok.RequiredArgsConstructor;
      import org.springframework.stereotype.Controller;
      import org.springframework.web.bind.annotation.GetMapping;

      @Controller
      @RequiredArgsConstructor
      public class ChatController {
          @GetMapping("/")
          public String chat() {
              return "chatting";
          }

          private final WebPushService webPushService;

          @GetMapping("push")
          public void push(HttpServletRequest request, HttpServletResponse response) {
              webPushService.push(request, response);
          }
      }
      ```

   3. chatting.html 파일에 WebPush를 위한 코드를 추가
   4. 로컬에서 실행해보고 도커 빌드
   5. React에서 Dockerfile

      ```docker
      FROM node:18
      COPY package.json ./
      RUN npm install
      COPY ./ ./
      EXPOSE 3000
      CMD ["npm", "start"]
      =>18은 node 버전이고 3000은 포트번호
      ```

   6. Web Push는 별도의 프로젝트를 만들어서 사용하는 경우 CORS나 Proxy를 설정해주어야 한다.

      리액트를 사용하면 안될 것이기 때문에 Proxy나 CORS를 설정해야 한다!

5. **Proxy Server**
   - 내부의 요청을 처리해주는 서버
   - 외부에 존재하는 데이터를 요청해야하는 경우 내부에서 외부로 직접 요청하지 않고 Proxy Server에게 요청을 하면 Proxy Server가 외부에 요청을 해서 데이터를 전달해주는 방식
   - 보안 떄문에 사용하는 경우가 대부분이고 ajax나 fetch API는 도메인이 다르면 데이터를 요청할 수 없기 때문에 Proxy를 사용한다.
     OpenAPI를 ajax로 바로 가져오려고 하면 CORS 에러가 날 수 있다.
   - Java 에서는 HttpUrlConnection을 이용해서 가져올 수 있다. 근데 Spring Boot 에서는 내부적으로 얘를 쓰는데 다른 애가 있는데 **이렇게 스레드(core-api)를 이용해보고 Spring Boot를 써보자!!**
     Spring 에서는 두 가지 메서드를 제공한다. `RestTemplate`, `WebClient API` 를 제공한다.
     `RestTemplate` 은 blocking 방식의 API이고 `Web Client` 는 non-blocking 방식의 API이다.
     `WebClient` 를 사용하려면 `Web Flux` 를 전부 import해야 한다.
