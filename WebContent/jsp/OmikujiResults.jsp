<%@page import="Bean.OmikujiBean"%>
<%@page import="Servlet.ChangeToResults"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>おみくじ結果</title>
<%-- <link href="../css/stylesheet.css"> --%>

<%--↓↓↓結果出力画面のレイアウト↓↓↓ --%>
<style>
body {
	margin-left: 500px;
	margin-top: 50px;
	background: #EEE8AA;
}
.fortune {
	color: #708090;
	font-family: fantasy;
	margin-top: 10px;
	margin-left: -50px;
}
.title {
	font-size: 30px;
}
.unsei {
	font-size: 35px;
	margin-left: 100px;
	color: #CD5C5C;
}
.wish {
	font-size: 20px;
}
.business {
	font-size: 20px;
}
.study {
	font-size: 20px;
}
<%--画像--%>
.panda{
margin-left: 400px;
margin-top: 50px;
width: 300px;
height: 300px;
}
<%--共通ボタン--%>
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
margin-top: 30px;
margin-left: -200px;
}
</style>
<%--↑↑↑結果出力画面のレイアウト↑↑↑ --%>
</head>

<%--↓↓↓結果出力画面の内容↓↓↓ --%>
<body>
	<div class="fortune">
		<%
			OmikujiBean omikuji = (OmikujiBean) request.getAttribute("results");
		%>
		<div class="title">あなたの今日の運勢は、</div>
		<div class="unsei"><%=omikuji.getFortuneName()%>
		</div>
		<div class="wish">
			願い事：<%=omikuji.getWish()%></div>
		<div class="business">
			商い：<%=omikuji.getBusiness()%></div>
		<div class="study">
			学問：<%=omikuji.getStudy()%><br>
		</div >
		</div>
		<img class="panda" src="img/panda.jpg">
		<div class="button">
			<button class="btn btn--green btn--emboss btn--cubic" type=button onclick="history.back()">RETURN</button>
			<a class="btn btn--green btn--emboss btn--cubic" href="/office_training_Servlet_JSP/HalfMonthResults">RESULTS OF PERCENTAGE</a>
			<a class="btn btn--green btn--emboss btn--cubic" href="/office_training_Servlet_JSP/SameBirthday">YOUR FORTUNE RESULTS</a>
	</div>
</body>
<%--↑↑↑結果出力画面の内容↑↑↑ --%>

</html>