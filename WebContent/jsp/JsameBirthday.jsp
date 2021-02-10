<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>半年の誕生日結果</title>
<style>
body {
	margin-left: 500px;
	margin-top: 50px;
	background: #EEE8AA;
}

<%--共通ボタン-- %>
<%--まずはお決まりのボックスサイズ算出をborer-boxに --%>
*, *:before, *:after {
	-webkit-box-sizing: inherit;
	box-sizing: inherit;
}

html {
	-webkit-box-sizing: border-box;
	box-sizing: border-box;
	font-size: 62.5%; /*rem算出をしやすくするために*/
}

.btn, a.btn, button.btn {
	font-size: 1.6rem;
	font-weight: 700;
	line-height: 1.5;
	position: relative;
	display: inline-block;
	padding: 1rem 4rem;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	-webkit-transition: all 0.3s;
	transition: all 0.3s;
	text-align: center;
	vertical-align: middle;
	text-decoration: none;
	letter-spacing: 0.1em;
	color: #212529;
	border-radius: 0.5rem;
}

<%--ボタン--%>
a.btn--green.btn--emboss {
	color: #0090bb;
	fontfamily: Microsoft Sans Serif;
	text-shadow: -1px -1px 1px 55d8ff;
	border-bottom: 5px solid #0090bb;
	background: #00b7ee;
}

a.btn--green.btn--cubic:hover {
	margin-top: 3px;
	border-bottom: 2px solid #0090bb;
}

.buotton {
	margin-top: 30px;
	margin-left: 80px;
}
</style>
</head>
<body>
	<h2>あなたの過去半年間の占い結果</h2>
	※いま入力した誕生日を条件に過去半年のおみくじ結果を一覧で取得
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
	<div class="buotton">
	<button class="btn btn--green btn--emboss btn--cubic" type=button onclick="history.back()">return</button>
	</div>
</body>
</html>