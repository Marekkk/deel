<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel=stylesheet href="../resources/css/login.css" media="screen">
<title>Profile</title>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript">

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
	
	function send() {
		var message = new Object();
		message.old = document.getElementById("old").value;
		message.password = document.getElementById("password").value;
		var psw2 = document.getElementById("psw2").value;
		if (psw2 != message.password) {
			console.log(psw2);
			console.log(message.password);
			alert("Password are different!");
			return;
		}
		$.ajax({
			url : "/deel/user/updatePsw",
			data : message,
			type : 'GET',
			success : function(returndata) {
				console.log(returndata);
			}
		});
	}
</script>
</head>
<body>

	<header id="mainheader">
	<h3>
		<a id="title">drop<span>box</span>~
		</a>
	</h3>
	</header>
	
	<nav id="mainav">
		<ul style="margin-top:3em;">
			<li><a href="/deel/home">home</a></li>
		</ul>
		</nav>

	<div id="registrationContainer" style="height: 350px">
		<label>Old Password:</label> <input id="old" type="password" /> <br>
		<label>Password:</label> <input id="password" type="password"
			onchange="cleanRetype()" /> <br> <label>Re-type
			Password:</label> <input id="psw2" name="psw2" type="password"
			onchange="checkpsw()"> <br>
		<div id="checkpass"
			style="position: absolute; margin-left: -145px; font-family: Helvetica, sans-serif; left: 50%; color: red;">
		</div>
		<br>
		<div id="lowerRegistration">
			<input type="submit" value="Modify" onclick="javascript:send()">
		</div>
	</div>

	<footer>
	<div id="footerSection">
		<p>Electric Sheep 2014</p>
	</div>
	</footer>

</body>
</html>