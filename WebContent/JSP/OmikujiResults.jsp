<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>おみくじ結果</title>
</head>
<body>
	<div class="fortune">
		<h3>今日のあなたの運勢は、＿＿です。</h3>
		<form action="/office_training_Servlet_JSP/InputBirthday" method="get">
			<input type="submit" value="もう一度占う" />
		</form>
	</div>
</body>
</html>