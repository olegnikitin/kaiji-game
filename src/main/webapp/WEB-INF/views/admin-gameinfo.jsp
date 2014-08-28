<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:message code="admin-page.gameInfo.gameName" var="gameName"/>
<spring:message code="admin-page.gameInfo.gameType" var="gameType"/>
<spring:message code="admin-page.gameInfo.cardsNum" var="cardsNum"/>
<spring:message code="admin-page.gameInfo.startTime" var="startTime"/>
<spring:message code="admin-page.gameInfo.endTime" var="endTime"/>
<spring:message code="admin-page.gameInfo.users" var="gameUsers"/>
<spring:message code="admin-page.operationColumn" var="operationColumn"/>
<spring:message code="admin-page.searchButton" var="searchButton"/>
<spring:message code="admin-page.deleteButton" var="deleteButton"/>
<spring:message code="admin-page.backButton" var="backButton"/>

<a href="${pageContext.servletContext.contextPath}/admin" class="btn btn-success btn-large active">${backButton}</a>
<br>
<form action="${pageContext.servletContext.contextPath}/admin/gameinfo" method="POST" class="navbar-form navbar-left">
    <div class="form-group">
        <select size="1" name="userId" class="form-control">
            <option value="default"></option>
            <c:forEach var="user" items="${usersList}">
                <option value="${user.id}">${user.nickname}</option>
            </c:forEach>
        </select>
    </div>
    <button type="submit" class="btn btn-default" style="background:#E1E1E1">${searchButton}</button>
</form>

<table class="table table-striped">
    <tr>
        <th>#</th>
        <th>${gameName}</th>
        <th>${gameType}</th>
        <th>${cardsNum}</th>
        <th>${startTime}</th>
        <th>${endTime}</th>
        <th>${gameUsers}</th>
        <th>${operationColumn}</th>
    </tr>
    <c:choose>
        <c:when test="${not empty userGames}">
            <c:set var="games" value="${userGames}"/>
        </c:when>
        <c:otherwise>
            <c:set var="games" value="${gamesList}"/>
        </c:otherwise>
    </c:choose>
    <c:forEach var="game" items="${games}">
        <tr>
            <td>${game.id}</td>
            <td>${game.gameName}</td>
            <td>${game.gameType}</td>
            <td>${game.numberOfCards}</td>
            <td>${game.gameStartTime}</td>
            <td>${game.gameFinishTime}</td>
            <td>
                <ul>
                    <c:forEach var="user" items="${game.users}">
                        <li>${user.nickname}</li>
                    </c:forEach>
                </ul>
            </td>
            <td>
                <form action="${pageContext.servletContext.contextPath}/admin/gameinfo/remove" method="POST">
                    <input type="submit" class="btn btn-default" value="${deleteButton}" style="background:#E1E1E1">
                    <input type="hidden" name="id" value="${game.id}">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>