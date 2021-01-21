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
.buotton{
margin-top: 30px;
margin-left: 80px;
}
</style>
</head>
<body>
	<div class="fortune">
		<%
			OmikujiBean omikuji = (OmikujiBean) request.getAttribute("results");
		%>
		<div class="title">あなたの今日の運勢は、</div>
		<div class="unsei"><%=omikuji.getFortune_name()%>
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
		<div class="buotton">
		<a class="btn btn--green btn--emboss btn--cubic "
			href="/office_training_Servlet_JSP/InputBirthday">RETURN</a>
			<a href="/office_training_Servlet_JSP/halfMonthResults">半年の結果割合</a>
			<a href="/office_training_Servlet_JSP/sameBirthday">半年の誕生日結果</a>
	</div>
</body>
</html>