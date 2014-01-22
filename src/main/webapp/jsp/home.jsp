<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="./resources/css/style.css" media="screen">
<link
	href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css"
	rel="stylesheet" type="text/css" />
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript">
	function sendUpload() {
		this.ajaxForm.submit();
	}

	var currentDir;
	var files;
	var directories;

	function getFiles() {
		cleanTable();
		if (sessionStorage.getItem("dir") == null
				|| sessionStorage.getItem("dir") === undefined)
			var request = "file/list";
		else
			var request = "file/list?path=" + sessionStorage.getItem("dir");

		makeRequest(request);
	}

	function removeFile(id, type) {
		if (!confirm("Vuoi davvero eliminare il file?"))
			return;

		var message = new Object();
		message.id = id;

		if (type == "file") {
			$.ajax({
				url : 'file/remove',
				data : message,
				type : 'GET',
				success : function(returndata) {
					console.log(returndata);
					getFiles();
				}
			});
		} else if (type == "folder") {
			$.ajax({
				url : 'folder/remove',
				data : message,
				type : 'GET',
				success : function(returndata) {
					console.log(returndata);
					getFiles();
				}
			});
		}
	}

	var usersWithShare = new Array();

	function sharing(id) {
		var ul = document.getElementById("slist");
		$(ul).remove();
		var sharingDiv = document.getElementById("sharingList");
		var nul = document.createElement("ul");
		nul.id = "slist";
		sharingDiv.appendChild(nul);
		$.ajax({
			url : 'user/list',
			type : 'GET',
			success : function(returndata) {
				var users = returndata;
				console.log(returndata);
				createShareBox(users.id, users.Username, id);
			}
		});
		var button = document.createElement("input");
		button.id = "sendButton";
		button.type = "button";
		button.value = "Share File!";
		button.innerHTML = "Share File!";
		nul.appendChild(button);
		$('#sendButton').click(function() {
			alert("You' re sharing " + id + " with " + usersWithShare);
			var message = new Object();

			message.users = usersWithShare;
			message.file = id;

			$.ajax({
				url : 'file/share',
				type : 'POST',
				data : JSON.stringify(message),
				dataType : "json",
				contentType : "application/json",
				success : function(returndata) {
					console.log(returndata);
					getFiles();

				}
			});

		});
		$('#sharingList').dialog("open");
	}

	function createShareBox(usersid, usr, idFile) {
		usersWithShare = [];
		var ul = document.getElementById("slist");

		for ( var i = 0; i < usr.length; i++) {
			var li = document.createElement("li");
			var a = document.createElement("a");
			a.innerHTML = usr[i];
			a.href = "javascript:shareFileWith(" + usersid[i] + ")";
			li.appendChild(a);
			ul.appendChild(li);
		}
	}

	function shareFileWith(userId) {
		var currSize = usersWithShare.length;
		usersWithShare[currSize] = userId;
	}

	function shareFile() {
		alert();
	}

	function makeRequest(request) {
		var req = request;
		$.get(req, function(data, success) {
			console.log(success);

			console.log(data);
			currentDir = data.currentDir;
			if (req == "file/list") {
				sessionStorage.setItem("root", currentDir.id);
				sessionStorage.setItem("dir", currentDir.id);
			} else {
				sessionStorage.setItem("dir", currentDir.id);
			}
			files = data.files;
			directories = data.directories;
			console.log(currentDir);

			updateTable();
		});
	}

	function updateTable() {
		for ( var i in directories) {
			var a = document.createElement("a");
			a.id = i;
			a.value = i;
			a.style = "color:red";
			a.href = "javascript:changeFolder(" + i + ")";
			a.innerHTML = directories[i];
			a.type = "folder";
			addRow(a, i);
		}

		for ( var i in files) {
			var a = document.createElement("a");
			a.id = i;
			a.href = "file/download/" + files[i] + "?id=" + i;
			a.innerHTML = files[i];
			a.type = "file";
			addRow(a, i);
		}
	}

	function addRow(data, id) {
		var t = document.getElementById("dataTable");
		var c = document.createElement("td");
		c.appendChild(data);
		var r = document.createElement("tr");
		r.appendChild(c);
		addingOps(data, r, id);
		t.appendChild(r);
	}

	function addingOps(data, tr) {
		var r = tr;
		var c = document.createElement("td");
		var a = document.createElement("a");
		var id = data.id;
		var type = data.type;

		a.type = type;
		a.id = "opRemove";
		a.href = "javascript:removeFile(" + id + ", '" + type + "')";
		a.style = "text-decoration: none";
		a.innerHTML = "Remove";
		c.appendChild(a);

		var cs = document.createElement("td");
		var share = document.createElement("a");
		var idOpShare = "share_" + id;
		share.id = idOpShare;
		share.className = "opShare";
		share.href = "javascript:sharing(" + id + ")";
		share.style = "text-decoration: none";
		share.innerHTML = "Share";
		cs.appendChild(share);
		r.appendChild(c);
		r.appendChild(cs);
	}

	function cleanTable() {
		var table = document.getElementById("dataTable");
		for ( var i = table.rows.length - 1; i > 1; i--) {
			table.deleteRow(i);
		}
	}

	$(document).ready(function() {
		getFiles();

		$('#uploadContainer').dialog({
			autoOpen : false,
			show : {
				effect : "blind"
			},
			hide : {
				effect : "explode"
			},
			title : "Select file to upload:"
		});

		$('#sharingList').dialog({
			autoOpen : false,
			show : {
				effect : "blind"
			},
			hide : {
				effect : "explode"
			},
			title : "Share with:"
		});

		$("form#ajaxForm").submit(function(event) {
			event.preventDefault();
			console.log(this);

			$('input[name="path"]').val(sessionStorage.getItem("dir"));

			//grab all form data  
			var formData = new FormData($(this)[0]);

			$.ajax({
				url : 'file/upload',
				type : 'POST',
				data : formData,
				async : false,
				cache : false,
				contentType : false,
				processData : false,
				success : function(returndata) {
					console.log(returndata);
					getFiles();
				}
			});
			return false;
		});
	});

	function sendFolderName() {
		var addingFolder = document.getElementById("addingFolder");
		if (addingFolder.children.length > 1)
			return;
		var input = document.createElement("input");
		input.id = "folderName";
		input.type = "text";
		var submit = document.createElement("input");
		submit.type = "submit";
		submit.value = "Create!";
		addingFolder.appendChild(input);
		addingFolder.appendChild(submit);
	}

	function createFolder() {
		var input = document.getElementById("folderName");
		var folderName = input.value;
		var father = sessionStorage.getItem("dir");
		var username = "<c:out value="${user}"></c:out>";
		alert("Creating folder " + folderName + " in folder' s id " + father
				+ " for user " + username + "!");

		var message = new Object();
		message.id = father;
		message.folderName = folderName;

		$.ajax({
			url : 'file/addFolder',
			type : 'GET',
			data : message,
			async : false,
			cache : false,
			contentType : false,
			success : function() {
				getFiles();
			}
		});
	}

	function changeFolder(into) {
		var inFolder = into;
		sessionStorage.setItem("dir", inFolder);
		getFiles();
	}

	function goRoot() {
		var root = sessionStorage.getItem("root");
		sessionStorage.setItem("dir", root);
	}
	function uploadDialog() {
		$('#uploadContainer').dialog("open");
	}
</script>
<title>Home</title>
</head>


<body>
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
			<li><a href="home.html" onclick="javascript:goRoot()"
				class="active">home</a></li>
			<li><a href="logout">logout</a></li>
			<li><a href="javascript:uploadDialog()" id="uploadButton">upload</a></li>
		</ul>
		</nav>

		<div id="filesContainer">
			<table id="dataTable">
				<tr>
					<th>name</th>
				</tr>
				<tr>
					<!-- Adding folder -->
					<td><form:form method="POST"
							action="javascript:createFolder();" id="addingFolder">
							<a href="javascript:sendFolderName()"
								style="text-decoration: none">+</a>
						</form:form></td>
				</tr>
			</table>
		</div>

		<div id="uploadContainer">
			<form:form method="POST" commandName="fileForm" action="file/upload"
				name="ajaxForm" id="ajaxForm" enctype="multipart/form-data">

				<input type="file" value="Choose file" name="files[0]" />
				<input type="hidden" name="path" />
				<input type="submit" value="Invia" />
		</div>
		</form:form>

		<div id="sharingList">
			<ul id="slist">
			</ul>
		</div>

	</div>

	<footer>
	<div id="footerSection">
		<p>Electric Sheep 2014</p>
	</div>
	</footer>

</body>
</html>