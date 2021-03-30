<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>半年の結果割合</title>
<style>
body {
	margin-left: 500px;
	margin-top: 70px;
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

h2 {
	font-size: 25px;
	margin-top: -10px;
	margin-left: 150px;
}

.harfMonth {
	font-size: 18px;
	margin-top: 100px;
	margin-left: -180px;
}

.today {
	font-size: 18px;
	margin-top: -188px;
	margin-left: 300px;
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

.button {
	margin-top: 30px;
	margin-left: 150px;
}

<%--テーブルデザイン--%>
.allData{
	font-size: 18px;
	margin-top: 10px;
	margin-left: -440px;
}

.AllDataTable{
	table-layout: auto;
	margin-top: 10px;
	margin-left: -10px;
}

.severalData{
	font-size: 18px;
	margin-top: 10px;
	margin-left: -440px;
}
.severalDataTable{
	table-layout: auto;
	margin-top: 10px;
	margin-left: -10px;
}

thead, tbody {
	display: block;
}
tbody {
	overflow-y: scroll;
	height: 200px;
}
.fixed01 {
	width: 300px;
}
</style>
</head>

<body>
	<h2>各運勢の割合</h2>

	<div class=allData>
		●過去半年の全体のおみくじ運勢結果の割合 <br>
	<table class=AllDataTable>
		<thead>
			<tr>
				<th class="fixed01">大吉</th>
				<th class="fixed01">中吉</th>
				<th class="fixed01">小吉</th>
				<th class="fixed01">末吉</th>
				<th class="fixed01">吉</th>
				<th class="fixed01">凶</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="percent" items="${resultsPercentList}">
				<tr>
					<td class="fixed01">${percent}</td>
				</tr>
			</c:forEach>
		</tbody>
		</table>
</div>

		<div class=severalData>
			●本日の誕生日のおみくじ運勢結果の割合<br>
		<table class=severalDataTable>
			<thead>
				<tr>
					<th class="fixed01">大吉</th>
					<th class="fixed01">中吉</th>
					<th class="fixed01">小吉</th>
					<th class="fixed01">末吉</th>
					<th class="fixed01">吉</th>
					<th class="fixed01">凶</th>
				</tr>
			</thead>
			<tbody>
					<tr>
						<td class="fixed01">${resultsTodayList[0]}</td>
						<td class="fixed01">${resultsTodayList[1]}</td>
						<td class="fixed01">${resultsTodayList[2]}</td>
						<td class="fixed01">${resultsTodayList[3]}</td>
						<td class="fixed01">${resultsTodayList[4]}</td>
						<td class="fixed01">${resultsTodayList[5]}</td>
					</tr>
				</tbody>
				</table>
</div>
				<div class="button">
					<button class="btn btn--green btn--emboss btn--cubic" type=button
						onclick="history .back()">RETURN</button>
				</div>
</body>
</html>
