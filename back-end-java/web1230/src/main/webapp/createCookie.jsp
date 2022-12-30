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
<a href="viewcookies.jsp">쿠키 보기</a>
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
</body>
</html>
