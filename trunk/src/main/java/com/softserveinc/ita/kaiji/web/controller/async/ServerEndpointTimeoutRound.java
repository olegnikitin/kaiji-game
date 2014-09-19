package com.softserveinc.ita.kaiji.web.controller.async;

import org.apache.log4j.Logger;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author Kyrylo Bardachov
 * @version 1.0
 * @since 02.09.14.
 */

@ServerEndpoint(value = "/timeout/{gameId}")
public class ServerEndpointTimeoutRound {

    private static final Logger LOG = Logger.getLogger(ServerEndpointTimeoutRound.class);

    @OnOpen
    public void open(Session session, @PathParam("gameId") Integer id) {
        session.getUserProperties().put("gameId", id);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        LOG.error("Server Endpoint Timeout Round socket was broken. " + t.getMessage());
    }


    @OnMessage
    public void onMessage(Session session, String message) {
        Integer id = (Integer) session.getUserProperties().get("gameId");
        try {
            for (Session s : session.getOpenSessions()) {
                if (id.equals(s.getUserProperties().get("gameId")))
                    s.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            LOG.error("onMessage failed. " + e.getMessage());
        }
    }
}
