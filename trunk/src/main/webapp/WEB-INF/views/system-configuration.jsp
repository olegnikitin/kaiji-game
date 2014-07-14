<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<spring:message code="system_configuration.headTitle" var="head_title" />
<spring:message code="system_configuration.homeReference" var="home_reference" />
<spring:message code="system_configuration.defBotType" var="def_bot_type" />
<spring:message code="system_configuration.easyBot" var="easy_bot" />
<spring:message code="system_configuration.mediumBot" var="medium_bot" />
<spring:message code="system_configuration.hardBot" var="hard_bot" />
<spring:message code="system_configuration.gameName" var="game_name" />
<spring:message code="system_configuration.userName" var="user_name" />
<spring:message code="system_configuration.numberOfCards" var="number_of_cards" />
<spring:message code="system_configuration.roundTimeout" var="round_timeout" />
<spring:message code="system_configuration.gameConnectionTimeout" var="game_connection_timeout" />
<spring:message code="system_configuration.refresh" var="refresh" />
<spring:message code="system_configuration.save" var="save" />

<html>
<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>${head_title}</title>


</head>
<body>

<jsp:include page="header.jsp"/>

	<div class="container">

		<div class="row">

			<div class="col-md-6 col-md-offset-3">
			
				<form:form action="${pageContext.servletContext.contextPath}/config/handler" method="post"
				           modelAttribute="systemConfiguration" role="form">
				    ${def_bot_type}:
				    <form:select path="botType"  class="form-control">
				        <form:option value="EASY" label="${easy_bot}"/>
				        <form:option value="MEDIUM" label="${medium_bot}"/>
				        <form:option value="HARD" label="${hard_bot}"/>
				    </form:select><br>
				    ${game_name}: <form:input type="text" path="gameName" size="20"  class="form-control"/><br>
				    ${user_name}: <form:input type="text" path="userName" size="20"  class="form-control"/><br>
				    ${number_of_cards}: <form:input type="text" path="numberOfCards" size="20" class="form-control"/><br>
				    ${round_timeout}: <form:input type="text" path="gameConnectionTimeout" size="20" class="form-control"/><br>
                    ${game_connection_timeout}: <form:input type="text" path="roundTimeout" size="20" class="form-control"/><br>
				    <br><br>
				    <form:button name="action" value="refresh" class="btn btn-default">${refresh}</form:button>
				    <form:button name="action" value="save" class="btn btn-primary">${save}</form:button>
				</form:form>
				
			</div>
		</div>
	</div>

</body>
</html>