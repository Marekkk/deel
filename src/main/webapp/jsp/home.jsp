<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href=<c:url value="/resources/css/style.css"/> media="screen">
<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/base/jquery-ui.css" media="screen"> 
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript">

	$(function() {
		$('#trash').dialog({
			autoOpen : false,
			show : {
				effect : "blind"
			},
			hide : {
				effect : "explode"
			},
			modal : true,

			close : function() {
				cleanTable("trashTable");
			},

			title : "Revision for your file:"
		});
		
		$('#revision').dialog({
			autoOpen : false,
			show : {
				effect : "blind"
			},
			hide : {
				effect : "explode"
			},
			modal : true,

			close : function() {
				cleanTable("revisionTable");
			},

			title : "Revision for your file:"
		});
		
		$('#uploadContainer').dialog({
			autoOpen : false,
			show : {
				effect : "blind"
			},
			hide : {
				effect : "explode"
			},
			modal : true,
			
			title : "Select file to upload:"
		});

		$('#sharingList').dialog({
			autoOpen : false,
			show : {
				effect : "blind"
			},
			hide : {
				effect : "explode",
			},
			title : "Share with:"
		});
	});

	function sendUpload() {
		this.ajaxForm.submit();
	}

	var currentDir;
	var files;
	var directories;
	var filesHidden;

	function getFiles() {
		cleanTable("dataTable");
		if (sessionStorage.getItem("dir") == null
				|| sessionStorage.getItem("dir") === undefined)
			var request = "<c:url value="/file/list"/>";
		else
			var request = "<c:url value="/file/list?path="/>" + sessionStorage.getItem("dir");

		makeRequest(request);
	}
	
	function makeRequest(request) {
		var req = request;
		$.get(req, function(data, success) {
			console.log(success);

			console.log(data);
			currentDir = data.currentDir;
			if (req == "<c:url value="/file/list"/>") {
				sessionStorage.setItem("root", currentDir.id);
				sessionStorage.setItem("dir", currentDir.id);
			} else {
				sessionStorage.setItem("dir", currentDir.id);
			}
			files = data.files;
			directories = data.directories;
			filesHidden = data.filesHidden;
			console.log(currentDir, filesHidden);
			updateTable("dataTable");
		});
	}

	function removeFile(id, type) {
		if (!confirm("Vuoi davvero eliminare il file?"))
			return;

		var message = new Object();
		message.id = id;

		if (type == "file") {
			$.ajax({
				url : "<c:url value="/file/remove"/>",
				data : message,
				type : 'GET',
				success : function(returndata) {
					console.log(returndata);
					getFiles();
				}
			});
		} else if (type == "folder") {
			$.ajax({
				url : "<c:url value="/folder/remove"/>",
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
			alert("File Shared!");
			//alert("You' re sharing " + id + " with " + usersWithShare);
			var message = new Object();

			message.users = usersWithShare;
			message.file = id;

			$.ajax({
				url : "<c:url value="/file/share"/>",
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

		for (var i = 0; i < usr.length; i++) {
			var li = document.createElement("li");
			var a = document.createElement("a");
			a.innerHTML = usr[i];
			a.href = "javascript:shareFileWith(" + usersid[i] + ")";
			li.appendChild(a);
			var a2 = document.createElement("a");
			a2.id = "added" + usersid[i];
			a2.innerHTML = "";
			li.appendChild(a2);
			ul.appendChild(li);
		}
	}

	function shareFileWith(userId) {
		for (var i = 0; i < usersWithShare.length; i++) {
			if (usersWithShare[i] == userId) {
				alert("You have already insert this user!")
				return;
			}
		}
		var currSize = usersWithShare.length;
		usersWithShare[currSize] = userId;
		var a = document.getElementById("added" + userId);
		a.innerHTML = "&nbsp OK!";
	}

	function revision(id) {
		console.log(id);
		var message = new Object();
		message.id = id;
		$.ajax({
			url : "<c:url value="/file/revision/list"/>",
			type : 'GET',
			data : message,
			success : function(returndata) {
				var dates = new Array();
				var idRevs = new Array();
				for ( var i in returndata) {
					// i is the id of current revision
					d = new Date(returndata[i].date);
					var currPos = dates.length;
					dates[currPos] = d;
					idRevs[currPos] = i;
				}
				createRevisionsTable(id, idRevs, dates);
			}
		});

		$('#revision').dialog("open");
	}

	function createRevisionsTable(idFile, idRevisions, datesRevisions) {
		//alert("Id file -> " + idFile);
		//alert("Id Revisions -> " + idRevisions);
		//alert("Dates revisions -> " + datesRevisions);
		var table = document.getElementById("revisionTable");
		for (var i = 0; i < datesRevisions.length; i++) {
			var r = document.createElement("tr");
			var c = document.createElement("td");
			var a = document.createElement("a");
			a.style = "color:black";
			//a.href = "javascript:requestRevision("+idFile+","+idRevisions[i]+")";
			a.href = "file/revision/" + files[idFile] + "?id=" + idFile
					+ "&revision=" + idRevisions[i];
			a.innerHTML = datesRevisions[i];
			c.appendChild(a);
			r.appendChild(c);
			table.appendChild(r);
		}
	}

	function requestRevision(idFile, idRevision) {
		var message = new Object();
		message.idFile = idFile;
		message.idRevision = idRevision;
		$.ajax({
			url : "<c:url value="/file/revision/list"/>",
			type : 'GET',
			data : message,
			success : function(returndata) {
				console.log(returndata);
			}
		});
	}

	function updateTable(tableId) {
		for ( var i in directories) {
			var a = document.createElement("a");
			a.id = i;
			a.value = i;
			a.style = "color:red";
			a.href = "javascript:changeFolder(" + i + ")";
			a.innerHTML = directories[i];
			a.type = "folder";
			addRow(a, i, tableId);
		}

		for ( var i in files) {
			var a = document.createElement("a");
			a.id = i;
			a.href = "file/download/" + files[i] + "?id=" + i;
			a.innerHTML = files[i];
			a.type = "file";
			addRow(a, i, tableId);
		}
	}

	function addRow(data, id, tableId) {
		var t = document.getElementById(tableId);
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
		c.style = "background-color: transparent";
		var a = document.createElement("a");
		var id = data.id;
		var type = data.type;

		a.type = type;
		a.id = "opRemove";
		a.href = "javascript:removeFile(" + id + ", '" + type + "')";
		a.style = "text-decoration: none";
		a.innerHTML = "Remove";
		c.appendChild(a);
		r.appendChild(c);


		if (type == "file") {
			var cs = document.createElement("td");
			cs.style = "background-color: transparent";
			var share = document.createElement("a");
			var idOpShare = "share_" + id;
			share.id = idOpShare;
			share.className = "opShare";
			share.href = "javascript:sharing(" + id + ")";
			share.style = "text-decoration: none";
			share.innerHTML = "Share";
			cs.appendChild(share);
			r.appendChild(cs);

			var cr = document.createElement("td");
			cr.style = "background-color: transparent";
			var rev = document.createElement("a");
			rev.id = "revision_" + id;
			rev.href = "javascript:revision(" + id + ")";
			rev.style = "text-decoration:none; color: blue";
			rev.innerHTML = "Revisions";
			cr.appendChild(rev);
			r.appendChild(cr);
		}
	}

	function cleanTable(tagid) {
		var table = document.getElementById(tagid);
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
				url : "<c:url value="/file/upload"/>",
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
			url : "<c:url value="/file/addFolder"/>",
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
	function trashDialog() {
		createDeletedTable();
		$('#trash').dialog("open");
	}
	
	function createDeletedTable() {
		var table = document.getElementById("trashTable");
		for (var i in filesHidden) {
			var r = document.createElement("tr");
			var c = document.createElement("td");
			var a = document.createElement("a");
			a.style = "color:black";
			a.href = "#";
			a.innerHTML = filesHidden[i];
			c.appendChild(a);
			r.appendChild(c);
			table.appendChild(r);
		}
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
			<li>Welcome, <a href="/deel/user/settingsProfile"><c:out value="${user}"></c:out></a>!
			</li>
		</ul>
		</nav>

		<nav id="mainav">
		<ul>
			<li><a href="<c:url value="/home" />" onclick="javascript:goRoot()"
				class="active">home</a></li>
			<li><a href=<c:url value="/logout"/> >logout</a></li>
			<li><a href="javascript:uploadDialog()" id="uploadButton">upload</a></li>
			<li><a href="javascript:trashDialog()" >trash</a></li>
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

		<div id="revision">
			<table id="revisionTable">
				<tr>
					<th>Date</th>
				<tr>
			</table>
		</div>
		
		<div id="trash">
			<table id="trashTable">
				<tr>
					<th>Name</th>
				<tr>
			</table>
		</div>

	</div>

	<footer>
	<div id="footerSection">
		<p>Electric Sheep 2014</p>
	</div>
	</footer>

</body>
</html>