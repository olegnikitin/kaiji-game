var wsocketSession
var wsocketHeader
var userMessage
var activityInterval
var closed;

var serviceLocationHeader = "ws://" + document.location.host + "/chat/";
var serviceLocationSession = "ws://" + document.location.host + "/session";
var groupHeader = 'DP-059';

function connectToSessionServer() {
    wsocketSession = new WebSocket(serviceLocationSession);
    wsocketSession.onerror = sessionError
}

function connectToChatServerHeader() {
    wsocketHeader = new WebSocket(serviceLocationHeader + groupHeader);
    wsocketHeader.onmessage = onMessageReceivedHeader;
    wsocketHeader.onerror = onMessageReceivedError;
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

function sessionError() {
    console.log('Session socket pipe broken')
}

function onMessageReceivedError() {
    console.log('Chat notification socket pipe broken')
}

function resetInterval() {
    clearInterval(activityInterval)
}

function onMessageReceivedHeader(evt) {

    document.getElementById("notificationMessage").innerHTML = 'New message'
    var shown = true;
    setInterval(function () {
        if (shown) {
            $("#notificationMessage").css("color", "red")
            shown = false;
        } else {
            $("#notificationMessage").css("color", "blue")
            shown = true;
        }
    }, 1000);
}