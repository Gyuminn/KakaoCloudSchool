# [Spring] MVC

1. **MVC Pattern**

   Presentaion Logic과 Business Logic을 분리해서 구현

   1. 구조
      - Model
        데이터 자체 또는 데이터를 처리하는 영역 - Persistence, Service
      - View
        결과 화면을 만들어내는 영역 - EL과 JSTL을 이용한 JSP, 템플릿 엔진, Front End Application 등으로 구현
      - Comtroller
        Model과 View 사이를 연결 - HttpServlet을 이용, Controller(JSP나 템플릿 엔진)와 RestController(Front End Application)
   2. 장점
      - 유지 보수가 쉬워짐
   3. 단점

      구조가 복잡

2. **Controller 계층**

   1. Front Controller
      - 모든 요청을 전부 받아들이는 Controller
      - 공통으로 처리할 Logic을 호출
      - 추적이나 보안을 적용
      - Spring에서는 DispatcherServlet이 이 역할을 담당(Spring Boot에서는 안보인다.)
   2. Page Controller

      특정 요청을 처리하기 위한 Controller

      Business Logic을 호출

   3. MVC 구조

      Request → FrontController → Controller → Service → DAO → 데이터베이스 프레임워크(MyBatis, Hibernate - JPA) → Database

      Front Controller가 Template Engine을 이용해서 결과 화면을 생성할 수 있음.

3. **Spring MVC에서 사용되는 Annotation**
   - Controller
     뷰를 선택하기 위한 Controller
   - RestController
     데이터를 전달하기 위한 Controller
   - Service
   - Repository
   - Component
     위 5개의 Annotaion은 component-san에 설정된 패키지에 만들면 bean을 자동 생성(즉, 인스턴스 만드는 일을 할 필요가 없다.)
   - @RequestMapping
     Controller에서 클라이언트가 요청한 URL과 처리할 메서드를 매핑하기 위한 어노테이션
   - @RequestParam
     파라미터 읽기 위한 어노테이션
   - @RequestHeader
   - @PathVariable
     URL의 일부분을 파라미터로 사용하는 경우
   - @CookieValue
   - @SessionAttribute
   - @ModelAttribute
   - @InitBinder
     파라미터를 모아서 하나의 객체로 만들고자 하는 경우
   - @ResponseBody
     리턴 타입을 HTTP의 응답 메시지로 사용하는 경우 - RestController에서 주로 이용
   - @RequestBody
     요청 문자열을 그대로 파라미터로 전달
4. **Spring MVC 용어**
   - DispatcherServelet
     Front Controller의 역할을 수행해주는 클래스, 자동 생성
   - HandlerMapping
     웹의 요청 URL과 Controller 클래스를 매핑시켜주는 클래스, 자동 생성
   - ModelAndView
     Controller에서 View 이름을 결정하고 데이터를 넘기고자 할 때 사용
   - ViewResolver
     Controller에서 넘겨준 View 이름을 가지고 실제 출력할 View를 결정하는 클래스
   - View
     결과 화면
5. **처리 흐름**

   클라이언트가 요청을 하면 DispatcherServlet 클래스가 요청을 받아서 알맞는 Controller가 있는지 확인하고 있으면 Controller에게 요청을 전달해서 처리를 하고 Controller가 리턴한 View 이름을 가지고 ViewResolver 설정을 확인해서 결과를 출력할 View를 결정해서 출력

6. **Spring MVC Project**

   STS3에서는 Legacy Project 메뉴에서 생성

   Eclipse나 STS4라면 직접 생성은 안되고 STS3 plugin을 설치해서 생성(이제 안되는 듯)

   STS3에서 만든 프로젝트를 가져와도 된다.

   STS3에서는 Java Web 메뉴가 없어서 Web Programming 하기 불편한 경우 Java EE plug in을 설치

   - 전자 정부 프레임워크와 AnyFramework 등은 이 프로젝트에 설정을 추가한 프로젝트를 생성

   1. web.xml

      ```java
      <?xml version="1.0" encoding="UTF-8"?>
      <web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee"
      	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee https://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

      	<!-- The definition of the Root Spring Container shared by all Servlets and Filters -->
      	<!-- applicationContext.xml의 경로를 변경 -->
      	<context-param>
      		<param-name>contextConfigLocation</param-name>
      		<param-value>/WEB-INF/spring/root-context.xml</param-value>
      	</context-param>

      	<!-- Creates the Spring Container shared by all Servlets and Filters -->
      	<!-- Listener 이벤트가 발생했을 때 동작할 객체로 Event Handler라고도 한다.
      	Web Application이 시작될 때와 종료될 때 아래 클래스의 인스턴스를 생성해서 행하는 것인데 기본 설정은
      	WEB-INF 디렉토리의 applicationContext.xml을 참조
      	-->
      	<listener>
      		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
      	</listener>

      	<!-- Processes application requests -->
      	<!-- FrontController 생성
      	WEB_INF/dispatcher-servelt.xml 파일을 FrontController로 사용하겠다는 의미인데
      	init-param을 이용해서 경로를 변경한 것
      	load-on-startup은 애플리케이션이 시작하자마자 서블릿 인스턴스를 생성하도록
      	-->
      	<servlet>
      		<servlet-name>appServlet</servlet-name>
      		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
      		<init-param>
      			<param-name>contextConfigLocation</param-name>
      			<param-value>/WEB-INF/spring/appServlet/servlet-context.xml</param-value>
      		</init-param>
      		<load-on-startup>1</load-on-startup>
      	</servlet>

      	<!-- 위에서 만든 서블릿의 URL 패턴 설정
      	/는 .jsp를 제외한 모든 요청을 처리 -->
      	<servlet-mapping>
      		<servlet-name>appServlet</servlet-name>
      		<url-pattern>/</url-pattern>
      	</servlet-mapping>

      </web-app>
      ```

   2. root-context.xml
      - 내용은 작성되어있지 않음.
      - 프로젝트가 실행될 때 읽는 설정 파일
      - 모든 곳에서 사용할 설정이나 bean을 생성
      - 일반적으로 데이터베이스 사용 설정을 한다.
   3. servlet-context.xml
      - .jsp를 제외한 모든 요청이 왔을 떄 처리하는 Front Controller

7. **Maven 기반의 Tomcat을 이용한 프로젝트 실행**

   순서

   pom.xml 파일을 읽어서 필요한 설정을 수행

   Tomcat의 server.xml 파일을 읽어서 Web Containter가 실행 - Tomcat은 9 버전을 권장

   Web Projcet 내의 web.xml 파일을 읽어서 웹 프로젝트 설정을 확인한 후 실행

   1. 톰캣 다운로드: [https://tomcat.apache.org/download-90.cgi](https://tomcat.apache.org/download-90.cgi) 에서 압축된 파일을 받아서 압축 해제
   2. [Run As] - [Run On Server]가 안나오는 경우

      Eclipse Market Place에서 Eclipse Enterprise Java를 설치한 후 수행

8. **기본 설정 변경**
   1. pom.xml 파일에서 자바 버전과 Spring 그리고 JUnit 버전 변경
   2. Build path에서 JRE 버전을 변경
   3. 프로젝트에 에러가 발생하면 프로젝트를 선택하고 마우스 오른쪽을 누른 후 [properties]를 선택하고 project facets에서
   4. pom.xml에 test를 위한 라이브러리와 lombok(데이터를 표현하는 클래스를 쉽게 만들 수 있도록 해주는 라이브러리) 라이브러리에 대한 의존성을 추가
9. **데이터 전달없이 페이지 이동 - forwarding**
10. **클라이언트가 전달한 데이터(Parameter) 읽기**

    1. URL에 전달한 데이터 읽기

       - 파라미터가 하나인 경우 최근에는 URL에 파라미터를 포함시켜 전송한다.
         상세보기나 삭제를 할 때 많이 사용

         ```java
         // URL: [https://host/member/10(데이터)](https://host/member/10(데이터)) 인 경우

         @RequestMapping(value="/member/{변수명}", method=GET 또는 POST)
         public String ??? (Model model, @PathVariable("변수명") 자로형 사용할 변수명)
         ```

         자로형은 기본적으로 String이지만 다른 자료형을 사용하면 강제 형 변환을 수행해서 대입한다.
         강제 형 변환에 실패하면 예외가 발생한다.

    2. Parameter 처리 방법
       - 요청 어리 메서드에 HttpServletRequest 타입의 매개변수를 설정해서 getParameter나 getParameterValues 메서드를 호출해서 읽어내기
       - `@RequestParam(”파라미터이름”) 자료형 이름` 을 매개변수로 추가해서 파라미터 이름에 해당하는 데이터를 형 변환해서 이름으로 받기
       - **파라미터와 동일한 이름을 사용하는 속성을 갖는 Command 클래스를 작성해서 사용 - 권장**
    3. 파라미터 인코딩 설정
       - get 방식은 파라미터를 전송할 때 인코딩을 설정해야 하고 post는
    4. 파라미터 받은 문자열을 [java.util.Date](http://java.util.Date) 타입으로 변환
       - Command 객체를 생성할 때 속성의 자료형을 Date로 설정하고 상단에 @DateTimeFormat(pattern = “서식”)
         ```java
         @DateTimeFormat(pattern="yyyy/MM/dd")
         private Date dueDate;
         ```
    5. @ModelAttribute
       - Controller의 요청 처리 메서드의 상단에 `@ModelAttribute(”이름”)`을 추가한 메서드는 리턴하는 값이 모든 View에게 전달된다.
         ```java
         @ModelAttribute("projectname")
         public String name() {
         	return "spring mvc";
         }
         ```

11. **Controller의 요청 처리 메서드**

    Controller를 생성하는 방법은 클래스 위에 `@Controller`, `@RestController`를 추가해주면 된다.

    1. URL 매핑
       - 클래스 위에 `@RequestMapping(”공통된 URL”)`를 기재하면 공통된 URL
       - 메서드 위에 `@RequestMapping(value=”공통된 URL”, method=ReqeustMethod.전송방식)`을 작성하는데 이렇게 하면 클래스 위에 있는 것과 합쳐져서 요청 URL을 결정
    2. 요청 처리 메서드 파라미터: 작성 순서는 상관없음.
       - HttpServletRequest, HttpServletResponse, HttpSession
       - Model
         뷰에 데이터를 전달하기 위해서 사용, 리다이렉트할 때는 데이터를 전달할 수 없음.
       - RedirectAttributes
         뷰에 데이터를 전달하는데 리다이렉트할 때 한 번만 사용할 데이터 전달
       - @RequestParam, @PathVariable, @RequestHeader
       - Command 객체
         파라미터를 받기 위해서 사용
       - Errors, BindingResult
         유효성 검사에 사용
    3. 리턴 타입
       - String
         Controller에서는 뷰 이름이 되고 RestController에서는 텍스트 데이터
       - void
         뷰를 직접 생성
       - View
         직접 생성한 뷰를 리턴
       - DTO, List, ResponseEntity
         RestController에서 json 리턴
    4. 뷰 이름 리턴 시 이동 방법

       뷰 이름만 리턴하면 ViewResolver와 결합해서 출력할 뷰를 결정하는데 이동 방법은 Forwarding

       redirect로 이동하고자 하는 경우에는 앞에 `redirect:요청경로` 를 기재하면 된느데 이 때는 ViewResolver 설정을 참조하지 않고 Controller로 이동한다. redirect할 때 작성하는 것은 뷰 이름이 아니다.

    5. View에 전달하는 방법
       - Model을 매개변수로 만들고 Model에 속성 이름과 데이터를 저장
       - HttpServeltRequest, HttpSession, ServletContext(application)을 이용해서 전달
       - redirect로 이동할 때 한 번만 데이터를 사용하는 경우는 Session을 이용하지 않고 RedirectAttributes를 이용
