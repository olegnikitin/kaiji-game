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
    </style>
    <link href="${pageContext.servletContext.contextPath}/resources/css/bootstrap-responsive.css" rel="stylesheet">

    <script>
        var wsocket;
        var serviceLocation = "ws://" + document.location.host + "/chat/";
        var $nickName;
        var $message;
        var $chatWindow;
        var group = 'DP-059';

        function onMessageReceived(evt) {
            var msg = JSON.parse(evt.data); // native API
            var $messageLine = $('<tr><td class="user label label-info">' + msg.sender
                    + '</td><td class="message badge">' + msg.message
                    + '<td class="received">' + msg.received
                    + '</td></tr>');
            $chatWindow.append($messageLine);
        }
        function sendMessage() {
            var msg = '{"message":"' + $message.val() + '", "sender":"'
                    + $nickName + '", "received":""}';
            wsocket.send(msg);
            $message.val('').focus();
        }

        function connectToChatserver() {
            wsocket = new WebSocket(serviceLocation + group);
            wsocket.onmessage = onMessageReceived;
        }

        $(document).ready(function () {
            $nickName = '${pageContext.request.userPrincipal.name}';
            $message = $('#message');
            $chatWindow = $('#response');
            connectToChatserver();
            $('.chat-wrapper h2').text('Welcome to chat : ' + $nickName);

            $('.chat-wrapper').show();
            $message.focus();

            $('#do-chat').submit(function (evt) {
                evt.preventDefault();
                sendMessage()
            });
        });
    </script>
</head>

<body>

<%--<script type="text/javascript">
    setInterval(function () {

        alert(nicknames)
       /* */
       /* alert(request.getServletContext().getAttribute("nicknames"))*/
    }, 5000);
</script>--%>

<div class="container chat-wrapper">
    <form id="do-chat">
        <h2 class="alert alert-success"></h2>
        <table id="response" class="table table-bordered"></table>
        <fieldset>
            <legend>Enter your message..</legend>

            <div class="controls">
                Nicknames <%=application.getAttribute("nicknames") %>
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