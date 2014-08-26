<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<tiles:insertAttribute name="titles" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link
	href="${pageContext.servletContext.contextPath}/resources/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${pageContext.servletContext.contextPath}/resources/css/styles.css"
	rel="stylesheet">
<sec:authorize access="hasRole('USER_ROLE')">
<%--	<script type="text/javascript"
		src="<tiles:getAsString name="header_script" />"></script>--%>
</sec:authorize>
<script type="text/javascript"
	src="//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>

<title><tiles:getAsString name="title" /></title>
</head>


<body>
	<div class="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div class="container_main">
		<div class="left_column">
			<img src="http://static.zerochan.net/Itou.Kaiji.full.665898.jpg"
				width="100%" alt="left column" />
		</div>
		<div class="main_div">
			<tiles:insertAttribute name="content" />
		</div>
		<div class="right_column">
			<img src="http://static.zerochan.net/Itou.Kaiji.full.665898.jpg"
				width="100%" alt="left column" />
		</div>
	</div>
	<div class="footer">
		<a id="about" href="/about">About us</a>
	</div>
</body>
</html>