<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<style>
#log{
	text-align: center;
    margin: 333px;
}
</style>
<script>

	function IsEmpty() {
		if (document.forms['frm'].Uname.value === "" && document.forms['frm'].Pass.value === "") {
			alert("Please enter username and password...");
			return false;
		} else if (document.forms['frm'].Uname.value === "") {
			alert("Please enter username...");
			return false;
		} else if (document.forms['frm'].Pass.value === "") {
			alert("Please enter password...");
			return false;
		}
		return true;
	}
	
	function error(){
		alert("Please enter valid details...");
	}
</script>

<%
	if(session.getAttribute("message") != null){
	%>
		<script>
			error();
		</script>	
	<%
	}
%>
<body>
<div id="log">
	<h2 style="color: darkorchid;">Login Page</h2><br>
	<form name="frm" action="login" method="post">
		<label><b>User Name :</b></label> <input type="text" name="Uname" placeholder="Username"> <br>
		<br> <label><b>Password :</b></label> <input type="Password" name="Pass" placeholder="Password"> <br>
		<br> <input onclick="return IsEmpty();" type="submit" value="Login"> <br>
		<br>
	</form>
</div>
</body>

</html>