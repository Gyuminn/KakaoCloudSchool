# [Java] Web Programming

1. **Web Service 종류**
   1. 정적 Web Service
      - HTML, CSS, Javascript만으로 구성한 Web Service
      - 3가지 기술은 Web Browser 외부로 나가지를 못한다.
   2. CGI(Common Gateway Interface)
      - 동적인 방식으로 동작할 수 있는 Web Service
      - 클라이언트가 요청을 하게 되면 프로그램을 실행시켜서(프로세스 생성) 응답을 하는 방식
      - 운영체제에 종속적이 되고 코드의 어떤 부분이라도 변경을 하게 되면 다시 컴파일해서 실행시켜야 한다.
   3. **Application Server**
      - Web Server가 Application Server에게 요청해서 Apllication Server가 작업을 수행한 후 결과를 Web Server에게 전달해서 Client에게 전송하는 방식
      - Application Server는 Web Container 위에서 동작하는데 요청이 오면 Thread를 생성해서 처리
      - Java(JSP), C#(ASP.net), PHP, Ruby, Python, Go 등으로 구현
        최근에는 이러한 언어를 이용해서 구현하는 것이 쉽지 않기 때문에 Framework를 이용해서 구현하는 형태로 개발
        Java → Spring, PHP → laravel, Javascript → node(express, next 등), Python → Flask, Django
      - 구조
        사용자 브라우저나 애플리케이션 ↔ 웹 서버(아파치, IIS, Linux 등) ↔ Web Container(Tomcat, WebLogic, Jboss, Zetty, Zeus 등) ↔ 애플리케이션 서버 ↔ 저장소
2. **Java Web Component**
   1. 정적 컴포넌트: HTML, CSS, JavaScript
   2. Http**Servlet**
      - URL과 매핑이 되는 Java EE의 클래스
      - HTML 출력을 생성하는 것이 가능한데 자바 코드 안에 HTML을 삽입하는 구조라서 출력물을 직접 생성하는 것이 어려움.
      - 실행되면 컴파일이 되서 클래스로 만들어진 후 인스턴스를 생성해서 Web Container에 상주
      - 소스 코드를 수정하면 컴파일을 다시해서 Web Container에 다시 적재를 해야 한다.
   3. **JSP(Java Server Pages)**
      - HttpServlet의 단점을 보완하기 위해서 등장
      - 스크립트 형태로 HTML 안에 자바 코드를 삽입할 수 있는 구조
      - 출력물을 작성하는 것이 쉬움.
      - 요청이 오면 Servlet 클래스로 변환된 후 인스턴스를 생성해서 요청을 처리하고 파기됨
      - 소스 코드를 수정하더라도 다시 적재할 필요가 없음.
      - JSP에서 서버의 데이터를 출력하고자 하면 자바의 구문을 이용하거나 EL과 JSTL이라는 것을 이용하게 되는데 최근에는 이러한 부분을 하나로 만든 Template Engine을 이용하거나 Front End Application을 별도로 만드는 경우가 많아서 사용 빈도가 낮음.
   4. Web Container
      - 클라이언트의 요청이 Web Server에게 전달되서 컴포넌트를 호출할 때 이 컴포넌트들을 관리하는 무엇인가가 필요한데 이것이 Web Container
      - **Web Server와 Web Container의 역할을 동시에 할 수 있는 프로그램: Tomcat(무료)**, Zeus(국산 - 티맥스)
        Spring boot는 Tomcat을 내장하고 있기 때문에 따로 설치하지 않는 것이지 없어도 되는 것이 아니다.
3. **Java Web Programming 개발 환경**

   1. JDK

      11 버전 이상 설치 권장 - https, 최신 eclipse, **Spring이 11버전에 최적화**

      path 설정은 필수는 아닌데 설정을 하지 않으면 불편함.(Mac에서는 할 필요 없다.)

      확인은 javac version이라는 명령으로 가능.

   2. Web Container 설치
      - **Tomcat** 설치 : [https://tomcat.apache.org](https://tomcat.apache.org)
      - 압축된 파일을 다운로드하여 압축 해제
      - Oracle을 사용하는 경우는 포트 충돌(8080)이 발생할 수 있으므로 conf 디렉토리에 있는 sever.xml 파일을 수정 - port 부분이 원래는 8080인데 다른 번호로 수정
        <Connect pool port = ‘8080’ protocol=”HTTP/1.1” …> 어쩌구를 바꿔주면 된다.
   3. IDE
      - Eclipse의 경우는 Java EE 버전으로 설치
      - Intellij는 Ultimate 버전으로 설치
   4. 프로젝트 생성 및 실행 - IntelliJ
      - new Project → Jakarta EE → Application server에 tomcat 10.0.27과 같은 폴더 선택 → next → create
      - 한글 설정: [Help] - [Edit Custome VM Options] 실행한 후 하단에 추가
        - -Dfile.encoding=UTF-8
        - -Dconsole.encoding=UTF-8
      - tomcat 권한 부여
        터미널 → tomcat 디렉토리의 bin 폴더 → 명령어작성
        `chmod a+x 경로/apache-tomcat-10.0.27/bin/catalina.sh`
   5. JSP에서의 출력

      webapp 디렉토리를 선택하고 마우스 오른쪽을 눌러서 New → jsp 추가

4. **JSP**

   1. 기본 구조
      - HTML 위에 아래 부분이 추가됨
        `<%@ *page contentType*="text/html;charset=UTF-8" *language*="java" %>`
      - 자바 코드가 있으면 자바 코드를 전부 실행한 후 그 결과를 HTML과 합쳐서 출력을 생성
   2. 실행 원리

      요청 → JSP 페이지 코드 → 서블릿 클래스의 소스 코드로 변환 → 서블릿 클래스의 클래스 생성 → 서블릿 클래스의 인스턴스 생성 → 처리해서 출력하고 소멸

   3. 구성 요소
      - Directive
        지시자로 설정에 관련된 내용
        `<%@ 디렉티브이름 속성 = 값, 속성 = 값 의 형태로 작성 %>`
      - 스크립트 요소
        `<% 자바 코드 % >`
        `<%= 자바코드가 만든 데이터 %>`
   4. 내장 객체

      jsp에서는 내장이고 servlet에서는 doGet이나 doPost에서 매개변수로 전달된 request나 response를 이용해서 생성해서 사용하고 Spring Controller에서는 request, response, session까지는 메서드의 배개변수로 생성이 가능하지만 그 이외의 것은 직접 생성해서 사용

      - HTTPServletRequest request
        - 클라이언트의 요청 정보를 가진 객체
        - 메서드
          - getRemoteAddr()
            클라이언트의 IP
          - getMethod()
            요청 방식
          - getRequestURI()
            요청한 URL
          - getQueryString()
            URL 부분 중에서 query string
          - getContextPath()
            루트로부터의 경로
          - getParameter(String name)
            name에 해당하는 파라미터 읽어오기
          - getParameterValues(String name)
            name에 해당하는 파라미터를 배열로 읽어오기
          - getHeader(s)(String name)
            헤더 읽기로 설정하지 않아도 브라우저나 운영체제 종류들이 전달.
          - getHeaderNames()
            모든 헤더의 이름을 접근할 수 있는 Enumeration을 리턴
        - 파라미터 전송 방식
          - get
            URL 뒤에 ?를 추가하고 파라미터의 이름과 값의 형태로 전송
            파라미터가 URL에 query string의 형태로 포함되어 있는 것
            파라미터 길이에 제한(128자)
            자동 재전송
            파라미터는 반드시 인코딩 되서 전송되어야 한다.
            서버에서는 Web Container가 인코딩을 수행한다.
            최근의 Web Container는 전부 UTF-8을 사용
            조회 작업에 주로 이용
          - post
            파라미터를 본문에 숨겨서 전송
            길이에 제한 없음
            자동 재전송 기능이 없음
            인코딩을 서버 애플리케이션에서 수행(request.setCharacterEncoding(”UTF-8”) - 대부분 필터로 해결
            예전에는 get이 아니면 무조건 POST였는데 최근에는 삽입 작업에만 POST를 사용하는 것을 권장
          - put
            post와 동일하게 동작하는데 전체 수정에 사용
          - patch
            post와 동일하게 동작하는데 부분 수정에 사용, 멱등성이 없다라고 표현
          - delete
            get과 동일하게 동작하는데 삭제에 이용
          - head
            리소스를 GET 방식으로 요청했을 때 돌아올 헤더를 요청
          - options
            통신 옵션
          - connect
            양방향 연결 시도 - SSL이나 Web Socket에서 사용
          - trace
            원격지 서버에 Loopback 메시지를 호출하기 위해서 사용
      - HttpServletResponse response

        - 웹 브라우저(클라이언트)에게 응답을 보내는 응답 정보를 가진 객체
        - 뷰를 직접 만들 떄나 **헤더를 추가**하고자 때 사용
        - 메서드

          - addDateHeader(String name, long value)
            시간은 epoch time에 해당하는 정수로 절정
          - addHeader(String name, String value)
          - setDateHeader, setHeader도 존재
          - Data Caching
            브라우저는 동일한 요청을 보낼 때 빠르게 출력하기 위해서 캐시에 저장된 데이터를 사용할 수 있고 이것을 캐싱이라고 함.
            캐싱을 사용하지 못하도록 하기 - 브라우저별로 다르게 설정

            ```java
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-control", "no-cache");
            response.addHeader("Cache-control", "no-cache");
            response.setDateHEader("Expires", 1L);

            // 반드시 새로 고침을 해야 함.
            ```

          - redirect
            response.redirect(String url) ⇒ url로 redirect
            forwarding을 하게 되면 이전 요청이 계속 이어지기 때문에 reqeust 객체의 내용이 유지가 되고 URL이 변경되지 않는다. 새로 고침을 하게 되면 이전 요청을 다시 수행한다. 도멩인 내에서만 이동이 가능함.
            redirect를 하게 되면 이전 요청이 종료되기 때문에 새로운 request 객체가 만들어지고 URL이 변경된다. 새로 고침을 하게 되면 결과만 다시 출력된다. 도메인에 상관없이 이동 가능이 가능함. **redirect를 할 때 데이터를 전달하고자 하면 session 객체를 이용해야 함.**
            데이터 조회 작업은 fowarding을 하고 그 이외의 작업은 redirect를 한다.

      - HttpSession session
        request 객체의 getSession이라는 메서드로 생성하거나 가져올 수 있음.
        접속한 브라우저에 대한 정보
      - PageContext pageContext
        페이지 내에서 사용 가능한 데이터 저장 객체
      - ServletContext application
        서버 객체 - 모든 클라이언트가 공유
      - web.xml
        웹 애플리케이션의 설정 파일로 WEB-INF 디렉토리에 위치해야 함.
        웹 애플리케이션이 실행되면 가장 먼저 읽어서 설정을 수행하는 파일
        필터, 서블릿, 웹 애플리케이션 정보, 세션 설정, 에러 페이지 설정, 시작 요청 설정, 초기화 파라미터 설정 등을 수행
      - application
        서버 객체
        여기에 데이터를 저장하면 모든 클라이언트가 데이터를 공유할 수 있음.
        초기 설정을 읽는데도 사용
      - 3개의 저장 객체
        뷰에서 데이터를 전달할 때 사용하는 3개의 객체 - 서버에서 뷰를 렌더링할 때 사용
        - request
          하나의 요청에 해당하는 데이터를 저장 - redirect를 하면 새로 만들어짐.
        - session
          하나의 브라우저에 해당하는 데이터를 저장 - 접속을 해제하지 않으면 유지됨.
        - application
          모든 클라이언트가 공유 - 서버를 재시작해야만 초기화된다.
          데이터 사용 메서드
        - void setAttribute(String name, Object value)
          데이터 저장
        - Object getAttribute(String name)
          데이터 읽기인데 Object 타입으로 리턴되므로 강제 형 변환을 해서 사용
        - removeAttribute(String name)
          데이터 삭제

   5. 이동
      - forwarding
        - 태그 이용
          `<jsp:foward page="이동할페이지 - 결과를 출력할 페이지">`
        - 코드 이용
          `request.getRequestDispatcher(”페이지 경로”).forward(request, response)`
          Spring에서 기본적으로 fowarding
      - redirect
        `response.sendRedirect(”경로”);`
   6. **forwarding 실습**

      - input.jsp: 요청
        ```java
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <html>
        <head>
            <title>요청</title>
        </head>
        <body>
          <form action="process.jsp" method="get">
            숫자 <input type="number" name="first" /><br/>
            숫자 <input type="number" name="second" /><br/>
            <input type="submit" value="계산" />
          </form>
        </body>
        </html>
        ```
      - process.jsp: 처리 - 이 부분은 나중에 Servlet(**Controller**)과 Java POJO(다른 프레임워크의 클래스로부터 상속받지 않은) Class(**Model**)로 변환

        ```java
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <%
          // 파라미터를 읽기
          String first = request.getParameter("first");
          String second = request.getParameter("second");

          // 처리 수행
          int result = Integer.parseInt(first) + Integer.parseInt(second);

          // 결과 저장
          request.setAttribute("result", result);
          session.setAttribute("result", result);
          application.setAttribute("result", result);

          // 결과 페이지로 이동
          System.out.println("처리페이지");

          // 포워딩
          request.getRequestDispatcher("output.jsp").forward(request, response);
        %>
        ```

      - output:jsp: 결과(View) - 이 부분은 별도의 Application으로 만들기도 한다.
        ```java
        <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <html>
        <head>
            <title>결과 출력</title>
        </head>
        <body>
          request 객체: <%= request.getAttribute("result")%>
          <br />
          session 객체: <%= session.getAttribute("result")%>
          <br />
          application 객체: <%= application.getAttribute("result")%>
          <br />
        </body>
        </html>
        ```
      - **포워딩 결과**
        실행한 후 input.usp로 이동해서 숫자 2개를 입력하고 계산을 클릭.
        포워딩으로 이동해서 request, session, application 모두 데이터를 읽어낼 수 있음.
        URL을 확인해보면 결과는 output.jsp 내용이 출력되지만 process.jsp이다.
        새로고침을 해보면 process.jsp를 다시 수행하는 것을 알 수 있다.
      - process.jsp 수정
        ```java
        ...
        // 리다이렉트
        response.sendRedirect("output.jsp");
        ```
      - **리다이렉트 결과**
        request 객체의 내용은 출력되지 않음.
        URL은 결과 페이지인 output.jsp
        새로고침을 하게 되면 output.jsp만 다시 출력됨.
      - 조회의 경우는 forwarding이 일반적인데 forwarding을 하게 되면 처리 작업을 다시 수행핸다는 것을 기억해야 한다.
        로그인 같은 경우는 ID와 Password를 찾는 조회 작업이지만 forward하지 않는다.

5. **Servlet**

   1. Model1과 Model2
      - Model1은 모든 로직을 jsp에 작성하는 것
      - Model2는 요청과 출력은 jsp가 담당하고 처리 부분은 Servlet이나 POJO Class가 담당하도록 하는 것.
      - jsp 파일에 java 코드와 출력하기 위한 태그가 같이 있으면 유지 보수가 어렵다라고 생각하기 때문이다.
   2. **MVC Pattern**
      - 처리하는 로직과 출력하는 부분을 분해
      - 요청과 출력을 위한 jsp 부분을 View라고 해서 별도로 만들고, 처리하는 부분을 Model이라고 해서 POJO Class로 만들고 요청을 받아서 필요한 Model을 호출하고 그 결과를 받아서 View에게 전달하는 부분을 Controller(Servlet으로 생성) 라고 한다.
      - Model 부분을 일반 로직을 처리하는 부분(Service - Busniess Logic)과 데이터 영속성 관련 부분(Repository or DAO, Psers)으로 분해해서 작업하는 것이 일반적이다.
      - Front Controller 패턴
        모든 사용자의 요청을 먼저 받을 Front Controller를 별도로 두고 그 이후 각 요청을 처리할 Page Controller를 만드는 패턴
      - Rest Controller
        Controller가 출력할 뷰를 직접 생성하지 않고 데이터만 전달하도록 만든 것
   3. **url pattern**
      - 서블릿을 등록할 때 매핑시키는 url을 설정하는 방법
        - /경로
          경로와 매핑
        - /\*
          모든 경로와 매핑
        - /디렉토리/
          디렉토리가 포함된 모든 URL과 매핑
        - \*.확장자
          확장자로 끝나는 모든 URL과 매핑
        - /
          .jsp를 제외한 모든 URL과 매핑
          디렉토리 패턴과 확장자 패턴은 같이 적용할 수 없음.
          예전에는 확장자 패턴이 많이 사용되었는데 최근에는 디렉토리 패턴이 많이 사용됨.
   4. 이전 프로젝트를 MVC 구조로 변경 - Repository는 없음

      - 사용자의 요청을 처리할 메서드의 원형을 가진 Service 인터페이스를 생성

        ```java
        package com.example.webbasic;

        // 사용자의 요청을 처리할 메서드의 원형을 가진 인터페이스
        public interface PageService {
            // 2개의 정수를 받아서 합계를 구한 후 리턴하는 메서드
            public int add(int first, int second);
        }
        ```

      - 사용자의 요청을 처리할 메서드를 구현한 클래스를 생성

        ```java
        package com.example.webbasic;

        public class PageServiceImpl implements PageService {
            // java이면 싱글톤 패턴 작성 코드를 추가 - Spring에서는 불필요 - 생략하자

            @Override
            public int add(int first, int second) {
                return first + second;
            }

        }
        ```

      - process.jsp를 처리할 수 있는 Controller를 생성

        ```java
        package com.example.webbasic;

        import jakarta.servlet.*;
        import jakarta.servlet.http.*;
        import jakarta.servlet.annotation.*;

        import java.io.IOException;

        @WebServlet(name = "PageController", value = "/process.jsp")
        public class PageController extends HttpServlet {
            // 서비스에 대한 참조 변수
            private PageService pageService;

            public PageController() {
                // 생성자에서 서비스 생성 - 나중에는 주입을 받음
                pageService = new PageServiceImpl();
            }
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                // 파라미터 읽기
                String first = request.getParameter("first");
                String second = request.getParameter("second");

                // 서비스 메서드 호출
                // 파라미너트이 자료형 변환은 Service에서 수행해도 된다.
                // Spring은 일반적으로 Controller에서 한다.
                int result = pageService.add(Integer.parseInt(first), Integer.parseInt(second));

                // 결과를 저장
                request.setAttribute("result", result);
                // 세션이 여기서는 없기 때문에 만들어서 써야한다.
                request.getSession().setAttribute("result", result);
                request.getServletContext().setAttribute("result", result);

                // 뷰 페이지 결정하고 데이터를 전달
                response.sendRedirect("output.jsp");

            }

            @Override
            protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            }
        }
        ```

      - process.jsp를 삭제
      - 자바 코드를 작성한 경우는 서버를 재실행 해야 한다.(새로고침으로 안됨.)
      - 서버를 실행하고 input.jsp로 이동한 후 이전과 동일한 작업 수행

6. **Listener**

   Listener는 이벤트가 발생했을 때 호출되는 메서드를 소유한 인스턴스

   Java EE에서는 기본적으로 ServletContextListener와 HttpSessionListener 인터페이스를 제공

   ServletContextListener는 웹 애플리케이션이 시작될 때와 종료될 때 호출되는 메서드를 소유

   HttpSessionListener는 클라이언트가 접속할 때와 접속을 해제할 때 호출되는 메서드를 소유

   HttpSessionListener는 클라이언트가 정상적으로 접속을 해제할 때 메서드를 호출하기 때문에 클라이언트가 비정상적으로 접속을 해제하면 감지를 못함.

   비정상적 접속 종료도 감지하기 위해서는 웹 페이지에서 window 객체의 beforeunload 이벤트가 발생할 때 서버에 요청을 전송하도록 해야 한다.

   새로 고침(f5 - 116)을 할 때도 beforeunload 이벤트가 발생하게 되는데 이 경우는 아무 일도 하지 않도록 해야 한다

   Listener를 만들면 등록을 ㅐ야 하는데 web.xml 파일에 한다

   1. ServletContextListener 생성

      디렉토리에서 마우스 오른쪽을 눌러서 [New] - [ Listener]를 선택하고 클래스 이름을 입력(이름 무의미)한 후 ServletContextListner를 선택

   2. SessionListener 생성

      디렉토리에서 마우스 오른쪽을 눌러서 [New] - [ Listener]를 선택하고 클래스 이름을 입력(이름 무의미)한 후 HttpSessionListener를 선택

   Intellij에서는 통합이 되어있음.

   ```java
   package com.example.webbasic;

   import jakarta.servlet.*;
   import jakarta.servlet.http.*;
   import jakarta.servlet.annotation.*;

   @WebListener
   public class ApplicationListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {
       // 접속자 수를 저장
       private static int count;

       public static int getCount() {
           return count;
       }

       public ApplicationListener() {
       }

       // 웹 서버가 구동될 때 호출되는 메서드
       @Override
       public void contextInitialized(ServletContextEvent sce) {
           /* This method is called when the servlet context is initialized(when the Web application is deployed). */
           System.out.println("웹 서버 시작");
       }

       // 웹 서버가 종료될 때 호출되는 메서드
       @Override
       public void contextDestroyed(ServletContextEvent sce) {
           /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
           System.out.println("웹 서버 종료");
       }

       // 세션이 만들어 질 떄 - 새로운 접속이 온 경우
       @Override
       public void sessionCreated(HttpSessionEvent se) {
           /* Session is created. */
           count++;
           System.out.println("접속자 수: " + count);

           HttpSession session = se.getSession();
           System.out.println("세션 아이디: " + session.getId());
       }

       @Override
       public void sessionDestroyed(HttpSessionEvent se) {
           /* Session is destroyed. */
       }

       @Override
       public void attributeAdded(HttpSessionBindingEvent sbe) {
           /* This method is called when an attribute is added to a session. */
       }

       @Override
       public void attributeRemoved(HttpSessionBindingEvent sbe) {
           /* This method is called when an attribute is removed from a session. */
       }

       @Override
       public void attributeReplaced(HttpSessionBindingEvent sbe) {
           /* This method is called when an attribute is replaced in a session. */
       }
   }
   ```
