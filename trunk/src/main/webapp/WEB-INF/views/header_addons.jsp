<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags" %>
<c:set var="userName" value="${pageContext.request.userPrincipal.name}"></c:set>

<script src="/resources/js/header.js"></script>

<script type="application/javascript">

    <sec:authorize access="hasRole('USER_ROLE')">
    $(document).ready(function () {
        var element = document.getElementById("notificationMessage");
        element.innerHTML = ''
        connectToSessionServer()
        startSessionActivity('${userName}', 1000)
        if ('${param.socketActive}' != 'false') {
            connectToChatServerHeader()
        }
    })
    window.onbeforeunload = function (evt) {
        closeSessionWebSocket()
        resetInterval()
        if ('${param.socketActive}' != 'false') {
            closeChatWebSocket()
        }
    }
    </sec:authorize>
</script>