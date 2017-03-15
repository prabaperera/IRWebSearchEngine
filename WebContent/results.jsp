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
	$(".searchResult").click( function() {
	    alert('Click on Row');
	});
	
$('#searchButton').click(function () {
	$.ajax({
		url : 'SearchEngineServlet',
		data : {
			requestType : 'search',
			resultsPerPage : $('#resultsPerPage').val(),
			searchQuery : $('#searchQuery').val()
		},
		success : function(json) 
		{
			alert('success');
			var resultView = document.getElementById('resultView');
			var table = document.createElement('table');
			var html = '<table>';
			$.each(json, function(i, searchResult)
					{
						if(searchResult != null)
						{
							html += ('<tr class=\'searchResult\' id='+searchResult.documentId+'><td>'+ searchResult.title+'</td></tr>');
						}
					});
			html += '</table>';
			resultView.innerHTML = html;
		},
		error: function(){
			alert("Error occured");
		},
		dataType: 'json'
		});
});
		});
</script>
</head>
<body>
<div>
	<table>
		<tr>
			<td><input type="text" id="searchQuery"></td><td><button type="button" id="searchButton">Search</button></td>
		</tr>
		<tr>
			<td><input type="text" id="resultsPerPage" value="10" ></td><td></td>
		</tr>
	</table>
</div>
<div id="resultView">
	
</div>
</body>
</html>