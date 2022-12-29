<%--
  Created by IntelliJ IDEA.
  User: gimgyumin
  Date: 2022/12/29
  Time: 15:01
  To change this template use File | Settings | File Templates.
--%>
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
