<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:message code="admin-page.gameHistory.gameInfo" var="gameInfo"/>
<spring:message code="admin-page.gameHistory.roundResults" var="roundResults"/>
<spring:message code="admin-page.gameHistory.winner" var="winner"/>
<spring:message code="admin-page.gameHistory.gameState" var="gameState"/>
<spring:message code="admin-page.gameHistory.windowpopup.resultColumn" var="resultColumn"/>
<spring:message code="admin-page.gameHistory.windowpopup.close" var="closeButton"/>
<spring:message code="admin-page.gameHistory.viewButton" var="viewButton"/>
<spring:message code="admin-page.operationColumn" var="operationColumn"/>
<spring:message code="admin-page.searchButton" var="searchButton"/>
<spring:message code="admin-page.deleteButton" var="deleteButton"/>
<spring:message code="admin-page.backButton" var="backButton"/>
<spring:message code="admin-page.gameInfo.gameName" var="gameName"/>
<spring:message code="admin-page.gameInfo.gameType" var="gameType"/>
<spring:message code="admin-page.gameInfo.cardsNum" var="cardsNum"/>
<spring:message code="admin-page.gameInfo.startTime" var="startTime"/>
<spring:message code="admin-page.gameInfo.endTime" var="endTime"/>
<spring:message code="admin-page.gameInfo.users" var="gameUsers"/>

<a href="${pageContext.servletContext.contextPath}/admin" class="btn btn-success btn-large active">${backButton}</a>
<br>
<form action="${pageContext.servletContext.contextPath}/admin/gamehistory" method="POST" class="navbar-form navbar-left">
    <div class="form-group">
        <input type="text" class="form-control" placeholder="Id" name="id"/>
    </div>
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
        <th>${gameInfo}</th>
        <th>${roundResults}</th>
        <th>${winner}</th>
        <th>${gameState}</th>
        <th>${operationColumn}</th>
    </tr>
    <c:choose>
        <c:when test="${not empty searchGameHistory}">
            <c:set var="histories" value="${searchGameHistory}"/>
        </c:when>
        <c:otherwise>
            <c:set var="histories" value="${gameHistoryList}"/>
        </c:otherwise>
    </c:choose>
    <c:forEach var="history" items="${histories}">
        <tr>
            <td>${history.id}</td>
            <td>
                <button class="btn btn-primary" data-toggle="modal" data-target="#gameInfo${history.id}">
                    ${viewButton}
                </button>
                <div class="modal fade" id="gameInfo${history.id}" tabindex="-1" role="dialog"
                     aria-labelledby="myModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span
                                        aria-hidden="true">&times;</span><span class="sr-only">${closeButton}</span></button>
                            </div>
                            <div class="modal-body" align="left">
                                <table class="table table-striped">
                                    <tr>
                                        <th>#</th>
                                        <th>${gameName}</th>
                                        <th>${gameType}</th>
                                        <th>${cardsNum}</th>
                                        <th>${startTime}</th>
                                        <th>${endTime}</th>
                                        <th>${gameUsers}</th>
                                    </tr>
                                    <tr>
                                        <td>${history.gameInfo.id}</td>
                                        <td>${history.gameInfo.gameName}</td>
                                        <td>${history.gameInfo.gameType}</td>
                                        <td>${history.gameInfo.numberOfCards}</td>
                                        <td>${history.gameInfo.gameStartTime}</td>
                                        <td>${history.gameInfo.gameFinishTime}</td>
                                        <td>
                                            <c:forEach var="user" items="${history.gameInfo.users}">
                                                ${user.nickname}
                                            </c:forEach>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">${closeButton}</button>
                            </div>
                        </div>
                    </div>
                </div>
            </td>
            <td>
                <button class="btn btn-primary" data-toggle="modal" data-target="#roundResult${history.id}">
                    ${viewButton}
                </button>
                <div class="modal fade" id="roundResult${history.id}" tabindex="-1" role="dialog"
                     aria-labelledby="myModalLabel"
                     aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span
                                        aria-hidden="true">&times;</span><span class="sr-only">${closeButton}</span></button>
                            </div>
                            <div class="modal-body" align="left">
                                <table class="table table-striped">
                                    <tr>
                                        <th>#</th>
                                        <th>${resultColumn}</th>
                                    </tr>
                                    <c:set var="i" value="1"/>
                                    <c:forEach var="result" items="${history.roundResults}">
                                        <tr>
                                            <td>${i}</td>
                                            <td>${result}</td>
                                        </tr>
                                        <c:set var="i" value="${i+1}"/>
                                    </c:forEach>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">${closeButton}</button>
                            </div>
                        </div>
                    </div>
                </div>
            </td>
            <td>
                <c:choose>
                    <c:when test="${history.winners.size() eq 0 }">-</c:when>
                    <c:otherwise>
                        <c:forEach var="winner" items="${history.winners}">
                            ${winner.nickname}
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </td>
            <td>${history.gameState}</td>
            <td>
                <form action="${pageContext.servletContext.contextPath}/admin/gamehistory/remove" method="POST">
                    <input type="submit" class="btn btn-default" value="${deleteButton}" style="background:#E1E1E1">
                    <input type="hidden" name="id" value="${history.id}">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>