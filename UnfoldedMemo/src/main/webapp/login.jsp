<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=medium-dpi">
    
	<title>login</title>
</head>
<body>
<%
if(session.getAttribute("user_idx") != null) {
	session.removeAttribute("user_idx");
	//session.invalidate();
	System.out.println("logout!");
}
%>

<form name="insert" action="./login.view" method="post">
<input type="text" name="id" maxlength="10" value="testid"><br>
<input type="password" name="pw" maxlength="10" value="1234"><br>
<input type="submit" value="로그인">
</form>

</body>
</html>