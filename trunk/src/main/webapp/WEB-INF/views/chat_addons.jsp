<%@ page import="com.softserveinc.ita.kaiji.chat.ChatUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
	window.location.hash = "no-back-button";
	window.location.hash = "Again-No-back-button";//again because google chrome don't insert first hash into history
	window.onhashchange = function() {
		window.location.hash = "no-back-button";
	}
</script>

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