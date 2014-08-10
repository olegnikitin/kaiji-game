<%@ page import="com.softserveinc.ita.kaiji.chat.ChatUtils" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>
    <meta charset="utf-8">
    <title>Websocket Chat</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>

    <script type="text/javascript">
        window.location.hash = "no-back-button";
        window.location.hash = "Again-No-back-button";//again because google chrome don't insert first hash into history
        window.onhashchange = function () {
            window.location.hash = "no-back-button";
        }
    </script>

    <!-- Styles -->
    <link href="${pageContext.servletContext.contextPath}/resources/css/bootstrap_chat.css" rel="stylesheet">

    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            background-color: #f5f5f5;
        }

        .received {
            width: 250px;
            font-size: 14px;
            font-weight: bold;
        }

        .users {
            width: 250px;
            font-weight: bold;
            font-size: 22px;
            color: blue;
        }
    </style>
    <link href="${pageContext.servletContext.contextPath}/resources/css/bootstrap-responsive.css" rel="stylesheet">

    <script>
        var wsocket;
        var updateWsocket;
        var serviceLocation = "ws://" + document.location.host + "/chat/";
        var updateServiceLocation = "ws://" + document.location.host + "/users";
        var $nickName;
        var $message;
        var $chatWindow;
        var $activeUsers;
        var group = 'DP-059';

        function onMessageReceived(evt) {
            var msg = JSON.parse(evt.data); // native API
            var $messageLine = $('<tr><td class="user label label-info">' + msg.sender
                    + '</td><td class="message badge">' + msg.message
                    + '<td class="received">' + msg.received
                    + '</td></tr>');
            $chatWindow.append($messageLine);
        }

        function convertJSONToTable(message) {
            var msg = JSON.parse(message);
            var $messageLine = $('<tr><td class="user label label-info">' + msg.sender
                    + '</td><td class="message badge">' + msg.message
                    + '<td class="received">' + msg.received
                    + '</td></tr>');
            return $messageLine;
        }

        function onMessageUpdateReceived(evt) {
            var msg = JSON.parse(evt.data);
            var users = msg.users.toString().split(',');
            var content = ' '
            users.forEach(
                    function buildString(user) {
                        content += (user + '; ')
                    }
            );
            $activeUsers.text(content);
        }

        function sendMessage() {
            var msg = '{"message":"' + $message.val() + '", "sender":"'
                    + $nickName + '", "received":""}';
            wsocket.send(msg);
            $message.val('').focus();
        }

        function connectToChatServer() {
            wsocket = new WebSocket(serviceLocation + group);
            updateWsocket = new WebSocket(updateServiceLocation);
            wsocket.onmessage = onMessageReceived;
            updateWsocket.onmessage = onMessageUpdateReceived;
        }

        window.onbeforeunload = function (evt) {
            wsocket.close();
            updateWsocket.close();
        }

        $(document).ready(function () {

            $nickName = '${pageContext.request.userPrincipal.name}';
            $message = $('#message');
            $chatWindow = $('#response');
            $activeUsers = $('#activeUsers')
            connectToChatServer();
            $('.chat-wrapper h2').text('Welcome to chat : ' + $nickName);
            $message.focus();
            var content = '';
            <c:forEach var = "item" items="<%=ChatUtils.getActiveUsers()%>">
            content += ("${item}" + "; ")
            </c:forEach>
            $activeUsers.text(content);

            <c:forEach items="${messages}" var = "message">
            $chatWindow.append(convertJSONToTable('${message}'))
            </c:forEach>

            $('#do-chat').submit(function (evt) {
                evt.preventDefault();
                sendMessage()
            });
        })
    </script>
</head>

<body>

<jsp:include page="header.jsp">
    <jsp:param name="socketActive" value="false"/>
</jsp:include>


<div class="container chat-wrapper">
    <form id="do-chat">
        <h2 class="alert alert-success"></h2>

        <h3> Online users : <label id="activeUsers" class="users"></label></h3>
        <table id="response" class="table table-bordered"></table>
        <fieldset>
            <legend>Enter your message...</legend>
            <div class="controls">
                <input type="text" class="input-block-level" placeholder="Print your message..." id="message"
                       style="height:60px"/>
                <input type="submit" class="btn btn-large btn-block btn-primary"
                       value="Send message"/>
            </div>
        </fieldset>
    </form>
</div>
</body>
</html>