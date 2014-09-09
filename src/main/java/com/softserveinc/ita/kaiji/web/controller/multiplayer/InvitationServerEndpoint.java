package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import org.apache.log4j.Logger;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by kbardtc on 8/28/2014.
 */
@ServerEndpoint(value = "/invitation/{playerLogin}")
public class InvitationServerEndpoint {

    private static final Logger LOG = Logger.getLogger(InvitationServerEndpoint.class);

    @OnOpen
    public void open(Session session, @PathParam("playerLogin") String playerLogin) {
        session.getUserProperties().put("playerLogin", playerLogin);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        LOG.error("Invitation socket was broken! Check why! " + t.getMessage());
    }


    @OnMessage
    public void onMessage(Session session, String message) {
        // Integer playerId = (Integer) session.getUserProperties().get("playerId");
        String playerLogin = message.substring(0, message.indexOf("#"));
        String data = null;
        if (message.indexOf("#") != message.length() - 1)
            data = message.substring(message.indexOf("#") + 1, message.length());
        try {
            for (Session s : session.getOpenSessions()) {
                if (playerLogin.equals(s.getUserProperties().get("playerLogin")))
                    if (data == null) {
                        s.getBasicRemote().sendText(String.valueOf(session.getUserProperties().get("playerLogin")));
                    } else{ s.getBasicRemote().sendText(data);}
            }
        } catch (IOException e) {
            LOG.error("onMessage failed. " + e.getMessage());
        }
    }

}
