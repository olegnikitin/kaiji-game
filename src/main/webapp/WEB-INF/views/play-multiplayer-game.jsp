<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="pl" uri="/WEB-INF/tld/play-game.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="com.softserveinc.ita.kaiji.model.Card" %>

<spring:message code="play-game.you" var="you"/>
<spring:message code="play-game.enemy" var="enemy"/>
<spring:message code="play-game.wins" var="wins"/>
<spring:message code="play-game.ties" var="ties"/>
<spring:message code="play-game.rock" var="rock"/>
<spring:message code="play-game.paper" var="paper"/>
<spring:message code="play-game.unknownCard" var="unknownCard"/>
<spring:message code="play-game.scissors" var="scissors"/>
<spring:message code="play-game.round" var="round"/>
<spring:message code="play-game.winner" var="winner"/>
<spring:message code="play-game.loser" var="loser"/>
<spring:message code="play-game.draw" var="drawStatus"/>
<spring:message code="play-game.gameOver" var="gameOverLabel"/>
<spring:message code="play-game.gameStart" var="gameStart"/>
<spring:message code="play-game.newGame" var="newGame"/>
<spring:message code="play-game.rules" var="rules"/>
<spring:message code="play-game.stars" var="stars"/>
<spring:message code="play-game.cards" var="cards"/>

<c:set var="cardList" value="<%=Card.values()%>"/>
<c:set var="roundsCount"
       value="${fn:length(gameHistory.getRoundResults()) }"/>
<c:set var="isFinishedRound"
       value="${endRound}"/>
<c:set var="isFinishedGame"
       value="${gameHistory.getGameStatus() == 'GAME_FINISHED'}"/>
<c:set var="playerGameOver"
       value="${gameOver}"/>
<c:set var="playerChosenCard"
       value="${gameHistory.getLastRoundResultFor(playerObject).getCard(playerObject)}"/>
<c:set var="enemyChosenCard"
       value="${gameHistory.getLastRoundResultFor(enemyObject).getCard(enemyObject)}"/>
<c:set var="userName" value="${pageContext.request.userPrincipal.name}"/>

<c:url var="urlMain" value="/"/>
<c:url var="urlResources" value="/resources"/>

<script>
    window.location.hash = "no-back-button";
    window.location.hash = "Again-No-back-button";//again because google chrome don't insert first hash into history
    window.onhashchange = function () {
        window.location.hash = "no-back-button";
    }
</script>

<script type="text/javascript">

    function WaitDiv() {

        setTimeout(function () {
            connectToSessionServer()
            startSessionActivity('${userName}', 1000)
        }, 500)

        document.getElementById('wait').style.display = 'block';
        var target = document.getElementById('wait');
        var spinner = new Spinner(playOpts).spin(target);

        document.getElementById('hide').style.display = 'none';

        var inputs = document.getElementsByTagName("a")
        for (var i = 0; i < inputs.length; i++) {
            if (inputs[i].id === 'cardBtn') {
                inputs[i].setAttribute("disabled", "disabled");
            }
        }
    }

    var locationWebSocket = "ws://" + document.location.host + "/timeout/" +<%=session.getAttribute("gameId")%>;
    var socketRoundTimeout;
    function connectToRoundTimeoutServer() {
        socketRoundTimeout = new WebSocket(locationWebSocket);
        socketRoundTimeout.onmessage = onMessageTimeout;
    }

    function onMessageTimeout(evt) {
        window.location.href = '/game/join?timeout=true';
    }

    window.onbeforeunload = function (evt) {
        // socketRoundTimeout.close();

    }

    $(document).ready(function () {
        $("#left_column").attr('class', 'columns_play_game');
        $("#right_column").attr('class', 'columns_play_game');
        $("#main_div").attr('class', 'main_play_game');
        connectToRoundTimeoutServer();
    })
</script>

<div class="row">
<div class="col-md-4" style="height: 600px">
    <c:if test="${playerObject.isGameWithStars()}">
        <div class="col-md-12 alert alert-success"
             style="text-align: left; height: 40px; width: 100px;  padding: 2px">
            <c:out value="${stars}"/>
            <span class="card-count" style="color: black">${playerObject.getStar().getQuantity()}</span>
        </div>
    </c:if>
    <div class="row">
        <div class="col-md-12 alert alert-info"
             style="text-align: center; height: 30px; padding: 7px">
            ${playerObject.name}
            <c:choose>
                <c:when test="${isFinishedGame}">
                    <script type="text/javascript">
                        $(document).ready(function () {

                            $('#gameResults').modal('toggle');
                        });
                    </script>
                </c:when>
                <c:otherwise>
                    <c:if test="${isFinishedRound}">
                        <script type="text/javascript">
                            $(document).ready(function () {

                                $('#roundResults').modal('toggle');
                            });
                        </script>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="row">
            <c:forEach items="${cardList}" var="cardEntry">
                <c:set var="count" value="${playerObject.getDeck().getCardTypeCount(cardEntry) }"/>
                <div class="col-md-4">


                    <a id="cardBtn"
                       href="${urlMain}game/multiplayer/play/${gameId }/card/${cardEntry }/"
                            <c:if test="${enemyObject.isBot() ne true}">
                                onclick="WaitDiv();"
                            </c:if>

                       class="btn btn-primary

									<c:if test="${count <= 0}">
									    disabled
									</c:if>"
                       style="width: 100%">


									<span style="width: 80%; display: inline-table">
									<c:choose>
                                        <c:when test="${cardEntry eq 'ROCK'}">
                                            ${rock }
                                        </c:when>
                                        <c:when test="${cardEntry eq 'PAPER'}">
                                            ${paper }
                                        </c:when>
                                        <c:when test="${cardEntry eq 'SCISSORS'}">
                                            ${scissors }
                                        </c:when>
                                        <c:otherwise>
                                            ${unknownCard }
                                        </c:otherwise>
                                    </c:choose>
									</span>
                        <span class="card-count" style="color: black">${count}</span>
                    </a>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="row" style="text-align: center">
        <br>
        <c:if test="${isFinishedRound}">
            <c:choose>
                <c:when test="${playerChosenCard eq 'ROCK'}">
                    <img style="width: 100%"
                         src="http://i59.tinypic.com/dqj71u.jpg">
                </c:when>
                <c:when test="${playerChosenCard eq 'PAPER'}">
                    <img style="width: 100%"
                         src="http://i59.tinypic.com/zna97s.jpg">
                </c:when>
                <c:when test="${playerChosenCard eq 'SCISSORS'}">
                    <img style="width: 100%"
                         src="http://i60.tinypic.com/sdg4k4.jpg">
                </c:when>
            </c:choose>
        </c:if>
    </div>
</div>

<div class="col-md-4">
    <div class="row">
    </div>
    <div class="row">
        <div class="col-md-12">
            <hr style="margin-bottom: 0">
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <hr style="margin-top: 10px">
        </div>
    </div>

    <pl:rounds gameHistory="${gameHistory }" player="${playerObject }" enemy="${enemyObject }">
        <div class="row">
            <div class="col-md-3" style="text-align: center;">
                <img src="${playerCardIconUrl }" class="img-rounded">
                <c:if test="${playerStatus eq 'WIN'}">
                    <a class="glyphicon glyphicon-ok"></a>
                </c:if>
            </div>
            <div class="col-md-6" style="text-align: center;">
            </div>
            <div class="col-md-3" style="text-align: center;">
                <c:if test="${enemyStatus eq 'WIN'}">
                    <a class="glyphicon glyphicon-ok"></a>
                </c:if>
                <img src="${enemyCardIconUrl }" class="img-rounded">
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <hr style="margin-top: 10px">
            </div>
        </div>

    </pl:rounds>

</div>

<div class="col-md-4" style="height: 600px">
    <c:if test="${enemyObject.isGameWithStars()}">
        <div class="col-md-12 alert alert-success"
             style="text-align: left; height: 40px; width: 100px;  padding: 2px">
            <c:out value="${stars}"/>
            <span class="card-count" style="color: black">${enemyObject.getStar().getQuantity()}</span>
        </div>
    </c:if>

    <div class="col-md-12 alert alert-success"
         style="text-align: left; height: 40px; width: 100px;  padding: 2px; position: absolute; right: 20px">
        <c:out value="${cards}"/>
        <span class="card-count" style="color: black">${enemyObject.getCardCount()}</span>
    </div>

    <div class="row">
    <div class="col-md-12 alert alert-danger"
         style="text-align: center; height: 30px; padding: 7px">
        ${enemyObject.name}
    </div>
</div>

    <div class="row" style="text-align: center; margin-top: 52px">
        <div id="wait" class="well" style="display: none;">
            Waiting for opponents turn...
        </div>
        <br>

        <div id="hide">
            <c:if test="${isFinishedRound}">
                <c:choose>
                    <c:when test="${enemyChosenCard eq 'ROCK'}">
                        <img style="width: 100%"
                             src="http://i59.tinypic.com/dqj71u.jpg">
                    </c:when>
                    <c:when test="${enemyChosenCard eq 'PAPER'}">
                        <img style="width: 100%"
                             src="http://i59.tinypic.com/zna97s.jpg">
                    </c:when>
                    <c:when test="${enemyChosenCard eq 'SCISSORS'}">
                        <img style="width: 100%"
                             src="http://i60.tinypic.com/sdg4k4.jpg">
                    </c:when>
                </c:choose>
            </c:if>
        </div>
    </div>
</div>
</div>


<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel"
     aria-hidden="true" id="roundResults">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <table class="table">

                <tr>
                    <td> Result</td>
                    <td>
                        <h3> ${result} </h3>
                    </td>
                </tr>
                <c:choose>
                    <c:when test="${playerGameOver}">
                        <tr>
                            <td colspan="2" align="center"> Your game is over!:( Try again!!!</td>
                        </tr>
                    </c:when>
                </c:choose>
            </table>
            </br>

            <a href="/game/multiplayer/finishRound?gameOver=${playerGameOver}"
               class="btn btn-primary col-md-6 col-md-offset-3">Finish
            </a>
            </br>
        </div>
    </div>
</div>

<div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel"
     aria-hidden="true" id="gameResults">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <table class="table">
                <tr>
                    <td> Winner</td>
                    <td>
                        <c:choose>
                            <c:when test="${gameHistory.winners.size() eq 0 }">DRAW</c:when>
                            <c:otherwise>${gameHistory.winners}</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td>State</td>
                    <td>${gameHistory.gameStatus} </td>
                </tr>
                <tr>
                    <td>Start time</td>
                    <td>${gameHistory.gameInfo.gameStartTime} </td>
                </tr>
                <tr>
                    <td>Finish time</td>
                    <td>${gameHistory.gameInfo.gameFinishTime} </td>
                </tr>
                <tr>
                    <td>Rounds played</td>
                    <td>${gameHistory.roundResults.size()}</td>
                </tr>
                <tr>
                    <td>Players played</td>
                    <td>${gameHistory.gameInfo.players.size()}</td>
                </tr>
            </table>
            </br>
            <a href="<spring:url value="/game/multiplayer/finish/${gameId}"/>"
               class="btn btn-primary col-md-6 col-md-offset-3">Finish
                game</a>
            </br>
        </div>
    </div>
</div>