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
