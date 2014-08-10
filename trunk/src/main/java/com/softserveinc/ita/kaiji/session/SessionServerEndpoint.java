package com.softserveinc.ita.kaiji.session;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.StringReader;

@ServerEndpoint(value = "/session")
public class SessionServerEndpoint {
    private final Logger LOG = Logger.getLogger(SessionServerEndpoint.class);

    @OnOpen
    public void open(Session session) {
        LOG.trace("Session Server EndPoint opened ");
    }

    @OnClose
    public void close() {
        LOG.trace("Close Session Server Endpoint socket");
    }

    @OnMessage
    public void onMessage(Session session, String sessionMessage) {
        //LOG.trace("ChatServerEndpoint Message " + sessionMessage);
        JsonObject obj = Json.createReader(new StringReader(sessionMessage))
                .readObject();
        SessionData sessionData = SessionUtils.getUserSession().get(obj.getString("nickname"));
        sessionData.setCurrentTime(System.currentTimeMillis());
        SessionUtils.getUserSession().put(obj.getString("nickname"),sessionData);
    }
}
