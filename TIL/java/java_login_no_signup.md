# [Java] 로컬 로그인 구현(회원가입x)

1. **샘플 데이터 생성**

   ```sql
   create database login;

   use login;

   create table tbl_member(
   	mid varchar(50) primary key,
   	mpw varchar(128) not null,
   	mname varchar(100) not null
   );

   insert into tbl_member values('rhkdtlrtm12', '1111', 'gyumin');

   commit;
   select * from tbl_member;
   ```

2. **프로젝트 생성하고 기본 설정**

   1. maven web project 생성
   2. pom.xml 파일에 필요한 의존성 설정

      servlet(IntelliJ 자동), jsp(IntelliJ 자동), mariadb

   3. main page 설정
   4. 프로젝트를 실행하고 브라우저에 메인 페이지가 출력되는지 확인

3. **Database Layer 작업**

   1. tml_member VO 클래스 생성

      ```java
      package com.example.login1230;

      public class MemberVO {
          private String mid;
          private String mpw;
          private String mname;

          public MemberVO() {
              super();
          }

          public String getMid() {
              return mid;
          }

          public void setMid(String mid) {
              this.mid = mid;
          }

          public String getMpw() {
              return mpw;
          }

          public void setMpw(String mpw) {
              this.mpw = mpw;
          }

          public String getMname() {
              return mname;
          }

          public void setMname(String mname) {
              this.mname = mname;
          }

          @Override
          public String toString() {
              return "MemberVO{" +
                      "mid='" + mid + '\'' +
                      ", mpw='" + mpw + '\'' +
                      ", mname='" + mname + '\'' +
                      '}';
          }
      }
      ```

   2. 데이터베이스 연동 코드를 가진 DAO 클래스를 생성 - persistence.MemberDAO

      ```java
      package persistence;

      import java.sql.Connection;
      import java.sql.DriverManager;
      import java.sql.PreparedStatement;
      import java.sql.ResultSet;

      public class MemberDAO {
          // 싱글톤 패턴을 위한 코드 - Spring에서는 필요 없음
          private MemberDAO() {}

          private static MemberDAO dao;

          public static MemberDAO getInstance() {
              if(dao == null) {
                  dao = new MemberDAO();
              }
              return dao;
          }

          // 데이터베이스 접속을 위한 드라이버 로드 코드
          static {
              try {
                  Class.forName("org.mariadb.jdbc.Driver");
                  System.out.println("드라이버 로드 성공");
              } catch(Exception e) {
                  System.out.println(e.getLocalizedMessage());
                  e.printStackTrace();
              }
          }

          // 데이터베이스 사용을 위한 속성
          private Connection connection;
          private PreparedStatement pstmt;
          private ResultSet rs;

          // 데이터베이스 연결
          {
              try {
                  connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/login", "root", "root");
                  System.out.println("데이터베이스 접속 성공");
              }catch(Exception e) {
                  System.out.println(e.getLocalizedMessage());
                  e.printStackTrace();
              }
          }
      }
      ```

   3. Test 클래스를 만들어서 데이터베이스 접속이 되는지 확인

      ```java
      import org.junit.jupiter.api.Test;
      import persistence.MemberDAO;

      public class TestCase {
          @Test
          public void daoTest() {
              MemberDAO dao = MemberDAO.getInstance();
              System.out.println(dao);

          }
      }
      ```

   4. DAO 클래스에 로그인 처리를 위한 메서드 추가

      ```java
      ...
      // 로그인 처리를 위한 메서드
          // 아이디와 비밀번호를 받아서 처리한 후 회원 정보를 리턴
          public MemberVO login(String mid, String mpw) {
              MemberVO vo = null;
              try {
                  // 수행할 SQL을 생성
                  String sql = "select * from tbl_member where mid=? and mpw=?";
                  pstmt = connection.prepareStatement(sql);
                  pstmt.setString(1, mid);
                  pstmt.setString(2, mpw);
                  // SQL 실행
                  rs = pstmt.executeQuery();

                  if(rs.next()) {
                      vo = new MemberVO();
                      // 비밀번호는 세션에 저장할 필요없기 때문에 제외한다.
                      vo.setMid(rs.getString("mid"));
                      vo.setMname(rs.getString("mname"));
                  }
              } catch (Exception e) {
                  System.out.println(e.getLocalizedMessage());
                  e.printStackTrace();
              }

              return vo;
          }
      ```

   5. testCase에 로그인 처리 테스트를 진행한다.

      ```java
      import org.junit.jupiter.api.Test;
      import persistence.MemberDAO;

      public class TestCase {
          @Test
          public void daoTest() {
              MemberDAO dao = MemberDAO.getInstance();
              System.out.println(dao.login("rhkdtlrtm12", "1111")); // id와 pw가 맞아서 데이터 출력
              System.out.println(dao.login("rhkdtlrtm12", "1234")); // pw가 틀려서 null
              System.out.println(dao.login("rhkdtlrtm13", "1111")); // id가 틀려서 null
          }

      }
      ```

4. **ServiceLayer 작업**

   1. Service와 Controller 그리고 View 사이의 데이터 전달을 위한 DTO 클래스 생성 - dto.MemberDTO

      ```java
      package dto;

      public class MemberDTO {
          private String mid;
          private String mpw;
          private String mname;

          public MemberDTO() {
              super();
          }

          public String getMid() {
              return mid;
          }

          public void setMid(String mid) {
              this.mid = mid;
          }

          public String getMpw() {
              return mpw;
          }

          public void setMpw(String mpw) {
              this.mpw = mpw;
          }

          public String getMname() {
              return mname;
          }

          public void setMname(String mname) {
              this.mname = mname;
          }

          @Override
          public String toString() {
              return "MemberDTO{" +
                      "mid='" + mid + '\'' +
                      ", mpw='" + mpw + '\'' +
                      ", mname='" + mname + '\'' +
                      '}';
          }
      }
      ```

   2. 회원 관련 요청을 처리할 메서드를 소유하는 Service 인터페이스를 생성 - service.MemberService

      ```java
      package service;

      import dto.MemberDTO;

      public interface MemberService {
      		// 로그인 처리를 위한 메서드
          public MemberDTO login(String mid, String mpw);
      }
      ```

   3. 회원 관련 요청을 처리할 메서드를 제공하는 클래스 생성 - service.MemberServiceImpl

      ```java
      package service;

      import domain.MemberVO;
      import dto.MemberDTO;
      import persistence.MemberDAO;

      public class MemberServiceImpl extends MemberService {
          // DAO 변수(외부러부터 주입받아야 함)
          private MemberDAO memberDAO;

          private MemberServiceImpl() {
              // 지금은 주입받을 수 없으니 일단 생성자에서 만들어준다.
              memberDAO = MemberDAO.getInstance();
          }

          private static MemberService service;

          public static MemberService getInstance() {
              if (service == null) {
                  service = new MemberServiceImpl();
              }
              return service;
          }

          @Override
          public MemberDTO login(String mid, String mpw) {
              MemberDTO dto = null;
              MemberVO vo = memberDAO.login(mid, mpw);

              //vo를 dto로 변환
              if (vo != null) {
                  dto = new MemberDTO();
      						// 비밀번호는 반환할 필요가 없다.
                  dto.setMid(vo.getMid());
                  dto.setMname(vo.getMname());
              }

              return dto;
          }
      }
      ```

   4. test method 작성

      ```java
      ...
      @Test
          public void serviceTest() {
              MemberService service = MemberServiceImpl.getInstance();
              System.out.println(service.login("rhkdtlrtm12", "1111"));
              System.out.println(service.login("rhkdtlrtm13", "1111"));
          }
      ```

5. **Controller Layer**

   아이디와 비밀번호를 입력받아서 로그인을 처리

   URL부터 생각하자 → login

   get 방식: 로그인 화면 출력

   post 방식: 로그인 처리 수행

   1. login 요청을 처리할 Servlet을 만들고 작성 - controller.LoginController

      얘는 **자동적으로 싱글톤 패턴**이기 때문에 그렇게 만들어주지 않아도 된다.

      컨트롤러는 request,response 변수를 만들어야 해서 테스트가 어렵다

      ```java
      package controller;

      import dto.MemberDTO;
      import jakarta.servlet.*;
      import jakarta.servlet.http.*;
      import jakarta.servlet.annotation.*;
      import service.MemberService;
      import service.MemberServiceImpl;

      import java.io.IOException;

      @WebServlet(name = "LoginController", value = "/login")
      public class LoginController extends HttpServlet {

          // 일단은 생성자에서 주입해주도록 하자
          private MemberService memberService;

          public LoginController() {
              super();
              memberService = MemberServiceImpl.getInstance();
          }

          @Override
          protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              // 이동 처리 해주는데 보통 포워딩이다.
              // webapp 디렉토리의 member 디렉토리의 login.jsp로 포워딩
              request.getRequestDispatcher("/member/login.jsp").forward(request, response);
          }

          @Override
          protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              // 로그인 처리
              // 파라미터 가져오기
              String mid = request.getParameter("mid");
              String mpw = request.getParameter("mpw");

              // 서비스 메서드 호출
              MemberDTO dto = memberService.login(mid, mpw);

              // 결과를 가지고 분기
              // 세션부터 찾아오기
              HttpSession session = request.getSession();
              if(dto == null) {
                  // 로그인 실패했을 때
                  session.invalidate();
                  // 로그인 페이지로 되돌아가기
                  response.sendRedirect("login?error=error");
              } else {
                  // 로그인 성공했을 때
                  session.setAttribute("logininfo", dto);
                  // 메인 페이지로 리다이렉트
                  response.sendRedirect("./");
              }
          }
      }
      ```

   2. logout을 처리하는 Servlet을 생성하고 작성 - controller.LogoutController

      ```java
      package controller;

      import jakarta.servlet.*;
      import jakarta.servlet.http.*;
      import jakarta.servlet.annotation.*;

      import java.io.IOException;

      @WebServlet(name = "LogoutController", value = "/logout")
      public class LogoutController extends HttpServlet {
          public LogoutController() {
              super();
          }

          @Override
          protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              // 세션 초기화
              request.getSession().invalidate();
              // 메인페이지로 리다이렉트
              response.sendRedirect("./");
          }

          @Override
          protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              doGet(request, response);
          }
      }
      ```

   3. 화면에 출력하기 위해 index.jsp 수정

      위 코드를 보면 ‘logininfo’에 정보를 넣어놨었기 때문에 같은 이름으로 세션에서 가져와야 함.

      ```java
      <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
      <!DOCTYPE html>
      <html>
      <head>
          <title>로그인 처리</title>
      </head>
      <body>
          <%
              Object loginInfo = session.getAttribute("logininfo");
              if(loginInfo == null) {
          %>
              <a href="login">로그인</a>
          <% } else {
              dto.MemberDTO dto = (dto.MemberDTO)loginInfo;
          %>
              <%= dto.getMname() %> 님 환영합니다. <br />
              <a href="logout">로그아웃</a>
          <% } %>
      </body>
      </html>
      ```

   4. member/login.jsp 생성

      ```java
      <%@ page contentType="text/html;charset=UTF-8" language="java" %>
      <html>
      <head>
          <title>로그인</title>
      </head>
      <body>
      <%
        String error = request.getParameter("error");
        if(error != null) {
      %>
        <div>아이디나 비밀번호가 틀렸습니다.</div>
      <%
        }
      %>
        <form method="post">
          아이디<input type="text" name="mid" /><br />
          비밀번호<input type="password" name="mpw" /><br />
          <input type="submit" value="로그인" />
        </form>
      </body>
      </html>
      ```

6. **자동 로그인**

   로그인할 때 자동 로그인 체크 여부를 확인해서 자동 로그인을 체크하면 쿠키에 유일 무이한 값을 배정하고 이 값을 데이터베이스에도 기록한 후 다음에 로그인을 시도할 때 쿠키의 값을 읽어서 쿠키의 값이 있는 경우에는 이 쿠키의 값을 가지고 데이터베이스에서 로그인을 수행할 수 있도록 한다.

   1. 데이터베이스의 회원 테이블을 수정 - 유일무이한 값을 저장할 수 있도록 컬럼을 추가

      ```sql
      ALTER TABLE tbl_member ADD COLUMN uuid VARCHAR(68);
      ```

   2. MemberVO와 MemberDTO 클래스에도 컬럼의 값을 저장할 수 있는 속성을 추가

      `private String uuid` 와 `getter` `setter` 추가(VO와 DTO에)

   3. login.jsp에서 자동 로그인 여부 체크박스를 폼에 추가
   4. MemberDAO 클래스에 2개의 메서드 추가

      - uuid를 가지고 로그인을 하는 메서드

        ```java
        ...
        public MemberVO login(String uuid) {
                MemberVO vo = null;
                try {
                    String sql = "select * from tbl_member where uuid=?";
                    pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, uuid);

                    rs = pstmt.executeQuery();

                    if(rs.next()) {
                        vo = new MemberVO();
                        vo.setMid(rs.getString("mid"));
                        vo.setMname(rs.getString("mname"));

                    }
                }catch(Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
                return vo;
            }
        ```

      - uuid를 업데이트하는 메서드: 로그인 성공 시 호출되는 메서드

        ```java
        public void updateUUID(String mid, String uuid) {
                try {
                    String query = "update tbl_member set uuid=?" + "where mid=?";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, uuid);
                    pstmt.setString(2, mid);

                    pstmt.executeUpdate();

                } catch(Exception e) {
                    System.out.println(e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        ```

   5. MemberService 인터페이스의 메서드를 수정하고 1개 추가

      ```java
      package service;

      import dto.MemberDTO;

      public interface MemberService {
          // 로그인 처리를 위한 메서드
          public MemberDTO login(String mid, String mpw, String uuid);

          // 자동 로그인 처리를 위한 메서드
          public MemberDTO login(String uuid);
      }
      ```

   6. MemberServiceImpl 클래스의 메서드를 수정하고 1개 추가

      ```java
      ...
      @Override
          public MemberDTO login(String mid, String mpw, String uuid) {
              MemberDTO dto = null;
              MemberVO vo = memberDAO.login(mid, mpw);

              // vo를 dto로 변환
              if (vo != null) {
                  dto = new MemberDTO();
                  // 비밀번호는 반환할 필요가 없다.
                  dto.setMid(vo.getMid());
                  dto.setMname(vo.getMname());
                  // UUID 업데이트
                  memberDAO.updateUUID(mid, uuid);
              }

              return dto;
          }

          @Override
          public MemberDTO login(String uuid) {
              MemberDTO dto = null;

              MemberVO vo = memberDAO.login(uuid);
              if(vo != null) {
                  dto = new MemberDTO();
                  dto.setMid(vo.getMid());
                  dto.setMname(vo.getMname());
              }
              return dto;
          }
      ```

   7. LoginController 클래스의 로그인 처리 메서드 수정

      ```java
      package controller;

      import dto.MemberDTO;
      import jakarta.servlet.*;
      import jakarta.servlet.http.*;
      import jakarta.servlet.annotation.*;
      import service.MemberService;
      import service.MemberServiceImpl;

      import java.io.IOException;
      import java.util.UUID;

      @WebServlet(name = "LoginController", value = "/login")
      public class LoginController extends HttpServlet {

          // 일단은 생성자에서 주입해주도록 하자
          private MemberService memberService;

          public LoginController() {
              super();
              memberService = MemberServiceImpl.getInstance();
          }

          @Override
          protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              // 이동 처리 해주는데 보통 포워딩이다.
              // webapp 디렉토리의 member 디렉토리의 login.jsp로 포워딩
              request.getRequestDispatcher("/member/login.jsp").forward(request, response);
          }

          @Override
          protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              // 로그인 처리
              // 파라미터 가져오기
              String mid = request.getParameter("mid");
              String mpw = request.getParameter("mpw");

              // 자동 로그인 읽기
              String auto = request.getParameter("auto");
              // 체크박스는 value가 없으면 체크하면 on, 그렇지 않으면 null
              String uuid;
              if (auto == null) {
                  uuid = null;
              } else {
                  uuid = UUID.randomUUID().toString();
              }

              // 서비스 메서드 호출
              MemberDTO dto = memberService.login(mid, mpw, uuid);

              // 결과를 가지고 분기
              // 세션부터 찾아오기
              HttpSession session = request.getSession();
              if (dto == null) {
                  // 로그인 실패했을 때
                  session.invalidate();
                  // 로그인 페이지로 되돌아가기
                  response.sendRedirect("login?error=error");
              } else {
                  // 로그인 성공했을 때
                  session.setAttribute("logininfo", dto);
                  if (uuid != null) {
                      // 쿠키를 생성해서 저장
                      Cookie rememberCookie = new Cookie("remember-me", uuid);
                      rememberCookie.setMaxAge(60 * 60 * 24 * 2);
                      rememberCookie.setPath("/");
                      response.addCookie(rememberCookie);
                  }
                  // 메인 페이지로 리다이렉트
                  response.sendRedirect("./");
              }
          }
      }
      ```

   8. 실행하면 자동 로그인 체크 여부에 따라 쿠키 생성 확인 가능
   9. / 요청이 왔을 때 처리하기 위한 Servlet을 생성 - Controller.indexController

      (Filter가 동작하도록 하기 위해서)

      ```java
      package controller;

      import jakarta.servlet.*;
      import jakarta.servlet.http.*;
      import jakarta.servlet.annotation.*;

      import java.io.IOException;

      @WebServlet(name = "IndexController", value = "/")
      public class IndexController extends HttpServlet {
          public IndexController() {
              super();
          }
          @Override
          protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              request.getRequestDispatcher("index.jsp").forward(request, response);
          }

          @Override
          protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

          }
      }
      ```

   10. 모든 요청에 대해서 반응하는 필터를 생성 - filter.LoginCheckFilter
   11. 실행하고 로그인을 할 때 자동 로그인을 체크하고 로그인한 후 로그아웃을 하고 다시 로그인을 누르면 로그인을 하지 않아도 로그인된 상태가 된다.
