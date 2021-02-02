<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>半年の誕生日結果</title>
</head>
<body>
	<h2>本日入力された誕生日の過去半年の「占った日」+「運勢結果」</h2>
	<table>
		<tr>
			<th>占った日</th>
			<th>運勢</th>
			<th>願い事</th>
			<th>商い</th>
			<th>学問</th>
		</tr>
		<c:forEach var="omikuji" items="${pastBirhdayResults}">
			<tr>
				<td>${omikuji. results_date}</td>
				<td>${omikuji.ob.fortune_name}</td>
				<td>${omikuji.ob.wish}</td>
				<td>${omikuji.ob.business}</td>
				<td>${omikuji.ob.study}</td>
			</tr>
		</c:forEach>
	</table>
	<button type=button onclick="history.back()">戻る</button>
</body>
</html>