<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="pl" uri="/WEB-INF/tld/play-game.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="с" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:message code="join-game.searchButton" var="search_button"/>
<spring:message code="join-game.connecting" var="connecting"/>
<spring:message code="join-game.playerNickname" var="playerNickname"/>
<spring:message code="join-game.inviteButton" var="inviteButton"/>


<form class="form-inline" role="form">
    <input type="text" class="form-control col-md-8" id="playerName"
           name="playerName" placeholder="Enter player name"/>
    <a href="/game/multiplayer/play/search/{playerName}" class="btn btn-default col-md-2">${search_button}</a>
</form>

<br>
<br>
<br>

<br>
<br>
<br>

<table class="table table-striped" id="players">

    <tr>
        <th>#</th>
        <th>${playerNickname}</th>
    </tr>

    <c:forEach var="player" items="${playersList}" varStatus="rowCounter">
        <tr>
            <td>${rowCounter.count}</td>
            <td>${player.user.nickname}</td>
            <td>
                <a href=""
                   onclick="socketInvitation.send('${player.user.nickname}'+'#')"
                   class="btn btn-primary btn-xs">${inviteButton}</a>
            </td>

            <td>
                <div id="wait-${rowCounter.count}" style="display: none;"
                     class="well well-sm">
                        ${connecting} <span id="spin-${rowCounter.count}"
                                            style="position: relative; display: block; top: -10px; left: 50%;"></span>
                </div>
            <td>
        </tr>
    </c:forEach>
</table>


<script type="text/javascript">

    var locationWebSocket = "ws://" + document.location.host + "/invitation/"
            +'${pageContext.request.userPrincipal.name}';
    var socketInvitation;

    function connectToInvitationServer() {
        socketInvitation = new WebSocket(locationWebSocket);
        socketInvitation.onmessage = onInvitation;
    }

    function onInvitation(evt) {
        var message = evt.data;
        switch (message) {
            case 'yes':
                window.location.href = "/";
                break;
            case 'no':
                alert("Invitation rejected :(");
                break;
            default:
            {
                if (confirm(message + " send you invitation to play. Do you want to play with " + message + "?")) {
                    socketInvitation.send(message + '#' + 'yes');
                    window.location.href = "/";
                }
                else {
                    socketInvitation.send(message + '#' + 'no');
                }
            }
        }
    }

    window.onbeforeunload = function (evt) {
        // socketRoundTimeout.close();

    }

    $(document).ready(function () {
        connectToInvitationServer();
    })

</script>
