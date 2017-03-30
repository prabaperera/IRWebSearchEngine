<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
	
      <link href = "https://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css"
         rel = "stylesheet">
      <script src = "https://code.jquery.com/jquery-1.10.2.js"></script>
      <script src = "https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
      
	<style type="text/css">
	.ui-tabs .ui-tabs-nav
	{
	background: white;
	border: 0px;
	padding: 0px;
	}

	.ui-tabs .ui-tabs-panel /* just in case you want to change the panel */
	{
		background: white;
		border: 0px;
		padding: 0px;
	}
	div.tabDiv{
	background-color : while;
	border: 0px;
	padding: 0px;
	}
	</style>

	
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
	
	$.ajax({
		url : 'SearchEngineServlet',
		data : {
			requestType : 'search',
			resultsPerPage : $( "#spinner" ).spinner().spinner( "value" ),
			searchIn : 'title,body',
			searchQuery : $('#searchQuery').val()
		},
		success : function(json) 
		{
			buildResultBody(json);
		},
		error: function(){
			alert("Error occured");
		},
		dataType: 'json'
		});
});


$('#advSearchButton').click(function () {
	var titleQueryPara = $("#titleQuery").val();
	var bodyQueryPara = $("#bodyQuery").val();
	
	$.ajax({
		url : 'SearchEngineServlet',
		data : {
			requestType : 'advSearch',
			resultsPerPage : $( "#spinnerAdv" ).spinner().spinner( "value" ),
			titleQuery : titleQueryPara,
			bodyQuery : bodyQueryPara
		},
		success : function(json) 
		{
			buildResultBody(json);
		},
		error: function(){
			alert("Error occured");
		},
		dataType: 'json'
		});
});

function buildResultBody(json)
{
	var resultView = document.getElementById('resultView');
	var table = document.createElement('table');
	var html = '<div>';
	
	$.each(json, function(i, searchResult)
			{
				if(searchResult != null)
				{
					html += ('<div style="margin-bottom: 15px;" class=\'searchResult\' id='+searchResult.documentId+'><h3 style="margin:0px;">'+ searchResult.title+'</h3><div> rank: '+searchResult.rank+' , Score: '+searchResult.score+'</div></div>');
				}
			});
	html += '</div>';
	resultView.innerHTML = html;
}

$("#accordion").accordion({
    collapsible: true
  });
  
$( "#spinner" ).spinner();

$( "#spinnerAdv" ).spinner();

$( "#tabs" ).tabs();

		});
</script>
</head>
<body style="width: 60%;margin-left: 20%;background-color:while;background-color: white;">

<div id="tabs" style="width: 100%; height:100%; background-color: white; border: 0px;padding: 0px; margin-top: 30px;">
  <ul>
    <li><a href="#tabs-1">Basic Search</a></li>
    <li><a href="#tabs-2">Advanced Search</a></li>
    <li><a href="#tabs-4">Applications</a></li>
    <li><a href="#tabs-3">Settings</a></li>
  </ul>
  <div id="tabs-1" class="tabDiv" style="background-color:while;background-color: white;padding: 0px">
  	<table>
  		<tr>
			<td><input type="text" id="searchQuery" style="width:600px;height:30px;	font-size:20px" placeholder="keyword or phrase"></td>
			<td><button type="button" id="searchButton" style="width:80px;height:30px">Search</button></td>
		</tr>
		<tr>
		<td>
						<table>
						<tr>
						<td><label for="spinner">Page Size:</label></td>
						<td><input id="spinner" name="value" value="10" style="width: 30px"></td>
						</tr>
						</table>
						
					</td>
					</tr>
	</table>
  </div>
  <div id="tabs-2" class="tabDiv" style="background-color:while;background-color: white;">
  	<table>
  		<tr>
  			<td><input type="text" id="titleQuery" style="width:300px;height:30px;	font-size:20px;" placeholder="title"></td>
  			<td><input type="text" id="bodyQuery" style="width:300px;height:30px;	font-size:20px" placeholder="body"></td>
  			<td><button type="button" id="advSearchButton" style="width:80px;height:30px">Search</button></td>
  		</tr>
  		<tr>
		<td>
						<table>
						<tr>
						<td><label for="spinner">Page Size:</label></td>
						<td><input id="spinnerAdv" name="value" value="10" style="width: 30px"></td>
						</tr>
						</table>
						
					</td>
					</tr>
  	</table>
  </div>
  <div id="tabs-4" class="tabDiv" style="background-color:while;background-color: white;">
  	<p><b>Applications</b></p>
  </div>
  <div id="tabs-3" class="tabDiv" style="background-color:while;background-color: white;">
  	<table><tr><td><label id ="indexBuild"><U>build Index</U></label></td><td><img id="indexProgress" src="images/loading-bar.gif" style="width:100px;height:100px;"></td> </tr></table>
  </div>
</div>
<hr/>
<div id="resultView">
	
</div>
</body>
</html>