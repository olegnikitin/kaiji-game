package com.softserveinc.ita.kaiji.session;

import org.apache.log4j.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.StringReader;

/**
 * @author Konstantin Shevchuk
 * @version 1.3
 * @since 01.08.14.
 */

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

    @OnError
    public void onError(Session session, Throwable t) {
        LOG.error("Session ServerEndpoint socket was broken. " + t.getMessage());
    }

    @OnMessage
    public void onMessage(Session session, String sessionMessage) {
        JsonObject obj = Json.createReader(new StringReader(sessionMessage))
                .readObject();
        SessionData sessionData = SessionUtils.getUserSession().get(obj.getString("nickname"));
        sessionData.setCurrentTime(System.currentTimeMillis());
        SessionUtils.getUserSession().put(obj.getString("nickname"), sessionData);

    }
}
