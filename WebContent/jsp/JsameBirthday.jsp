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
h2{
font-size: 25px;
margin-top: 90px;
margin-left: 60px;
}

table{
	font-family: arial narrow;
	font-size: 15px;
	width: 1100px;

	margin-top: 100px;
	margin-left: -330px;
	border-collapse: collapse;
	border-spacing: 0;
}

<%--テーブルデザイン--%>
.pastData{
	table-layout: auto;
}
thead, tbody {
    display: block;
}
tbody{
	 overflow-y: scroll;
    height: 200px;
}
.fixed01{
width: 300px;

}

<%--ヘッダー --%>
.pastData thead th {
	background:#C4A3BF;
	font-weight: bold;
	text-align: center;
}

<%--ボディ項目・フッター項目 --%>
.pastData tbody th,
.pastData tfoot th {
	background:#FEEEED;
}
<%--ボディデータ・フッターデータ --%>
.pastData tbody td,
.pastData tfoot td {
	text-align:center;
}

/* 偶数行 １行ごとの色変えが不要なら削除 */
.pastData tr:nth-child(2n) td {
    background: #C0C0C0;
}
/* 偶数行の項目 １行ごとの色変えが不要なら削除 */
.pastData tr{
    background: #FBFBF6;
}

.button, .btn, a.btn, button.btn {
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
	color: #000000;
	border-radius: 0.5rem;
	border: 0px none;
	font-family: arial narrow;
}

<%--ボタン--%>
a.btn--green.btn--emboss {
	color: #000000;
	text-shadow: -1px -1px 1px 55d8ff;
	border-bottom: 5px solid #C0C0C0;
	background: #FFFFFF;
}

a.btn--green.btn--cubic:hover {
	margin-top: 3px;
	border-bottom: 2px solid #C0C0C0;
}
button.btn--green.btn--emboss {
	color: #000000;
	text-shadow: -1px -1px 1px 55d8ff;
	border-bottom: 5px solid #C0C0C0;
	background: #FFFFFF;
}
button.btn--green.btn--cubic:hover {
	margin-top: 3px;
	border-bottom: 2px solid #C0C0C0;
}

.button{
	margin-top: 250px;
	margin-left: 100px;
}
</style>
</head>
<body>
	<h2>あなたの過去半年間の占い結果</h2>
	<table class=pastData>
	<thead>
		<tr>
			<th class="fixed01">占った日</th>
			<th class="fixed01">運勢</th>
			<th class="fixed01">願い事</th>
			<th class="fixed01">商い</th>
			<th class="fixed01">学問</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach var="omikuji" items="${pastBirhdayResults}">
			<tr>
				<td class="fixed01">${omikuji. resultsDate}</td>
				<td class="fixed01">${omikuji.ob.fortuneName}</td>
				<td class="fixed01">${omikuji.ob.wish}</td>
				<td class="fixed01">${omikuji.ob.business}</td>
				<td class="fixed01">${omikuji.ob.study}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>

	<div class="button">
	<button class="btn btn--green btn--emboss btn--cubic" type=button onclick="history.back()">RETURN</button>
	</div>
</body>
</html>