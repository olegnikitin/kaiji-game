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
<c:set var="ownLogin" value="${pageContext.request.userPrincipal.name}"/>


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
            <c:choose>
                <c:when test="${!player.isPlaying()}">
                    <button id="${player.user.nickname}" onclick="socketInvitation.send('${player.user.nickname}'+'/'+'${ownLogin}'+'/'+'${gameId}'+'#');disableAllButtons()"
                            class="btn btn-primary btn-xs">${inviteButton}</button>
                </c:when>
                <c:otherwise>
                    <button <%--onclick="socketInvitation.send('${player.user.nickname}'+'/'+'${ownLogin}'+'/'+'${gameId}'+'#');disableAllButtons()"--%>
                            class="btn btn-primary disabled">${inviteButton}</button>
                </c:otherwise>
            </c:choose>
                </td>

        <%--    <td>

                <button onclick="socketInvitation.send('${player.user.nickname}'+'/'+'${ownLogin}'+'/'+'${gameId}'+'#')"
                        class="btn btn-primary btn-xs">${inviteButton}</button>
            </td>--%>

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

<%--<div id="popWindow" class="invite">
    <div class="popup">
        <div style="height:25px;line-height:25px;background-color:rgb(6, 62, 137);color:white;padding-left:5px;">
            Invitation
        </div>
        <div align="center" style="padding-top:5px">
            <div id="invitation" class="inv"></div>
            <br/>
            <input type="button" style="margin:5px" onClick="acceptInvitation()" value="Ok"/>
            <input type="button" style="margin:5px" onClick="declineInvitation()" value="Cancel"/>
        </div>
    </div>
</div>
</div>--%>


<script type="text/javascript">

    var locationWebSocket = "ws://" + document.location.host + "/invitation/"
            + '${pageContext.request.userPrincipal.name}';
    var socketInvitation;
    var eventSource;
    var users = {};


    function connectToInvitationServer() {
        socketInvitation = new WebSocket(locationWebSocket);
        socketInvitation.onmessage = onInvitation;
    }

    /*function acceptInvitation() {
        $("#popWindow").hide();
        socketInvitation.send(users['${ownLogin}'] + '/' + '${ownLogin}' + '/' +
                '${gameId}' + '#' + 'yes');
        window.location.href = "/game/multiplayer/play/${gameId}?enemy=" + users['${ownLogin}'];
        delete users['${ownLogin}'];
    }
    }*/

    function disableAllButtons(){
        <c:forEach var="player" items="${playersList}" varStatus="rowCounter">
        $("#" + '${player.user.nickname}').attr('class', 'btn btn-primary disabled');
        </c:forEach>
    }

    function onInvitation(evt) {
        var message = evt.data;
        var dataArr = message.split('/');
        switch (dataArr[0]) {
            case 'yes':
                window.location.href = "/game/multiplayer/play/${gameId}?enemy=" + dataArr[1];
                break;
            case 'no':
                alert("Invitation rejected :(");
                break;
            default:
            {
                if (confirm(message + " send you invitation to play. Do you want to play with " + message + "?")) {
                    socketInvitation.send(message + '/' + '${ownLogin}' + '/' + '${gameId}' + '#' + 'yes');
                    window.location.href = "/game/multiplayer/play/${gameId}?enemy=" + message ;
                }
                else {
                    socketInvitation.send(message + '/' + '${ownLogin}' + '/' + '${gameId}' + '#' + 'no');
                }
            }
        }
    }

    function sendToServer(enemyLogin) {
        sessionStorage.setItem("enemyLogin", enemyLogin);
    }

    window.onbeforeunload = function (evt) {
        eventSource.close();
        // socketRoundTimeout.close();

    }

    $(document).ready(function () {
        $("#popWindow").hide();
        connectToInvitationServer();
        updatePlayers();
    })

    function updatePlayers() {
        eventSource = new EventSource("/multiplayer/invite/" + ${gameId});

        eventSource.onmessage = function (event) {
            $('#players').empty();
            var inviteBtn = '';
            var inviteUserHeader = $('<tr><th>' + '#'
                    + '</th><th>' + '${playerNickname}'
                    + '</th></tr>');
            $('#players').append(inviteUserHeader);
            var msg = JSON.parse(event.data);
            var inviteButtonStyle;
            msg.forEach(
                    function fillUserTable(player) {
                        inviteButtonStyle = 'btn btn-primary btn-xs';
                        if (player.isPlaying == true) {
                            inviteButtonStyle = 'btn btn-primary disabled'
                        }
                        inviteBtn = '<button class=' + "\"" + inviteButtonStyle + "\"" +
                                ' id = ' + "\"" +  player.name + "\" " +
                                ' onclick = ' + '\"socketInvitation.send(' + '\'' + player.name + '/' +
                                '${ownLogin}' + '/' + '${gameId}' + '#' + '\'' + ');disableAllButtons()\"' +
                                '>${inviteButton}</button>'
                        console.log(inviteBtn);
                        console.log(player.isPlaying)
                        $('#players').append(
                                        '<tr><td>' + player.number
                                        + '</td><td>' + player.name
                                        + '</td><td>' + inviteBtn
                                        + '</td></tr>')
                    })
        };
    }
    ;

</script>
