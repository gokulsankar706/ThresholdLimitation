<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<style>
h1 {
    text-align: center;
    margin: 400px;
}
</style>
<body>
	 <%
		if(session.getAttribute("username")==null){
			response.sendRedirect("login.jsp");
		}
	%> 
	<h1>Hello..... ${username} welcome back!</h1>
	
	<form action="logout">
	<br> <input type="submit" value="Logout"> <br>
	</form>
</body>
</html>