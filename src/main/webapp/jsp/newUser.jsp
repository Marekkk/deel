<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel=stylesheet href="../resources/css/login.css" media="screen">
<title>Registration</title>
</head>
<body>

	<header id="mainheader">
	<h3>
		<a id="title">drop<span>box</span>~
		</a>
	</h3>
	</header>

	<div id="registrationContainer">
		<form:form commandName="user">
			<label for="username">Username: </label>
			<form:input path="username" />
			<br>
			<label for="password">Password:</label>
			<form:input path="password" />
			<br>
			<label for="password">Re-type Password:</label>
			<input type="password">
			<br>
			<label for="email">Email: </label>
			<form:input path="email" />
			<br>
			<label for="name">Name:</label>
			<form:input path="name" />
			<br>
			<label for="surname">Surname:</label>
			<form:input path="surname" />
			<br>
			<div id="lowerRegistration">
				<input type="submit" value="Register">
			</div>
		</form:form>
	</div>

	<footer>
	<div id="footerSection">
		<p>Electric Sheep 2013</p>
	</div>
	</footer>

</body>
</html>