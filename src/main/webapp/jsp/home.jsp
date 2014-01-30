<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href=<c:url value="/resources/js/style.css"/>
	media="screen">
<link rel="stylesheet"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/themes/base/jquery-ui.css"
	media="screen">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="<c:url value='/resources/js/service.js'/>"></script>
<script src="<c:url value='/resources/js/myUI.js'/>"></script>
<script type="text/javascript">

	$(function() {
		
		$('#team').dialog({
			autoOpen : false,
			show : {
				effect : "blind"
			},
			hide : {
				effect : "explode"
			},
			modal : true,

			title : "Create your team:"
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
			title : "Revision for your file",
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
			
			modal : true,
			
			title : "Share with:",
			
			close : function () {
				$("#insertUser").remove();
				$("#sharingWith").remove();
				$("#sender").remove();
			}
		});
	});

	function sendUpload() {
		this.ajaxForm.submit();
	}

	var currentDir;
	var files;
	var directories;
	var filesHidden;

// 	function getFiles() {
// 		cleanTable("dataTable");
// 		if (sessionStorage.getItem("dir") == null
// 				|| sessionStorage.getItem("dir") === undefined)

// 				var files = e.dataTransfer.files;
// 				var fd = new FormData();
// 				console.log(files);
// 				for (var i = 0; i < files.length; i++) 
// 				      fd.append('files', files[i]);
				
// 				fd.append('path', currentFolder);
				
// 				$.ajax({
					
					
// 					url : "file/upload",
// 					type : 'POST',
// 					data : fd,
// 					cache : false,
// 					contentType : false,
// 					async : false,
// 					processData : false,
// 					success : function(returndata) {
// 						getFiles();
// 					}
// 				});
				 
				

// 			},
			
// 			downloadFile: function (id) {
// 				console.log("donwloading file with id " + id);
// 			},
// 			removeFolder : function (id) {
// 				console.log("removing folder with id " + id);
// 			},
			
// 			changeDir: function(id) {
// 				console.log("changing to dir with id " + id);
// 			},
// 			remove : function (id) {
// 				console.log("removing id " +id);
// 			},
// 			revision : function (id) {
// 				console.log("revision id " +id);	
// 			},
// 			share : function (id) {
// 				console.log("sharing id" + id);
// 			},e/list"/>";
// 		else
// 			var request = "<c:url value="/file/list?path="/>" + sessionStorage.getItem("dir");

// 		makeRequest(request);
// 	}
	
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

	function sharing(fp) {

	}

	function createShareBox(usersid, usr, idFile) {
		usersWithShare = [];
		var ul = document.getElementById("slist");
		ul.style = "list-style-type: none; margin-left: 2px";

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
	
	var usersInTeam = new Array();
	function createTeam() {
		var ul = document.getElementById("teamList");
		$(ul).remove();
		var team = document.getElementById("team");
		var nul = document.createElement("ul");
		nul.id = "teamList";
		team.appendChild(nul);
		$.ajax({
			url : "<c:url value="/user/list"/>",
			type : 'GET',
			success : function(returndata) {
				var data = returndata;
				createTeamBox(data.users);
			}
		});
		var button = document.createElement("input");
		button.id = "sendButton";
		button.type = "button";
		button.value = "Create Team!";
		button.innerHTML = "Create Team!";
		nul.appendChild(button);
		var name = document.createElement("input");
		name.id = "name";
		name.value = "";
		name.type = "text";
		nul.appendChild(name);
		$('#sendButton').click(function() {
			//alert("Team Created!");
			var message = new Object();

			message.users = usersInTeam;
			message.name = name.value;
			
			$.ajax({
				url : "<c:url value="/team/create"/>",
				type : 'POST',
				data : JSON.stringify(message),
				dataType : "json",
				contentType : "application/json",
				success : function(returndata) {
					console.log(returndata);
					alert(returndata.status);				
				}
			});

		});
		$('#team').dialog("open");
	}
	
	function createTeamBox(users) {
		usersInTeam = [];
		var ul = document.getElementById("teamList");
		ul.style = "list-style-type: none; margin-left: 2px";

		users.forEach(function(u) {
			var li = document.createElement("li");
			var a = document.createElement("a");
			a.innerHTML = u.username;
			a.href = "javascript:addUserInTeam(" + u.id + ")";
			li.appendChild(a);
			var a2 = document.createElement("a");
			a2.id = "added" + u.id;
			a2.innerHTML = "";
			li.appendChild(a2);
			ul.appendChild(li);
		});
	}

	function addUserInTeam(userId) {
		for (var i = 0; i < usersInTeam.length; i++) {
			if (usersInTeam[i] == userId) {
				alert("You have already insert this user!")
				return;
			}
		}
		var currSize = usersInTeam.length;
		usersInTeam[currSize] = userId;
		var a = document.getElementById("added" + userId);
		a.innerHTML = "&nbsp OK!";
	}
	

	function revision(id) {

	}

	function createRevisionsTable(file, idRevisions, datesRevisions) {

		var table = document.getElementById("revisionTable");
		$(table).empty();
		for (var i = 0; i < datesRevisions.length; i++) {
			var r = document.createElement("tr");
			var c = document.createElement("td");
			var a = document.createElement("a");
			a.style = "color:black";
			//a.href = "javascript:requestRevision("+idFile+","+idRevisions[i]+")";
			a.href = "file/revision/" + file.name + "?id=" + file.id2
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
		var img = document.createElement("img");
		img.src = "<c:url value="/resources/img/remove.png"/>";
		img.height = "50";
		img.width = "75";
		a.appendChild(img);
		var id = data.id;
		var type = data.type;

		a.type = type;
		a.id = "opRemove";
		a.href = "javascript:removeFile(" + id + ", '" + type + "')";
		a.style = "text-decoration: none";
		//a.innerHTML = "Remove";
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
			//share.innerHTML = "Share";
			var img = document.createElement("img");
			img.src = "<c:url value="/resources/img/share.png"/>";
			img.height = "40";
			img.width = "40";
			share.appendChild(img);
			cs.appendChild(share);
			r.appendChild(cs);

			var cr = document.createElement("td");
			cr.style = "background-color: transparent";
			var rev = document.createElement("a");
			rev.id = "revision_" + id;
			rev.href = "javascript:revision(" + id + ")";
			rev.style = "text-decoration:none; color: blue";
			var img = document.createElement("img");
			img.src = "<c:url value="/resources/img/revision.png"/>";
			img.height = "35";
			img.width = "40";
			rev.appendChild(img);
			//rev.innerHTML = "Revisions";
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
		myUI.init();
		
		var wrapper = $('#wrapper');
		
		myUI.setWrapper(wrapper);
		
		myUI.updateSpace();
		
		return;
		
		function getDataAndMakeTableBody(body) {
			
			var opsImageUrls = {
					remove : '<c:url value="/resources/img/remove.png"/>',
					revision : '<c:url value="/resources/img/revision.png"/>',
					share : '<c:url value="/resources/img/share.png"/>',
			};
			
			var url = "file/list" + (myUI.getCurrentFolder() ? "?path=" + myUI.getCurrentFolder() : "");
			$.get(url, function(data) {
				myUI.setCurrentFolder = data.me;
				var now = new Date();				
				data.folders.forEach(function(f){
					var div = $("<div></div>");
					div.className = "folderDiv";
					
					var name = $("<span></span>");
					name.html(f.name);
					name.click(function(){myUI.changeDir(f.id)});
					
					var ops = $("<div></div>");
					ops.className = "ops";
					
					div.append(name);
					div.append(ops);
					
					var tr = $("<tr></tr>");
					var td = $("<td></td>");
					body.append(tr.append(td.append(div)));
				});
				
				data.filePaths.forEach(function(fp) {
					var div = $("<div></div>");
					
					var name = $("<span></span>");
					name.html(fp.name);
					name.click(function(){myUI.downloadFile(fp.id)});
					
					var time = $("<span class='time'></span>");
					var lastModified = new Date(fp.lastModified);
					if (lastModified.getDate() == now.getDate() &&
							lastModified.getMonth() == now.getMonth())
						time.html(lastModified.toLocaleTimeString());
					else
						time.html(lastModified.toLocaleString());
					
					
					
					
					//ops.className = "ops";
					
					"remove revision share".split(' ').forEach(function(op) {
						var img = $("<img></img>");
						img.prop('src', opsImageUrls[op]);
						img.prop('height', '50');
						img.prop('width', '75');
						img.click(function() {
							myUI[op](fp.id);
						});
						div.append(img);
					})
					
					div.append(name);
					div.append(time);
					//div.append(ops);
					
					var tr = $("<tr></tr>");
					var td = $("<td></td>");
					body.append(tr.append(td.append(div)));
				});
				

			});	
		}
		

		var uploadDiv = myUI.createUploadDiv({
			cssClass : "uploadDiv",
			cssClassHover : "uploadDivHover",
			uploadCB : myUI.uploadFiles,
		});
		
		console.log(uploadDiv);
		var fr = $("<tr></tr>");
		fr.append(uploadDiv);

		var t = myUI.createTable({
			tableClassName : "fileTable",
			head : ["Your deel space"],
			firstRow : fr,
			dataCB : getDataAndMakeTableBody,
		});
		
		$('#filesContainer').append(t);	

		return;
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
		submit.id = "submitInput";
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
		//alert("Creating folder " + folderName + " in folder' s id " + father + " for user " + username + "!");

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
	
	function teamDialog() {
		createTeam();
	}
</script>
<title>Home</title>
</head>


<body>
	<div id="mainheader">
		<img src="<c:url value="/resources/img/logo.png"/>"/>
	</div>

	<div id="page">

		<nav id="personalInfo">
		<ul>
			<li>Welcome, <a href="<c:url value="/user/settingsProfile"/>"><c:out
						value="${user}"></c:out></a>!
			</li>
		</ul>
		</nav>

		<nav id="mainav">
		<ul>
			<li><a href="<c:url value="/home" />"
				onclick="javascript:goRoot()" class="active">home</a></li>
			<li><a href="javascript:uploadDialog()" id="uploadButton">upload</a></li>
			<li><a href="javascript:teamDialog()">team</a></li>
			<li><a href=<c:url value="/logout"/>>logout</a></li>
		</ul>
		</nav>

		<div id="wrapper"></div>
		<!-- 
		<div id="filesContainer">
			<table id="dataTable">
				<tr>
					<th>name</th>
				</tr>
				<tr>
					<td><form:form method="POST"
							action="javascript:createFolder();" id="addingFolder">
							<a href="javascript:sendFolderName()"
								style="text-decoration: none"> <img
								src="<c:url value="/resources/img/folder.png"/>" height="30"
								width="30">
							</a>
						</form:form></td>
				</tr>
			</table>
		</div>
		 -->

		<div id="uploadContainer">
			<form:form method="POST" commandName="fileForm" action="file/upload"
				name="ajaxForm" id="ajaxForm" enctype="multipart/form-data">

				<input type="file" value="Choose file" name="files[0]" />
				<input type="hidden" name="path" />
				<input type="submit" value="Invia" />
		</div>
		</form:form>

		<div id="sharingList">
			<label>Type for a user or a team :</label>
			</div>
		</div>

		<div id="revision">
			<table id="revisionTable">
				<tr>
					<th>Date</th>
				<tr>
			</table>
		</div>

		<div id="team">
			<ul id="teamList">
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