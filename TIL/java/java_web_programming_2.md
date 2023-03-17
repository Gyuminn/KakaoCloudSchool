# [Java] Web Programming

1. **복습**
   - Java Web Programming
   - Spring Legacy MVC Project
   - Spring Boot
   - JPA
   - Communications
     - Web Socket
     - Web Push
     - Proxy
     - Mail Send
   - Security
   - Batch
2. **개요**

   1. Web Programming

      클라이언트가 웹 브라우저나 일반 앱에서 http나 https 형태의 프로톸로로 요청을 했을 때 서버가 응답을 전송하는 프로그래밍

   2. 구현 방식
      - 정적 구현
        - Web Client ↔ Web Server ↔ 정적 파일(HTML)
        - 처리 과정이 없고 보여지는 페이지만 전송하는 방식
        - Client가 요청한 URL을 가지고 필요한 자원을 찾아서 넘겨주는 소프트웨어가 Web Server
        - Web Server로 많이 사용되는 소프트웨어가 apache(스레드나 프로세스 형태로 동작), nginx(이벤트 기반 - 트래픽 예측이 비교적 쉬워서 최근에 많이 사용), iis 등
      - 동적 구현
        - Web Client ↔ Web Server ↔ Web Container(Web Application Server) ↔ Server Application ↔ 저장소
        - 클라이언트가 Web Server에게 요청을 전송하면 Web Server는 그 요청을 Web Container에게 전송하고 Web Container에 적절한 메서드를 호출해서 요청을 처리한 후 Web Server에게 응답을 전송하고 Web Client는 그 응답을 출력하거나 해석하는 사용 방식
        - We Contiainer는 Tomcat이 가장 많이 사용됨.
        - Web Container가 해주는 역할은 Server Application의 Instance 들을 관리하는 역할(수명주기를 관리)
        - Spring Legacy Project에서는 Tomcat WAS를 많이 사용했는데 Web Server와 통신이 문제가 되어서 Apache Tomcat이라는 소프트웨어를 이용해서 Web Server의 역할과 Web Container의 역할을 묶어버림.
        - node나 python의 django, spring boot는 별도의 소프트웨어를 설치하는 것도 내장으로 해결함.
        - 그럼에도 불구하고 nginx 같은 것을 별도로 설치하는 이유는 트래픽 제어 때문
      - 저장소
        - 사용 방법에 따른 분류
          - RDBMS
            관계형 데이터베이스
            테이블 기반
            엄격한 트랜잭션을 적용(금융)
            SQL을 사용
            프로그래밍 언어와 연동할 때는 SQL을 이용하는 방식과 ORM을 이용하는 방식
            아직도 가장 많이 사용되는 방식
            알려진 바로는 SQL을 이용하는 방식보다는 ORM을 이용하는 방식이 성능은 좋다고 하고 Running Curve가 길다고 한다.
            SQL을 이용하는 방식은 Read 작업을 제외하면 Lock을 걸어서 사용하기 때문에 동기화에 대해서 신경을 많이 쓰지 않아도 된다. (Client ↔ Database)
            ORM은 메모리에 데이터를 복사해두고 사용하는 방식이라서 여러 곳에서 동시에 작업을 수행하면 동기화 문제를 해결하는 것이 쉽지 않다. (Client ↔ Context ↔ Database, Client ↔ Context ↔ Database)
            외래키와 정규화를 알아야 한다고 한다.
            [A.id](http://A.id) = [B.id](http://B.id) → Full Table Scan
            [B.id](http://B.id)에 Index가 설정되어 있다면 Index(Primary Key나 Unique를 설정하면 자동 생성) Scan
          - NoSQL
            SQL 이외의 방식을 사용하는 모든 데이터베이스.
            이 방식은 대부분 스키마의 구조가 유연함.
            미리 스키마의 구조를 설계할 필요가 없음.
            현재 많이 사용되는 NoSQL들은 Key-Value 방식의 형태로 사용
            프로그래밍 언어에서 사용할 때 NoSQL에서 사용했던 함수를 그대로 사용하는 경우가 많음.
            최근에는 NoSQL도 ORM처럼 사용하는 방식도 등장.
            Mongo DB, Cassandra, Redis, Dynamo DB 등
          - 분산 파일 시스템
            Hadoop - 다양한 Echo System을 갖추고 있음.(잘 안씀)
        - 데이터베이스 위치에 따른 분류
          - Disk 기반
            데이터가 디스크에 존재하는 방식
            속도가 느림
            백업이나 동기화가 수월
          - In Memory 기반
            데이터가 메모리에 존재하는 방식 - 가장 많이 사용되는 데이터베이스: redis
            속도가 빠름
            비용이 많이 소모

3. **Java Web Programming**

   1. JSP와 HttpServlet
      - HttpServlet
        - Java EE Spec에서 제공하는 URL과 매핑 가능한 클래스
        - Java SE를 설치하고 Java Web Programming을 하고자 하는 경우에는 이 클래스를 가진 라이브러리를 프로젝트에 포함시켜야 한다.
          Tomcat과 같은 Web Container에 포함되어 있고 별도의 의존성 설정을 이용해서 추가한다.
        - Java 코드 안에 Java 코드 이외의 출력을 위한 HTML을 추가할 수 있다.
        - Java 코드 안에 HTML을 추가해야 하는 구조라서 출력이 어렵다.
        - 클라이언트의 자신이 처리할 수 있는 첫번째 요청이 오거나 설정에 따라 인스턴스를 생성하고 Container에 상주한 상태에서 클라이언트가 요청을 처리한다.
          동일한 요청이 여러 번 온다고 해서 다시 만들어지지 않고 Thread를 이용해서 처리한다.
          컨테이너 차원에서 싱글톤 패턴을 적용
          코드를 수정하면 애플리케이션을 다시 컴파일하고 build를 해야 수정한 내용이 적용된다.
          최근에는 WAS를 이용하거나 설정을 하면 수정과 동시에 컴파일과 빌드를 자동으로 해서 적용한다.
      - JSP(Java Server Pages)
        - HTML 코드 안에 Java 코드를 추가할 수 있는 스크립트
        - 해석을 서버가 한다.
        - Java Code를 해석해서 그 내용을 HTML 안에 끼워넣는 구조
        - 자신이 처리할 수 있는 클라이언트의 요청이 오면 HttpServlet 클래스로 변경된 후 인스턴스를 만들어서 클라이언트의 요청을 처리한 후 인스턴스를 소멸시킨다.
          클라이언트의 요청이 올 때마다 클래스와 인스턴스가 다시 만들어진다.
          코드를 수정한 후 요청만 다시 수정하면 수정한 내용이 적용이 된다.
   2. Model1과 Model2
      - Model1은 모든 코드를 JSP에 작성
      - Model2는 처리하는 코드는 HttpServlet과 Java Class에 작성하고 출력하는 코드는 JSP에 작성
   3. EL
      - 표현 언어
      - Server의 데이터를 JSP에 Java 코드를 사용하지 않고 출력하기 위한 언어
      - 템플릿 엔진의 시초
   4. Custom Tag
      - 사용자 정의 태그
      - 개발자가 만드는 태그
      - JSTL
        Apache 재단에서 만든 Custom Tag로 웹 프로그래밍에서 자주 사용하는 자바의 문법을 태그 형태로 사용할 수 있도록 한 라이브러리
   5. 내장 객체
      - HttpServletRequest
        클라이언트의 요청을 처리하기 위한 클래스
        JSP와 Servlet의 요청 처리 메서드의 매개변수
      - HttpServletResponse
        클라이언트에게 응답을 하기 위한 클래스
        JSP와 Servlet의 요청 처리 메서드의 매개변수로 만들어져서 제공
      - HttpSession
        클라이언트의 브라우저에 대한 정보를 저장하기 위한 클래스
        실제 브라우저 정보를 저장하는 것이 아니고 클라이언트와 매핑되는 브라우저를 사용하는 동안 정보를 유지하기 위한 클래스
        서버의 메모리에 저장이 된다.
        각 클라이언트와 세션을 매핑하기 위해서 클라이언트의 브라우저에 Cookie를 만들어서 키를 생성해 둔다.
        서버의 메모리에 너무 많은 내용을 저장하면 웹 서버가 느려질 수 있으므로 Session을 데이터베이스에 별도로 저장하는 방식이 등장했는데 세션은 자주 사용하는 데이터가 될 가능성이 높아서 최근에는 redis와 같은 In Memory Databsase에 저장하는 것을 권장한다.
        Java에서는 Session은 request.getSession으로 가져오는데 spring에서는 session을 내장 객체처럼 사용하는 것이 가능.
      - ServletContext
        Web Application Server와 매칭되는 클래스
        Applicaiton 객체라고 많이 한다.
        모든 클라이언트가 공유해야 하는 데이터를 설정하는데 사용한다.
      - Cookie
        내장 객체는 아니고 클라이언트와 서버가 통신하기 위해서 클라이언트의 브라우저에 데이터를 저장하고 클라이언트가 서버에게 어떤 요청을 하던 같이 전송되는 객체
        보통은 서버에서 생성하지만 클라이언트에서 생성하는 것도 가능하다.
        클라이언트에서 조작이 가능하기 때문에 보안이 중요한 항목을 저장하는 용도로는 사용하지 않는다.
        Cookie는 문자열만 저장할 수 있고 서버에게 무조건 전송되기 때문에 문자열 이외의 데이터를 저장하거나 서버에게 전송하지 않고 데이터를 유지하기 위해서 Local Storage(Local Storage, Session Storage, Web SQL, Indexed DB 등)의 개념이 도입됨.
        로컬 스토리지는 Javascript를 사용하기 때문에 문자열 이외의 데이터도 저장이 가능하고 필요할 때만 서버에게 전송이 가능하다.
   6. Filter & Listener
      - Filter
        클라이언트의 요청을 처리하기 전이나 후에 수행할 작업을 설정하는데 사용
        클라이언트의 요청을 처리하기 전이나 후는 Controller가 요청을 받기 전(Servlet이 요청을 처리하기 전)이나 Controller가 요청을 처리한 후(예외가 발생한 경우와 정상적으로 처리한 경우)
      - Listener - Event Handler
        이벤트가 발생했을 때 수행할 작업을 설정하는데 사용
        Web Application에서는 대부분 웹 애플리케이션의 시작과 종료 그리고 세션의 생성과 소멸 시점에 수행할 작업을 정의할 수 있는 Listener를 제공
   7. web.xml

      배포 지시자라고 번역을 하는데 Web Application에 대한 설정 파일

   8. Pattern
      - MVC Pattern
        Model, View, Controller로 나누어서 구현
        Model은 Service와 Repository 그리고 Entity 등이 있다.
        Controller는 Controller와 DTO 등이다.
      - Template Method Pattern
        - 클라이언트의 요청을 처리하는 Service 게층을 구현할 때는 클라이언트의 요청을 처리할 메서드의 원형을 만들고 별도의 클래스에서 구현
          클라이언트의 요청을 처리할 메서드의 원형은 요청 당 1개여야 한다.
          구현이 될 때는 여러 개의 메서드를 활용해도 된다.
          예를 들어 회원가입일 때, 메서드의 원형은 ‘회원가입()’ 하나로 만들고 아이디 중복 검사, 유효성 검사, 회원 가입 등을 메서드로 구현한다.
        - 메서드의 원형을 그림의 형태로 표현한 것이 UML이 된다.
      - Front Controller Pattern
        클라이언트의 요청을 전부 받아서 처리하는 Controller를 만들고 클라이언트의 요청을 분할해서 실제 처리하는 Controller를 구현하는 패턴
        우리가 만드는 것은 Page Contoller. 즉 여기에 속하는 패턴은 아님. AOP와 같은 것들이 중요해짐.
      - Command Pattern
        확장 버전이 마이크로 서비스
        클라이언트의 요청 별로 별도의 객체를 이용해서 처리하는 패턴
        Web Application Server 방식은 대부분 컨테이너 차원에서 싱글톤 패턴을 적용해서 객체가 처리할 수 있는 요청이 여러 개 오면 스레드를 이용해서 처리한다.
        하나의 서비스 객체가 처리할 수 있는 요청이 여러 개 오면 멀티 스레드의 형태로 처리하게 된다.
      - 가장 일반적인 구조
        View ↔ Controller ↔ Service ↔ Repository ↔ 저장소
        DTO나 Domain Class(Entity가 될 수도 있음)
        Filter - AOP나 Interceptor로 구현되기도 한다.
        Listener
        Config
        Micro Service의 핵심은 Service 계층의 분할.
        분할을 할 때 고려해야 하는 것은 Domain(회원이냐, 상품이냐), 읽기 작업과 다른 작업(변화를 주는 작업과 변화를 주지 않는 작업), 사용 빈도(사용 빈도 수가 높은 작업과 그렇지 않은 작업), 중요도(중간에 실패하거나 동기화의 중요성 - 결제)
