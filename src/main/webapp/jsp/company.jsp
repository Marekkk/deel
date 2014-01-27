<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href=<c:url value="/resources/css/style.css"/> media="screen">
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script src="<c:url value='/resources/js/service.js'/>"></script>
<script src="<c:url value='/resources/js/myUI.js'/>"></script>
<title></title>
</head>
<script type="text/javascript">
 $(document).ready(function() {
	
	 $.ajax({
			url : 'list',
			type : 'GET',
			success : function(returndata) {
				if (returndata.status == "success") {
					var t = myUI.createTable({head: ["name"], 
									tableClassName: "companyTable",
									data: returndata.companies,
									cbRow: myUI.createRowForCompanyAdmin,
									firstRow: myUI.createFirstRowForCompany(),
									});
					$("#tableContainer").append(t);
				}
			}
		});
	 
	 
 })
</script>
<body>
<div id="tableContainer"></div>
</body>
</html>