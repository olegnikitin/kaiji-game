var wsocketSession;
var userMessage;
var activityInterval;
var closed;

var serviceLocationHeader = "ws://" + document.location.host + "/chat/";
var serviceLocationSession = "ws://" + document.location.host + "/session";
var groupHeader = 'DP-059';

function connectToSessionServer() {
    wsocketSession = new WebSocket(serviceLocationSession);
}

function connectToChatServerHeader() {
    wsocketHeader = new WebSocket(serviceLocationHeader + groupHeader);
    wsocketHeader.onmessage = onMessageReceivedHeader;
}

function closeSessionWebSocket() {
    wsocketSession.close()
}

function closeChatWebSocket() {
    wsocketHeader.close();
}

function startSessionActivity(nickname, interval) {
    userMessage = JSON.stringify({ "nickname": nickname });
    activityInterval = setInterval(function () {
        wsocketSession.send(userMessage)
    }, interval)
}

function resetInterval() {
    clearInterval(activityInterval)
}

function onMessageReceivedHeader(evt) {
    document.getElementById("notificationMessage").innerHTML = 'New messages'
    var shown = true;
    setInterval(function(){
        if (shown) {
            element.style.color = 'red'
            shown = false;
        } else {
            element.style.color = 'blue'
            shown = true;
        }
    }, 1000);
}