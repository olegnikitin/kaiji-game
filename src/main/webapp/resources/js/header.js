var wsocketHeader;
var wsocketSession;
var serviceLocationHeader = "ws://" + document.location.host + "/chat/";
var serviceLocationSession = "ws://" + document.location.host + "/session";
var groupHeader = 'DP-059';

function connectToChatServerHeader() {
    wsocketHeader = new WebSocket(serviceLocationHeader + groupHeader);
    wsocketHeader.onmessage = onMessageReceivedHeader;
    }

function connectToSessionServerHeader() {
    wsocketSession = new WebSocket(serviceLocationSession);
    }

function onMessageReceivedHeader(evt) {
    document.getElementById("notificationMessage").innerHTML = 'New messages'
    setInterval(blinker, 1000);
    }

window.onbeforeunload = function (evt) {
    wsocketHeader.close();
    wsocketSession.close();
    }

var element;
$(document).ready(function () {
    connectToSessionServerHeader()
    element = document.getElementById("notificationMessage");
    element.innerHTML = ''
    if ('${param.socketActive}' != 'false') {
    connectToChatServerHeader();
    }

    })

var shown = true;

function blinker() {
    if (shown) {
    element.style.color = 'red'
    shown = false;
    } else {
    element.style.color = 'blue'
    shown = true;
    }
    }

function sessionActivity(nickname) {
    alert(nickname)
    setInterval(function () {
        var message = {"nickname": nickname};
        wsocketSession.send(message);
    },3000)
}
