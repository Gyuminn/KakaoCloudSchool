# [Java] Cookie & Session & Filter

1. **Cookie & Session**
   - http나 https는 상태가 없음
   - 클라이언트가 서버에게 request를 전송하고 response를 받으면 연결이 끊어진다. 이전에 무슨 작업을 했는지 알 수 없다.
   - 응답이 전송된 이후에도 데이터를 저장해서 상태 유지를 하기 위해서 저장소의 개념이 필요
   - Cookie
     클라이언트의 웹 브라우저에 저장한 후 동일한 도메인에서 요청을 전송하면 매번 서버에게 전송되는 데이터
   - Session
     Cookie에 발급되는 키를 가지고 서버에 저장해서 사용하는 데이터
2. **Cookie**

   1. 웹 서버와 클라이언트가 정보를 주고받는 방법 중의 하나
   2. 클라이언트에 파일의 형태로 존재하기 때문에 임의 조작이,,,
   3. 구성
      - name
        구별하기 위해 사용하는 이름 - 중복되면 쿠키의 내용이 변경
      - value
        저장되는 값으로 문자열만 가능
      - 유효시간
      - 도메인
        사용 가능한 도메인으로 기본은 자신의 도메인
      - 경로
        도메인 전체가 되는데 일부분으로 한정 가능
   4. Java에서 쿠키 생성
      - 생성
        `new Cookie(name, value)`
      - 저장
        `response.addCookie(Cookie 객체)`
        value는 반드시 인코딩된 채로 저장해야 한다.
   5. Cookie 클래스의 메서드

      구성을 get하고 set 메서드 제공

      쿠키를 삭제하는 메서드는 별도로 없고 유효시간을 과거로 되돌리는 방식으로 삭제

   6. 자바에서 모든 쿠키 가져오기

      `Cookie[] reqest.getCookie[]`

3. **실습을 위한 프로젝트 생성**

   1. Maven Web Project 생성

      IntelliJ에서는 Maven을 선택해서 Web Project를 생성

   2. servlet과 jsp에 대한 의존성 설정
      - maven은 외부 라이브러리에 대한 의존성을 repositories와 dependencies에 한다.
        - repositories
          저장소 위치로 설정하지 않으면 maven 중앙 저장소가 된다.
          설정을 해야하는 경우는 oracle처럼 open source를 하지 않는 라이브러리를 직접 다운받아야 할 때 이다.
        - dependencies
          실제 다운로드 받을 라이브러리
      - 라이브러리 검색
        www.mvnrepository.com

4. **쿠키 생성 및 읽기**

   1. 쿠키 생성 - createCookie.jsp

      ```java
      <%@ page import="java.net.URLEncoder" %><%--
        Created by IntelliJ IDEA.
        User: gimgyumin
        Date: 2022/12/30
        Time: 9:37
        To change this template use File | Settings | File Templates.
      --%>
      <%@ page contentType="text/html;charset=UTF-8" language="java" %>
      <html>
      <head>
          <title>쿠키 생성</title>
      </head>
      <body>
        <%
          // 쿠키 생성 및 저장
          // value가 한글인 경우 인코딩을 해주어야 한다.
          Cookie cookie = new Cookie("name", URLEncoder.encode("김규민", "UTF-8"));
          response.addCookie(cookie);
        %>
        <a href="viewcookies.jsp" >쿠키 보기</a>
      </body>
      </html>
      ```

   2. 쿠키 읽기 - viewcookies.jsp

      ```java
      <%@ page import="java.net.URLDecoder" %><%--
        Created by IntelliJ IDEA.
        User: gimgyumin
        Date: 2022/12/30
        Time: 9:46
        To change this template use File | Settings | File Templates.
      --%>
      <%@ page contentType="text/html;charset=UTF-8" language="java" %>
      <html>
      <head>
          <title>Title</title>
      </head>
      <body>
      <%
        // 모든 쿠키 전부 가져오기
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies) {
          String value = URLDecoder.decode(cookie.getValue(), "UTF-8");
      %>
        <%=cookie.getName()%>:
        <%=value%>
        <br />
      <%
        }
      %>
      </body>
      </html>
      ```

   3. javascript 코드 추가 - createCookie.jsp

      ```jsx
      ...
      <script>
          // 유효 시간 설정
          let expire = new Date();
          expire.setDate(expire.getDate() + 60 * 60 * 24);
          // 쿠키 모양 생성
          let cookie = "nickname" + "=" + encodeURI("박규민") + "; path=/";
          // 유효 시간 설정
          cookie += ";expires=" + expire.toGMTString() + ";";
          document.cookie = cookie;
      </script>
      ```

   4. javascript 코드 추가 - viewcookies.jsp

      ```jsx
      ...
      <script>
          // 모든 쿠키 읽기
          let cookieData = document.cookie;
          let start = cookieData.indexOf("nickname");
          let cValue = '';
          if(start != -1) {
              start += "nickname".length;
              let end = cookieData.indexOf(";", start);
              if(end === -1) {
                  end = cookieData.length;
              }
              console.log(decodeURI(cookieData.substring(start, end)));
          } else {
              console.log("존재하지 않는 쿠키입니다");
          }
      </script>
      ```

   5. 쿠키 삭제를 위한 deletecookie.jsp 파일을 만들어서 작성

      ```jsx
      <%--
        Created by IntelliJ IDEA.
        User: gimgyumin
        Date: 2022/12/30
        Time: 10:31
        To change this template use File | Settings | File Templates.
      --%>
      <%@ page contentType="text/html;charset=UTF-8" language="java" %>
      <html>
      <head>
          <title>쿠키 삭제</title>
      </head>
      <body>
      <%
        // name 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies) {
          if (cookie.getName().equals("name")) {
            Cookie cookie1 = new Cookie("name", "");
            cookie1.setMaxAge(0);
            response.addCookie(cookie1);
          }
        }
      %>
      </body>
      </html>
      ```

5. **Web Storage**

   1. web storage

      브라우저에 저장 가능한 HTML5 API

   2. 쿠키와 다른 점

      크기가 제한 없음.

      데이터를 서버로 보내지 않음.

      자바스크립트 객체를 저장할 수 있음.

   3. 종류
      - **localStorage**
        삭제하지 않는 이상 소멸되지 않음.
      - **sessionStorage**
        세션과 수명을 같이하는 스토리지
   4. 속성과 메서드
      - length
      - key(index)
      - getItem(key)
      - setItem(key, data)
      - removeItem(key)
      - clear
   5. 아이디를 로컬에 저장했다가 다음에 다시 접속할 때 출력 - 쿠키를 사용하지 않으므로 html이나 jsp나 상관없음.

      ```jsx
      <%--
        Created by IntelliJ IDEA.
        User: gimgyumin
        Date: 2022/12/30
        Time: 11:02
        To change this template use File | Settings | File Templates.
      --%>
      <%@ page contentType="text/html;charset=UTF-8" language="java" %>
      <html>
      <head>
          <title>Title</title>
      </head>
      <body>
      <form action="login.jsp" id="loginform">
          아이디<input type="text" id="id" name="id" required/><br/>
          <input type="checkbox" value="check" id="idsave"/>
          <input type="submit" value="로그인"/>
      </form>
      </body>
      <script>
          let idsave = document.getElementById("idsave");
          let ids = document.getElementById("id");
          let loginform = document.getElementById("loginform");

          // 처음 로딩될 떄 ids 존재 여부를 확인해서 작업
          window.addEventListener("load", (e) => {
              if (typeof localStorage.ids != 'undefined') {
                  ids.value = localStorage.ids;
                  idsave.checked = true;
              }
          })

          // 폼의 데이터 전송할 때 idsave를 확인해서 아이디를 저장
          loginform.addEventListener("submit", (e) => {
              if (idsave.checked === true) {
                  localStorage.ids = ids.value;
              } else {
                  localStorage.clear();
              }
          })
      </script>
      </html>
      ```

6. **Session**

   접속한 브라우저에 대한 정보를 웹 컨테이너에 저장하는 객체

   웹 컨테이너는 하나의 웹 브라우저에 하나의 Session을 할당

   1. 세션 사용
      - jsp
        `<%@ page session="true" %>` 를 작성하거나 생략하면 session이 만들어지고 false로 설정하면 session이 만들어지지 않음.
        Spring Legacy Project로 Spring MVC Project를 만들면 index.jsp에 세션을 사용하지 않는다는 옵션이 설정되어 있으므로 주의.
      - servlet
        `request 객체`를 가지고 `getSession()` 을 호출하면 없을 시 생성하고 있을 시 존재하는 세션을 리턴한다.
   2. 세션의 데이터 활용
      - setAttribute(String name, Obejct value)
      - Object getAttribute(String name)
      - void removeAttribute(String name)
   3. 메서드

      - String getId()
        id 리턴
      - long getCreationTime()
        세션이 만들어진 시간 리턴
      - long getLastAccessedTime()
        마지막 사용 시간
      - void setMaxInactiveInterval(int seconds)
        세션을 사용하지 않았을 때 유지하는 시간으로 초 단위
      - long getMaxInactiveInterval()
      - invalidate()
        세션 초기화

      유휴 시간 설정은 web.xml 파일에서도 가능한데 이 경우는 분단위

      ```jsx
      <session-config>
      	<session-timeout>시간</sesssion-timeout>
      </session-config>
      ```

   4. 세션 사용

      **redirect하더라도 정보를 유지하기 위해서 사용하는데 대표적인 경우가 로그인 정보를 저장해서 한 번 로그인을 하면 브라우저를 종료할 떄까지 또는 일정시간 동안 조작하지 않으면 로그아웃될 목적으로 이용**

      접속자가 아주 많은 경우는 세션이 서버의 메모리를 사용하기 때문에 서버의 속도가 나빠질 수 있다. 따라서 세션에 저장해야 하는 정보를 데이터베이스나 파일에 저장하기도 한다.

7. **Filter**

   Controller가 요청을 처리하기 전이나 후에 동작하는 Java EE의 객체

   Spring에서는 Interceptor 또는 AOP로 부른다

   Filter 인터페이스의 메서드를 구현한 클래스를 생성하고 URL Pattern을 등록하면 패턴에 해당하는 요청이 왔을 때 동작

   1. Filter 인터페이스
      - doFilter
        요청이 왔을 때 호출되는 메서드인데 3개의 매개변수로 request, response, chain이다.
        `chain.doFilter(request, response)`를 전송하면 원래 해야 하는 동작을 수행한다.
        이 메서드 앞에서 Controllor가 처리하기 전에 수행할 내용을 작성하고 이 메서드 뒤에는 Controller가 처리한 후에 수행할 동작을 설정한다.
        Controller가 요청을 처리하기 전에 수행할 내용은 유효성 검사나 로그 기록 등이다.
        유효성 검사를 해서 통과하지 못하면 되돌리거나 다른 곳으로 이동시킨다.
        요청을 처리한 후에 하는 것은 로그 기록이나 데이터의 변환 작업
      - init
      - destroy
   2. 사용

      전체 요청에 대해서 인코딩 설정을 할 때 많이 사용
