<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script language="JavaScript" type="text/javascript" src="jquery.min.js"></script>
<script language="JavaScript" type="text/javascript" src="jquery.js"></script>
	<script language="JavaScript" type="text/javascript" src="jquery-ui.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
	
	
<script type="text/javascript">
$(document).ready( function() 
		{
	alert('loading single document page');
			<%
				List<String[]> documents = (List<String[]>)request.getAttribute("results");
				for(String[] docs : documents)
				{ 
					String title = docs[1];
					String rbody = docs[2];
				%>
					$('#resultView').html("<%= rbody	%>");
					
					var title = "<%= title	%>";
					alert(title);
			<%	}
			%>
		});
</script>
</head>
<body>

<div id="resultView">
	
</div>
</body>
</html>