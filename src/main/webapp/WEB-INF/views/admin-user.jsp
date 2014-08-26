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
<a href="${pageContext.servletContext.contextPath}/admin" class="btn btn-success btn-large active">Back</a>
<br>
<form:form action="${pageContext.servletContext.contextPath}/admin/users/save" commandName="newUser" class="navbar-form">
    <div class="input-group">
        <form:input path="name" class="form-control" placeholder="Name"/>
    </div>
    <div class="input-group">
        <form:input path="nickname" class="form-control" placeholder="Nickname"/>
    </div>
    <div class="input-group" style="width: 178px">
        <span class="input-group-addon">@</span>
        <form:input path="email" class="form-control" placeholder="Email"/>
    </div>

    <button type="submit" class="btn btn-default" style="background:#EAEFFA">Create user</button>
</form:form>

<form action="${pageContext.servletContext.contextPath}/admin/users" method="POST" class="navbar-form navbar-left">
    <div class="form-group">
        <input type="text" class="form-control" placeholder="Nickname" name="nickname">
    </div>
    <div class="form-group">
        <input type="text" class="form-control" placeholder="Email" name="email">
    </div>
    <button type="submit" class="btn btn-default" style="background:#E1E1E1">Search</button>
</form>

<table class="table table-striped">
    <tr>
        <th>#</th>
        <th>Name</th>
        <th>Nickname</th>
        <th>Email</th>
        <th>Role</th>
        <th>Operations</th>
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
                        <input type="submit" class="btn btn-default" value="Delete" style="background:#E1E1E1">
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
                            <input type="submit" class="btn btn-default" value="Delete" style="background:#E1E1E1">
                            <input type="hidden" name="id" value="${user.id}">
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</table>
</body>
</html>
