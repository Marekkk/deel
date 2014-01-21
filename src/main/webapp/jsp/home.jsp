<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="./resources/css/style.css" media="screen">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript">
	var data;
	function runEffect() {
		$("#uploadContainer").show("slow");
	}
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
	
	function removeFile(id) {
		var file = id;
		var message = new Object();
		message.id = file;
		
		$.ajax({
			url : 'file/remove',
			data: message,
			type : 'GET',
			success : function(returndata) {
				console.log(returndata);
				getFiles();
			}
		});
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

	function getRootId() {
		var req = "file/list";
		$.get(req, function(data, success) {
			console.log(success);

			console.log(data);
			currentDir = data.currentDir;
			sessionStorage.setItem("root", currentDir.id);
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
			addRow(a);
		}

		for ( var i in files) {
			var a = document.createElement("a");
			a.id = i;
			a.href = "file/download/"+ files[i] +"?id=" + i;
			a.innerHTML = files[i];
			addRow(a);
		}
	}
	
	function addRow(data) {
		var t = document.getElementById("dataTable");
		var c = document.createElement("td");
		c.appendChild(data);
		var r = document.createElement("tr");
		addingOps(data, c);
		r.appendChild(c);
		t.appendChild(r);
	}
	
	function addingOps(data, td) {
		var c = td;
		var a = document.createElement("a");
		var id = data.id;
		
		a.href = "javascript:removeFile(" + id +")";
		a.style = "text-decoration: none";
		a.innerHTML = "&nbsp Remove!";
		c.appendChild(a);
	}

	function cleanTable() {
		var table = document.getElementById("dataTable");
		for (var i = table.rows.length - 1; i > 1; i--) {
			table.deleteRow(i);
		}
	}

	$(document).ready(function() {
		getFiles();

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
			<li><a href="upload">upload</a></li>
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

		<form:form method="POST" commandName="fileForm" action="file/upload"
			name="ajaxForm" id="ajaxForm" enctype="multipart/form-data">
			<div id="uploadContainer">
				<input type="file" value="Choose file" name="files[0]" /> <input
					type="hidden" name="path" /> <input type="submit" value="Invia" />
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