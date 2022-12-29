<%@ page import="java.util.Enumeration" %>
<%--
  Created by IntelliJ IDEA.
  User: gimgyumin
  Date: 2022/12/29
  Time: 12:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>요청 실습</title>
</head>
<body>
<%
  // web.xml 파일의 초기화 파라미터 가져오기
  String user = application.getInitParameter("User");
%>
<%=user %><br />
  <a href="res.jsp?name=규민">GET 요청</a>
  <form method="post" action="res.jsp">
    별명: <input type="text" name="nickname" />
    <input type="submit" value="전송"/>
  </form>
  <%
    // 모든 헤더 정보 출력
    // 브라우저나 운영체제 정보, 쿠키 등을 확인 가능
    Enumeration <String> headerEnum = request.getHeaderNames();
    while(headerEnum.hasMoreElements()) {
      String name = headerEnum.nextElement();
      String value = request.getHeader(name);

  %>
<%=name%>: <%=value%><br/>
<%
  }
%>
</body>
</html>
