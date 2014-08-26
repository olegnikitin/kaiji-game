<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="pl" uri="/WEB-INF/tld/play-game.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>

<spring:message code="join-game.headTitle" var="head_title"/>
<spring:message code="join-game.enterGameName" var="enter_game_name"/>
<spring:message code="join-game.searchButton" var="search_button"/>
<spring:message code="join-game.numberLabel" var="number_label"/>
<spring:message code="join-game.titleLabel" var="title_label"/>
<spring:message code="join-game.hostLabel" var="host_label"/>
<spring:message code="join-game.cardsLabel" var="cards_label"/>
<spring:message code="join-game.connecting" var="connecting"/>

<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">


    <title>${head_title}</title>

</head>
<body>

<spring:url value="/game/new/join" var="url"/>
<jsp:include page="header.jsp"/>


<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<script src="/resources/js/autocompleter.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="/resources/js/eventsource.min.js"></script>

<br> <br> <br>

<div class="container">
    <form class="form-inline" role="form">
        <input type="text" class="form-control col-md-8" id="gameName" name="gameName"
               placeholder="${enter_game_name}"/>
        <a href="#" class="btn btn-default col-md-2" id="search">${search_button}</a>
    </form>
    </br>
    </br>
    </br>
    <div id="json" class="well well-sm" style="display: none;"></div>

    </br>
    </br>

    <script>
        $(document).ready(function () {
            updateGames();
        })

        function updateGames() {
            var eventSource = new EventSource("/joingame/update");
            eventSource.onmessage = function (event) {
                $('#games').empty();
                var removeUrl = '';
                var url = '';
                var $createdGames = $('<tr><th>' + '${number_label}'
                        + '</th><th>' + '${title_label}'
                        + '</th><th>' + '${host_label}'
                        + '</th><th>' + '${cards_label}'
                        + '</th></tr>');
                $('#games').append($createdGames);
                var msg = JSON.parse(event.data);
                msg.forEach(
                        function fillTable(game) {
                            url = '<a class =\"btn btn-primary btn-xs\" href="new/join?gameName='
                                    + game.gameName + '&infoId=' + game.id + '\">Join</a>';
                            <sec:authorize access="hasRole('ADMIN_ROLE')">
                            removeUrl = '<a class=\"label label-danger\" href ="/game/cleanup/' + game.id
                                    + '\">Remove</a>'
                                    </sec:authorize>
                            console.log(url);
                            console.log(removeUrl);
                            $('#games').append(
                                            '<tr><td>' + game.number
                                            + '</td><td>' + game.gameName
                                            + '</td><td>' + game.players
                                            + '</td><td>' + game.numberOfCards
                                            + '</td><td>' + url
                                            + '</td><td>' + removeUrl +'</td></tr>')

                        })
            };
        }
        ;
    </script>


    <table class="table table-striped" id="games">

        <tr>
            <th>${number_label}</th>
            <th>${title_label}</th>
            <th>${host_label}</th>
            <th>${cards_label}</th>
        </tr>

        <c:forEach var="game" items="${openedGames}"
                   varStatus="rowCounter">

            <spring:url value="/game/new/join" var="urlGame">
                <spring:param name="gameName" value="${game.gameName}"/>
                <spring:param name="infoId" value="${game.id}"/>
            </spring:url>
            <tr>
                <td>${rowCounter.count}</td>
                <td>${game.gameName}</td>
                <td>${game.players}</td>
                <td>${game.numberOfCards}</td>
                <td><a href="${urlGame}" onclick="WaitDiv('wait-${rowCounter.count}', 'spin-${rowCounter.count}');"
                       class="btn btn-primary btn-xs">Join</a></td>
                <sec:authorize access="hasRole('ADMIN_ROLE')">
                    <td><a href="<spring:url value="/game/cleanup/${game.id}" htmlEscape="true"/>"
                           class="label label-danger"><b class="glyphicon glyphicon-remove"></b> Remove</a></td>
                </sec:authorize>


                <td>
                    <div id="wait-${rowCounter.count}" style="display: none;" class="well well-sm">
                            ${connecting}
                        <span id="spin-${rowCounter.count}"
                              style="position: relative;display: block;top: -10px;left: 50%;"></span>
                    </div>
                <td>
            </tr>
        </c:forEach>
    </table>

</div>

<script type="text/javascript">

    function WaitDiv(it, spin) {
        document.getElementById(it).style.display = 'block';
        var target = document.getElementById(spin);
        var spinner = new Spinner(joinOpts).spin(target);
    }

    $('#search').click(function () {
        $('#json').hide();
        var query = $('#gameName').val();
        var url = "/rest/info/" + query;
        var id;
        var gname;

        $.getJSON(url)
                .done(function (obj) {
                    $('#json').empty();
                    gname = obj.gameName;
                    id = obj.gameId;
                    $.each(obj, function (key, value) {
                        $('#json').append(key.toUpperCase() + ":    " + value + "</br>");

                    });
                    $('<a>', {
                        text: 'Join',
                        href: 'new/join?gameName=' + gname + '&infoId=' + id,
                        class: 'btn btn-primary'
                    }).appendTo('#json');

                })
                .fail(function (jqxhr, textStatus, error) {
                    $('#json').empty();
                    var err = textStatus + ", " + error;
                    $('#json').append("Request Failed: " + err);
                });
    });


</script>

</body>
</html>
