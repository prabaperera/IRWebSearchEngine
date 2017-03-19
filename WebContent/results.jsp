<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
	<!-- <script language="JavaScript" type="text/javascript" src="jquery.min.js"></script> 
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
	<script src="/jquery.js"></script>
	<script src="http://code.jquery.com/ui/1.10.0/jquery-ui.js"></script>
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script> -->
	
      <link href = "https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css"
         rel = "stylesheet">
      <script src = "https://code.jquery.com/jquery-1.10.2.js"></script>
      <script src = "https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
      
	

	
<script type="text/javascript">
$(document).ready( function() 
		{
	$("#indexProgress").hide();
	
	$('#indexBuild').click(function () {
		$("#indexProgress").show();
		$.ajax({
			url : 'SearchEngineServlet',
			data : {
				requestType : 'buildIndex'
			},
			success : function(json) 
			{
				$("#indexProgress").hide();
			},
			error: function(){
				$("#indexProgress").hide();
				alert("Error occured while building Index.");
			},
			dataType: 'json'
			});
	});
	
	$(".searchResult").click( function() {
	    alert('Click on Row');
	});
	
$('#searchButton').click(function () {
	var titleChecked = $("#inTitle").is(':checked');
	var bodyChecked = $("#inBody").is(':checked');
	
	var searchInside ;
	if(titleChecked && bodyChecked)
	{
		searchInside =  'title,body';
	}
	else if(titleChecked)
	{
		searchInside = 'title';
	}
	else if(bodyChecked)
	{
		searchInside = 'body';
	}
	
	$.ajax({
		url : 'SearchEngineServlet',
		data : {
			requestType : 'search',
			resultsPerPage : $( "#spinner" ).spinner().spinner( "value" ),
			searchIn : searchInside,
			searchQuery : $('#searchQuery').val()
		},
		success : function(json) 
		{
			var resultView = document.getElementById('resultView');
			var table = document.createElement('table');
			var html = '<table>';
			$.each(json, function(i, searchResult)
					{
						if(searchResult != null)
						{
							html += ('<tr class=\'searchResult\' id='+searchResult.documentId+'><td>'+ searchResult.title+'</td><td>['+searchResult.rank+' , '+searchResult.score+']</td></tr>');
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

$("#accordion").accordion({
    collapsible: true
  });
  
$( "#spinner" ).spinner();

		});
</script>
</head>
<body>
<div>
	<table>
		<tr>
			<td><input type="text" id="searchQuery" style="width:600px;height:30px;	font-size:20px"></td><td><button type="button" id="searchButton" style="width:30px;height:30px">...</button></td>
			<td>
			<div id="accordion">
			<h3>Settings</h3>
         	<div>
				<table>
				<tr>
				<td>
					<label for="spinner">Select a value:</label>
				</td>
				<td>
					<input id="spinner" name="value" value="10" style="width: 30px">
				</td>
					<!-- <td>No of results Per Page:</td><td><input type="text" id="resultsPerPage" value="10" ></td> -->
				</tr>
				<tr>
					<td>Search inside</td>
					<td>
					<table>
					<tr><td>Title <input title="Title" type="checkbox" id="inTitle" checked ></td></tr>
					<tr><td>Body <input title="Body" type="checkbox" id="inBody" checked></td></tr>
					<!-- <tr><td><img src="images/loading-bar.gif" style="width:100px;height:100px;"></td></tr> -->
					</table>
					</td>
				</tr>
				<tr><td><label id ="indexBuild"><U>build Index</U></label></td><td><img id="indexProgress" src="images/loading-bar.gif" style="width:100px;height:100px;"></td> </tr>
				</table>
			</div>
			</div>
			</td>
		</tr>
	</table>
	
</div>
<div id="resultView">
	
</div>
</body>
</html>