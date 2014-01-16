<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel=stylesheet href="./resources/css/style.css" media="screen">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript">
   		function runEffect() {
   			$("#uploadContainer").toggle();
   		}
   		function sendWithAjax() {
   			var data = new FormData($("#ajaxForm")[0]);
   			$.ajax({
   				url : "file/new",
   				success : function() {alert("Success!");},
   				error : function() {alert("Error please retry!");},
   				type : "POST",
   				data : data
   			});
   		}
    </script>
<title>Home</title>
</head>


<body onload="javascript:runEffect()">
	<header id="mainheader">
	<h3>
		<a id="title">drop<span>box</span>~
		</a>
	</h3>
	</header>

	<div id="page">

		<nav id="personalInfo">
		<ul>
			<li>Welcome, <a href="#"><c:out value="${user}"></c:out></a>!
			</li>
		</ul>
		</nav>

		<nav id="mainav">
		<ul>
			<li><a href="home.html" class="active">home</a></li>
			<li><a href="login.html">logout</a></li>
			<li><a href="javascript:runEffect()" id="toggle">Upload</a>
				<div id="uploadContainer">
					<form name="ajaxForm" id="axajForm" enctype="multipart/form-data"
						onsubmit="javascript:sendWithAjax()">
						<input type="file" value="Choose file" name="files[0]" />
						<input type="hidden" value="/home/" name="path" />
						<input type="submit"
							value="Invia!">
					</form>
				</div>
			</li>
		</ul>
		</nav>

		<div id="filesContainer">
			<table>
				<tr>
					<th>name</th>
					<th>kind</th>
					<th>modified</th>
				</tr>
				<tr>
					<td>file1</td>
					<td>kind1</td>
					<td>date1</td>
				</tr>
			</table>
		</div>

	</div>

	<footer>
	<div id="footerSection">
		<p>Electric Sheep 2013</p>
	</div>
	</footer>

</body>
</html>