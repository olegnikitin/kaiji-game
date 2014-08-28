<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

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