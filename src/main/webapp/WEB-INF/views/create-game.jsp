<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:message code="create-game.gameNameLabel" var="gameNameLabel"/>
<spring:message code="create-game.ownerNameLabel" var="ownerNameLabel"/>
<spring:message code="create-game.numberOfCardsLabel"
                var="numberOfCardsLabel"/>
<spring:message code="create-game.numberOfStarsLabel"
                var="numberOfStarsLabel"/>
<spring:message code="create-game.botTypeLabel" var="botTypeLabel"/>
<spring:message code="create-game.createButton" var="createButton"/>
<spring:message code="create-game.isBotGame" var="isBotGame"/>

<c:set var="userName" value="${pageContext.request.userPrincipal.name}"/>

<div id="create-game">
    <spring:url value="/game/new" var="url"/>
    <form:form action="${url}" method="POST" modelAttribute="gameInfo" role="form">
        <table>
            <tr>
                <td>
                    <div style="display:inline-block;width:150px">${gameNameLabel}</div>
                </td>
                <td><form:input path="gameName" size="25" class="form-control"/>
                <td><form:errors path="gameName"/></td>
            </tr>


            <tr>
                <td>
                    <div style="display:inline-block;width:150px">${ownerNameLabel}</div>
                </td>
                <td><form:input path="playerName" size="25" class="form-control"/></td>
                <td>
                        <form:errors path="playerName"/>
            </tr>

            <tr>
                <td>
                    <div style="display:inline-block;width:150px">${numberOfCardsLabel}</div>
                </td>
                <td><form:input path="numberOfCards" size="25" class="form-control"/></td>
                <td><form:errors path="numberOfCards"/></td>
            </tr>

            <tr>
                <td>
                    <div name="stars" style="display:inline-block;width:150px">${numberOfStarsLabel}</div>
                </td>

                <td>
                    <div name="stars">
                        <form:input path="numberOfStars" size="25" class="form-control"/>
                    </div>
                </td>
                <td>
                    <div name="stars">
                        <form:errors path="numberOfStars"/>
                    </div>
                </td>
            </tr>

            <tr>
                <td>
                    <div name="bots" style="display:inline-block;width:150px">${botTypeLabel}</div>
                </td>
                <td>
                    <div name="bots">
                        <form:select path="botType" class="form-control">
                            <form:option value="EASY" label="Easy Bot"/>
                            <form:option value="MEDIUM" label="Medium Bot"/>
                            <form:option value="HARD" label="Hard Bot"/>
                        </form:select>
                    </div>
                <td>
            </tr>

            <tr>
                <td>
                    <div style="display:inline-block;width:150px">${isBotGame}</div>
                </td>
                <td><form:checkbox onclick="showBots('bots', this); showStars('stars',this)"
                                   path="botGame"/></td>
            </tr>
            <tr>
                <td></td>
                <td align="right">
                    <form:button class="btn btn-primary" onclick="WaitDiv();">${createButton}</form:button>
                </td>
            </tr>
        </table>


    </form:form>

    <div id="wait" style="display: none;" class="jumbotron">
        Waiting for other player to connect...
    </div>

</div>


<script type="text/javascript">

    $(document).ready(function () {
        var elements = document.getElementsByName("stars");
        for (var i = 0; i < elements.length; ++i) {
            elements[i].style.display = 'none';
        }
    })

    function WaitDiv() {
        if (${!gameNameExist==false})
            alert("Game with this name has already existed!");
        setTimeout(function () {
            connectToSessionServer()
            startSessionActivity('${userName}', 1000)
        }, 500)

        document.getElementById('wait').style.display = 'block';
        var target = document.getElementById('wait');
        var spinner = new Spinner(createOpts).spin(target);
    };

    function showStars(it, box) {
        var visibility = (box.checked) ? "none" : "block";
        var elements = document.getElementsByName("stars");

        for (var i = 0; i < elements.length; ++i) {
            elements[i].style.display = visibility;
        }
    }

    function showBots(it, box) {
        var vis = (box.checked) ? "block" : "none";
        var elements = document.getElementsByName("bots");
        for (var i = 0; i < elements.length; i++) {
            elements[i].style.display = vis;
        }
    }

</script>