<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="../resources/css/style.css" rel="stylesheet" type="text/css" />
<link href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css" rel="stylesheet" type="text/css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<title>Select Users</title>
<script type="text/javascript">

function getUsers() {
	$.ajax({
		url : './list',
		type : 'GET',
		success : function(returndata) {
			var users = returndata;
			console.log(returndata);
			createShareBox(users.id, users.Username);
		}
	});
}

function createShareBox(usersid, usr) {
	var page = document.getElementById("page");
	var list = document.createElement("ul");
	list.id = "userList";
	for (var i = 0; i < usr.length; i++) {
		var op = document.createElement("li");
		var a = document.createElement("a");
		a.id = usersid[i];
		a.href = "javascript:shareWith("+ usersid[i] +")";
		a.innerHTML = usr[i];
		op.appendChild(a);
		list.appendChild(op);
	}
	page.appendChild(list);
}

function shareWith(id) {
	alert("You' re going to share file with -> " +id);
}

$(document).ready(function () {
	getUsers();
	$('#dialog').dialog({
		autoOpen: false
	});
});

function openDialog() {
	$('#dialog').dialog("open");
}
</script>
</head>
<body>
	<div id="page">
		<button onclick="openDialog()" value="apri">Apri</button>
		<div>
		<p id="dialog">Sdsadsaddasdadad</p> 
		</div>
	</div>
</body>
</html>