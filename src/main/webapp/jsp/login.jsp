<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel=stylesheet href="./resources/css/login.css" media="screen">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		sessionStorage.clear();
	});
</script>
<title>Login Page</title>
</head>
<body>

	<div id="mainheader">
		<img style="margin: 20px;" src="<c:url value="/resources/img/logosmall.png"/>"/>
	</div>


	<div id="loginContainer">
		<c:url value="loginprocess" var="loginUrl" />
		<form action="${loginUrl}" method="post">
			<c:if test="${param.error != null}">
				<p>Invalid username and password.</p>
			</c:if>
			<c:if test="${param.logout != null}">
				<p>You have been logged out.</p>
			</c:if>
			<label for="username">Username</label> <input type="text"
				id="username" name="j_username" /> <label for="password">Password</label>
			<input type="password" id="password" name="j_password" />

			<div id="lowerLogin">
				<input type="submit" value="Login">
				<!-- 
		<button type="submit" class="btn">Log in</button>
		-->
				<div style="position: absolute; top: 250px; left: 160px">
					If you' re not, please <a href="./user/new">register</a>.
				</div>
			</div>
		</form>
	</div>

	<footer>
	<div id="footerSection">
		<p>Electric Sheep 2014</p>
	</div>
	</footer>

</body>
</html>