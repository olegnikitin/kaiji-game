var wsocketSession;
var userMessage;
var activityInterval;

function connectToSessionServer(serviceLocationSession) {
    wsocketSession = new WebSocket(serviceLocationSession);
}

function closeSessionWebSocket(){
    wsocketSession.close()
}

function startSessionActivity(message,interval){
    userMessage = message
    activityInterval = setInterval(sessionActivity, interval)
}

function resetInterval(){
    clearInterval(activityInterval)
}

function sessionActivity() {
    console.log(userMessage)
        wsocketSession.send(userMessage)
}
