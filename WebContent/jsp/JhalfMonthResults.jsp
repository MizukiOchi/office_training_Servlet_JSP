<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>半年の結果割合</title>
</head>
<body>
各運勢の割合<br>
		<c:forEach var="percent" items="${resulesPercentList}">
		${percent}<br>
		</c:forEach>
<button type=button onclick="history.back()">戻る</button>
</body>
</html>