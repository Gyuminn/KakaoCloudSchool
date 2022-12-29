<%--
  Created by IntelliJ IDEA.
  User: gimgyumin
  Date: 2022/12/29
  Time: 11:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sample</title>
</head>
<body>
<%
    int sum = 0;
    for (int i = 0; i < 10; i++) {
        sum = sum + i;
    }
%>
    <h1>JSP 생성중임</h1>
    <div><%=sum%></div>
    <div><%= request.getRemoteAddr()%></div>
</body>
</html>
