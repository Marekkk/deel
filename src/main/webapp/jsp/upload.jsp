<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
   <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Upload Page</title>
		<link href="./resources/css/style.css" rel="stylesheet" type="text/css" />
		
		<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
		<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
		
		 <!-- Google web fonts -->
        <link href="http://fonts.googleapis.com/css?family=PT+Sans+Narrow:400,700" rel='stylesheet' />
        
        <!-- The main CSS file -->
        <link href="./resources/miniuploadform/assets/css/style.css" rel="stylesheet" />
		
		
	</head>
	<body>
		<header id="mainheader">
			<h3><a id="title">drop<span>box</span>~</a></h3>
		</header>
		
		<div id="page">
		
		<nav id="personalInfo">
			<ul>
				<li>Welcome, <a href="#"><c:out value="${user}"></c:out></a>!</li>
			</ul>
		</nav>
		
		<nav id="mainav">
			<ul>
				<li><a href="home">home</a></li>
				<li><a href="login.html">logout</a></li>
				<li class="active"><a href="upload.html">upload</a></li>
			</ul>
		</nav>
		</div>
		
		<div id="dropbox">
		<form:form method="POST" commandName="fileForm" action="file/upload"
			name="upload" id="upload" enctype="multipart/form-data">
            <div id="drop">
                Drop Here

                <a>Browse</a>
                <input type="file" name="files[0]" multiple />
                <input type="hidden" name="path"/>
                <input type="file" value="Choose file" name="files[0]" />
            </div>

            <ul>
                <!-- The file uploads will be shown here -->
            </ul>
            
                    <!-- JavaScript Includes -->
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
                    <script src="./resources/miniuploadform/assets/js/jquery.knob.js"></script>

                    <!-- jQuery File Upload Dependencies -->
                    <script src="./resources/miniuploadform/assets/js/jquery.ui.widget.js"></script>
                    <script src="./resources/miniuploadform/assets/js/jquery.iframe-transport.js"></script>
                    <script src="./resources/miniuploadform/assets/js/jquery.fileupload.js"></script>

                    <!-- Our main JS file -->
                    <script src="./resources/miniuploadform/assets/js/script.js"></script>

        </form:form>
		</div>
		
		<script type="text/javascript">
			$(document).ready(function() {
				$('input[name="path"]').val(sessionStorage.getItem("dir"));
			})
		</script>
		
		<footer>
			<div id="footerSection">
				<p>Electric Sheep 2014</p>
			</div>
		</footer>
	</body>
</html>