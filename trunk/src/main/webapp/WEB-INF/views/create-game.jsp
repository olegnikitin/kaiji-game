<%--
  Created by Eduard Boiko
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<spring:message code="create-game.gameNameLabel" var="gameNameLabel"/>
<spring:message code="create-game.ownerNameLabel" var="ownerNameLabel"/>
<spring:message code="create-game.numberOfCardsLabel"
                var="numberOfCardsLabel"/>
<spring:message code="create-game.botTypeLabel" var="botTypeLabel"/>
<spring:message code="create-game.createButton" var="createButton"/>

<html>
<head>
    <title>Create Game</title>

</head>
<body>

<jsp:include page="header.jsp"/>

<br> <br> <br>

    <div class="container">

        <div class="row">

            <div class="col-md-6 col-md-offset-3">

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
                        <td/>
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
                            <div style="display:inline-block;width:150px">Check to play with bot</div>
                        </td>
                        <td><form:checkbox onclick="showBots('bots', this)" path="botGame"/></td>
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

                </table>

                <form:button class="btn btn-primary" onclick="WaitDiv();">${createButton}</form:button>

            </form:form>

                <div id="wait" style="display: none;" class="jumbotron">
                   Waiting for other player to connect...
                </div>

            </div>
        </div>
    </div>

<script type="text/javascript">

    function WaitDiv() {
        document.getElementById('wait').style.display = 'block';
         var target = document.getElementById('wait');
        var spinner = new Spinner(createOpts).spin(target);
    }

    function showBots(it, box) {
        var vis = (box.checked) ? "block" : "none";
        var elements = document.getElementsByName("bots");
        for (var i = 0; i < elements.length; i++) {
            elements[i].style.display = vis;
        //document.getElementById(it).style.display = vis;
        }
    }

</script>

</body>
</html>