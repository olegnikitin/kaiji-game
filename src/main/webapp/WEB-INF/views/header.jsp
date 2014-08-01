<%@ page language="java" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<spring:message code="header.kaiji" var="headerKaiji" />
<spring:message code="header.login" var="headerLogin" />
<spring:message code="header.logout" var="headerLogout" />
<spring:message code="header.language" var="headerLanguage" />
<spring:message code="header.settings" var="headerSettings" />
<spring:message code="header.dao" var="headerDAO" />
<spring:message code="header.welcome" var="headerWelcome" />
<spring:message code="header.statistic" var="headerStatistic" />
<spring:message code="header.register" var="headerRegister" />

<html>
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link
            href="${pageContext.servletContext.contextPath}/resources/css/bootstrap.min.css"
            rel="stylesheet">

</head>
<body>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span> <span
                    class="icon-bar"></span> <span class="icon-bar"></span> <span
                    class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href='<spring:url value="/" htmlEscape="true"/>'>
                ${headerKaiji}
            </a>
        </div>

       <div class="navbar-brand">
           <c:choose>
        <c:when test="${pageContext.request.userPrincipal.name != null}">
            ${headerWelcome} : ${pageContext.request.userPrincipal.name} |
               <a href="<c:url value="/statistic/user" />" > ${headerStatistic}  <span class="glyphicon glyphicon-stats"></span> </a>
            | <a href="<c:url value="/j_spring_security_logout" />" > ${headerLogout}   <span class="glyphicon glyphicon-share"></span></a>
        </c:when>
               <c:otherwise>
                   <a href="<c:url value="/login" />" > ${headerLogin}</a>  /  <a href="<c:url value="/registration" />" >${headerRegister}</a>
               </c:otherwise>
           </c:choose>
       </div>

        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">${headerLanguage} <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="?lang=en">en</a></li>
                    <li><a href="?lang=ru">ru</a></li>
                    <li><a href="?lang=ua">ua</a></li>
                </ul>
            </li>

            <sec:authorize access="hasRole('ADMIN_ROLE')">
            <li><a href="<spring:url value="/config" htmlEscape="true"/>"><b class="glyphicon glyphicon-wrench"> </b> ${headerSettings}</a></li>
            <li><a href="<spring:url value="/dao" htmlEscape="true"/>"><b class="glyphicon glyphicon-user"> </b> ${headerDAO}</a></li>

            </sec:authorize>
            <sec:authorize access="hasRole('USER_ROLE')">
            <li><a href="<spring:url value="/gamechat" htmlEscape="true"/>"><b class="glyphicon glyphicon-user"> </b>Chat</a></li>
            </sec:authorize>
        </ul>
    </div>
</div>

</br></br></br>
<c:if test="${notification ne null}">
<div class="container">
    <div class="alert alert-warning alert-dismissable">
    <button type="button" class="close" data-dismiss="alert">&times;</button>

         <h3>   ${notification} </h3>

    </div>
</div>
</c:if>

<script
        src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>


<script
        src="<spring:url value="/resources/js/bootstrap.min.js" htmlEscape="true"/>"></script>

<script
        src="http://fgnass.github.io/spin.js/spin.js"></script>
<script
        src="<spring:url value="/resources/js/spinner.opts.js" htmlEscape="true"/>"></script>

</body>
</html>