<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel=stylesheet href="../resources/css/login.css" media="screen">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<title>Registration</title>
<script type="text/javascript">
	function check() {
		if (form1.username.value == "") {
			alert("Please enter your username!");
			return false;
		}
// 		if (form1.email.value == "") {
// 			alert("Please enter your email adress!");
// 			return false;
// 		}
// 		if (form1.name.value == "") {
// 			alert("Please enter your name!");
// 			return false;
// 		}
// 		if (form1.surname.value == "") {
// 			alert("Please enter your surname!");
// 			return false;
// 		}

		if (form1.password.value != form1.psw2.value) {
			alert("Password doesn' t matching!");
			form1.password.value = "";
			return false;
		}
		return true;
	}

	function checkpsw() {
		var psw1 = document.getElementById("password");
		var psw2 = document.getElementById("psw2");
		if (psw1.value != psw2.value) {
			var message = document.getElementById("checkpass");
			message.innerHTML = "<p>" + "No matching between passwords!"
					+ "</p>";
			psw2.value = "";
		}
		if (psw1.value == psw2.value) {
			var message = document.getElementById("checkpass");
			message.innerHTML = "<p>" + "OK!" + "</p>";
		}
		//alert(psw1.value + " " + psw2.value);
	}

	function cleanRetype() {
		var psw2 = document.getElementById("psw2");
		psw2.value = "";
	}
</script>
</head>
<body>


	<div id="mainheader">
		<img style="margin: 20px;"
			src="<c:url value="/resources/img/logosmall.png"/>" />
	</div>


	<c:if test="${errors != null}">
		<c:forEach var="error" items="${errors}">
			<div class="error">
				Error
				<c:out value="${error}" />
			</div>
			<p>
		</c:forEach>
	</c:if>


	<div id="registrationContainer">
		<form:form commandName="user" name="form1">
			<label for="username">Username: </label>
			<form:input path="username" />
			<br>
			<label for="password">Password:</label>
			<form:input path="password" type="password" onchange="cleanRetype()" />
			<br>
			<label for="password">Re-type Password:</label>
			<input id="psw2" name="psw2" type="password" onchange="checkpsw()">
			<br>
			<div id="checkpass"
				style="position: absolute; margin-left: -145px; font-family: Helvetica, sans-serif; left: 50%; color: red;">
			</div>
	
			<br>
			<div id="lowerRegistration">
				<input type="submit" value="Register" onclick="return check()">
			</div>
		</form:form>
	</div>

	<footer>
	<div id="footerSection">
		<p>Electric Sheep 2014</p>
	</div>
	</footer>

</body>
</html>