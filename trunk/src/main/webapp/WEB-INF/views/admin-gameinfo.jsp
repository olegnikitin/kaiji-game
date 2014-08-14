<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title></title>
</head>
<body>
<jsp:include page="header.jsp"/>
<a href="${pageContext.servletContext.contextPath}/dao" class="btn btn-success btn-large active">Back</a>
<br>
<form action="${pageContext.servletContext.contextPath}/dao/gameinfo" method="POST" class="navbar-form navbar-left">
    <div class="form-group">
        <select size="1" name="userId" class="form-control">
            <option value="default"></option>
            <c:forEach var="user" items="${usersList}">
                <option value="${user.id}">${user.nickname}</option>
            </c:forEach>
        </select>
    </div>
    <button type="submit" class="btn btn-default" style="background:#E1E1E1">Search</button>
</form>

<table class="table table-striped">
    <tr>
        <th>#</th>
        <th>Game name</th>
        <th>Game type</th>
        <th>Num of cards</th>
        <th>Start time</th>
        <th>End time</th>
        <th>Users</th>
        <th>Operations</th>
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
                <form action="${pageContext.servletContext.contextPath}/dao/gameinfo/remove" method="POST">
                    <input type="submit" class="btn btn-default" value="Delete" style="background:#E1E1E1">
                    <input type="hidden" name="id" value="${game.id}">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
