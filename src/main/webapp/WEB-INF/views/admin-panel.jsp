<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="admin-page.panel.usersLink" var="usersLink"/>
<spring:message code="admin-page.panel.gameInfoLink" var="gameInfoLink"/>
<spring:message code="admin-page.panel.gameHistoryLink" var="gameHistoryLink"/>

<ul class="nav nav-pills nav-stacked">
    <li><a href="/admin/users">${usersLink}</a></li>
    <li><a href="/admin/gameinfo">${gameInfoLink}</a></li>
    <li><a href="/admin/gamehistory">${gameHistoryLink}</a></li>
</ul>