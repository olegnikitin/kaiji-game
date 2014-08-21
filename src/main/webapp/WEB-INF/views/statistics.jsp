<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>

<spring:message code="statistics.headTitle" var="head_title" />

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>${head_title} ${pageContext.request.userPrincipal.name}</title>
</head>
<body>

<jsp:include page="header.jsp"/>
<br>
<br>
<div class="container">

      <div id="roundsChart"></div>


</br>

<div id="gamesChart"></div>

</div>





<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>
<script
        src="<spring:url value="/resources/js/statistics.js" htmlEscape="true"/>"></script>

<script type="text/javascript">
var url = '/rest/management/statistic/${pageContext.request.userPrincipal.name}'
 charts('roundsChart', 'gamesChart', url);
</script>

</body>
</html>
