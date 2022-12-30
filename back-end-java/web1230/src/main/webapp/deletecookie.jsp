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
