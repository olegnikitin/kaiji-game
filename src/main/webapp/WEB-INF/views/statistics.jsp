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