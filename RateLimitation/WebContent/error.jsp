<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<style>
h2 {
    text-align: center;
    margin: 400px;
}
</style>
<body>
	<%
	if(session.getAttribute("message")==null){
		response.sendRedirect("login.jsp");
	}
	%>
	<h2> ${message} </h2>
</body>
</html>