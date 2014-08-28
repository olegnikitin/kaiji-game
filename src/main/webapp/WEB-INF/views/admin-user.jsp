<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:message code="admin-page.user.name" var="userName"/>
<spring:message code="admin-page.user.nickname" var="userNickname"/>
<spring:message code="admin-page.user.email" var="userEmail"/>
<spring:message code="admin-page.user.role" var="userRole"/>
<spring:message code="admin-page.user.createButton" var="userCreateButton"/>
<spring:message code="admin-page.operationColumn" var="operationColumn"/>
<spring:message code="admin-page.searchButton" var="searchButton"/>
<spring:message code="admin-page.deleteButton" var="deleteButton"/>
<spring:message code="admin-page.backButton" var="backButton"/>

<a href="${pageContext.servletContext.contextPath}/admin" class="btn btn-success btn-large active">${backButton}</a>
<br>
<form:form action="${pageContext.servletContext.contextPath}/admin/users/save" commandName="newUser" class="navbar-form">
    <div class="input-group">
        <form:input path="name" class="form-control" placeholder="${userName}"/>
    </div>
    <div class="input-group">
        <form:input path="nickname" class="form-control" placeholder="${userNickname}"/>
    </div>
    <div class="input-group" style="width: 178px">
        <span class="input-group-addon">@</span>
        <form:input path="email" class="form-control" placeholder="${userEmail}"/>
    </div>

    <button type="submit" class="btn btn-default" style="background:#EAEFFA">${userCreateButton}</button>
</form:form>

<form action="${pageContext.servletContext.contextPath}/admin/users" method="POST" class="navbar-form navbar-left">
    <div class="form-group">
        <input type="text" class="form-control" placeholder="${userNickname}" name="nickname">
    </div>
    <div class="form-group">
        <input type="text" class="form-control" placeholder="${userEmail}" name="email">
    </div>
    <button type="submit" class="btn btn-default" style="background:#E1E1E1">${searchButton}</button>
</form>

<table class="table table-striped">
    <tr>
        <th>#</th>
        <th>${userName}</th>
        <th>${userNickname}</th>
        <th>${userEmail}</th>
        <th>${userRole}</th>
        <th>${operationColumn}</th>
    </tr>
    <c:choose>
        <c:when test="${not empty user}">
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.nickname}</td>
                <td>${user.email}</td>
                <td>${user.roles}</td>
                <td>
                    <form action="${pageContext.servletContext.contextPath}/admin/users/remove" method="POST">
                        <input type="submit" class="btn btn-default" value="${deleteButton}" style="background:#E1E1E1">
                        <input type="hidden" name="id" value="${user.id}">
                    </form>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <c:forEach var="user" items="${usersList}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.nickname}</td>
                    <td>${user.email}</td>
                    <td>${user.roles}</td>
                    <td>
                        <form action="${pageContext.servletContext.contextPath}/admin/users/remove" method="POST">
                            <input type="submit" class="btn btn-default" value="${deleteButton}" style="background:#E1E1E1">
                            <input type="hidden" name="id" value="${user.id}">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</table>