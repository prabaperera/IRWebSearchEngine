<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>IR Search Engine</title>
</head>
<body>
	<form method="get" action="SearchEngineServlet">
		<div>
			<table>
				<tr>
					<td><b>IR Search Engine</b> (Powered by Lucene)</td>
				</tr>
				<tr>
					<td><input type="text" align="middle" name="searchQuery" id="searchText"></td>
				</tr>
				<tr>
					<td><input type="submit" name="Search" id = "searchButton"></td>
				</tr>
			</table>
		</div>
	</form>
</body>
</html>