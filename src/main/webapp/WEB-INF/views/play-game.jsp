<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="pl" uri="/WEB-INF/tld/play-game.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="com.softserveinc.ita.kaiji.model.Card"%>

<spring:message code="play-game.you" var="you" />
<spring:message code="play-game.enemy" var="enemy" />
<spring:message code="play-game.wins" var="wins" />
<spring:message code="play-game.ties" var="ties" />
<spring:message code="play-game.rock" var="rock" />
<spring:message code="play-game.paper" var="paper" />
<spring:message code="play-game.unknownCard" var="unknownCard" />
<spring:message code="play-game.scissors" var="scissors" />
<spring:message code="play-game.round" var="round" />
<spring:message code="play-game.winner" var="winner" />
<spring:message code="play-game.loser" var="loser" />
<spring:message code="play-game.draw" var="drawStatus" />
<spring:message code="play-game.gameOver" var="gameOver" />
<spring:message code="play-game.gameStart" var="gameStart" />
<spring:message code="play-game.newGame" var="newGame" />
<spring:message code="play-game.rules" var="rules" />

<c:set var="cardList" value="<%=Card.values()%>"/>
<c:set var="roundsCount" value="${fn:length(gameHistory.getRoundResults()) }"/>
<c:set var="isFinished" value="${gameHistory.getGameStatus() == 'GAME_FINISHED'}"/>
<c:set var="playerChosenCard" value="${gameHistory.getLastRoundResultFor(playerObject).getCard(playerObject)}"/>
<c:set var="enemyChosenCard" value="${gameHistory.getLastRoundResultFor(enemyObject).getCard(enemyObject)}"/>

<c:url var="urlMain" value="/" />
<c:url var="urlResources" value="/resources" />


<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Kaiji</title>

<style>
.card-count {
	background-color: #faebcc;
	width: 1em;
	padding: 4px;
	font-size: 1.5em;
	display: inline-table;
	-moz-border-radius: 1em;
	-webkit-border-radius: 1em;
	border-radius: 1em;
}
</style>

<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="${urlResources}/js/ie8-responsive-file-warning.js"></script><![endif]-->

<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<jsp:include page="header.jsp"/>

	<br>
	<br>
	<br>
	<br>
	<div class="container">
		<div class="row">
			<div class="col-md-4" style="height: 600px">
				<div class="row">
					<div class="col-md-12 alert alert-info"
						style="text-align: center; height: 30px; padding: 7px">
                        ${playerObject.name}
						<c:if test="${isFinished}">
							-
                            <script type="text/javascript">
                                $( document ).ready(function() {

                                $('.bs-example-modal-lg').modal('toggle');
                                });
                            </script>
							<c:choose>
                                <c:when test="${empty gameHistory.getWinners() }">${drawStatus }</c:when>
								<c:when test="${gameHistory.getWinners().contains(playerObject)}">${winner}</c:when>
								<c:otherwise>${loser }</c:otherwise>
							</c:choose>
						</c:if>
					</div>
					<div class="row">
						<c:forEach items="${cardList}" var="cardEntry">
							<c:set var="count" value="${playerObject.getDeck().getCardTypeCount(cardEntry) }"/>
							<div class="col-md-4">


								<a  id="cardBtn"
									href="${urlMain}game/${gameId }/card/${cardEntry }/"
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
				</div>
			</div>

			<div class="col-md-4">
				<div class="row">
				
				    <pl:statistic player="${playerObject }">
						<div class="col-md-4">
							<div class="alert alert-warning"
								style="text-align: center; height: 30px; padding: 7px; margin-bottom: 0">
								<c:choose>
									<c:when test="${duelResult == 'WIN' || duelResult == 'LOSE'}">
	      								${wins }
									</c:when>
	
									<c:when test="${duelResult == 'DRAW'}">
	      								${ties }
									</c:when>
								</c:choose>
							</div>
							<div
								style="text-align: center; font-size: 35px; height: 75px; background-color: #fcf8f2; padding-top: 10px">
								${count }
							</div>
						</div>
				    </pl:statistic>
				    
				</div>
				<div class="row">
					<div class="col-md-12">
						<hr style="margin-bottom: 0">
					</div>
				</div>
				<div class="row">
					<div class="col-md-12" style="text-align: center;">
						<h2>
							<c:choose>
								<c:when test="${isFinished }">
      								${gameOver }
								</c:when>

								<c:when test="${roundsCount == 0}">
      								${gameStart }
								</c:when>

								<c:otherwise>
      								${round } ${roundsCount+1}
								</c:otherwise>
							</c:choose>
						</h2>
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
							<span>${round } ${numberOfRound }</span>
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
				<div class="row">
					<div class="col-md-12 alert alert-danger"
						style="text-align: center; height: 30px; padding: 7px">
                        ${enemyObject.name}
						<c:if test="${isFinished}">
							- 
							<c:choose>
                                <c:when test="${empty gameHistory.getWinners()}">${drawStatus }</c:when>
								<c:when test="${gameHistory.getWinners().contains(enemyObject) }">${winner}</c:when>
								<c:otherwise>${loser }</c:otherwise>
							</c:choose>
						</c:if>
					</div>
					
					<div class="row">
						<c:forEach items="${cardList}" var="cardEntry">
							<c:set var="count" value="${enemyObject.getDeck().getCardTypeCount(cardEntry) }"/>
							<div class="col-md-4">
								<a
									href="${urlMain}game/${gameId }/card/${cardEntry }/"
									class="btn btn-primary disabled"
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
                    <div id="wait" class="well" style="display: none;">
                        Waiting for opponents turn...
                    </div>
                    <br>
                    <div id="hide">

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
				</div>
			</div>
            </div>
		</div>




        <div  class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
            <div id="gameResults" class="modal-dialog modal-lg">
                <div class="modal-content">
                    <table class="table">

                        <tr>
                          <td>  Winner </td>
                            <td>     ${gameHistory.winners} </td>
                        </tr>
                        <tr>
                            <td>State </td>
                            <td>${gameHistory.gameStatus}  </td>
                        </tr>
                        <tr>
                            <td>Start time </td>
                            <td>${gameHistory.gameInfo.gameStartTime} </td>
                        </tr>
                        <tr>
                            <td>Finish time </td>
                            <td>${gameHistory.gameInfo.gameFinishTime} </td>
                        </tr>
                        <tr>
                            <td>Cards played </td>
                            <td>${gameHistory.gameInfo.numberOfCards * 3} </td>
                        </tr>
                        </table>
                    </br>
                   <a href="<spring:url value="/game/finish/${gameId}"/>" class="btn btn-primary col-md-6 col-md-offset-3">Finish game</a>
                    </br>
                </div>
            </div>
        </div>

	</div>

    <script type="text/javascript">

        function WaitDiv()
        {

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
    </script>

</body>
</html>