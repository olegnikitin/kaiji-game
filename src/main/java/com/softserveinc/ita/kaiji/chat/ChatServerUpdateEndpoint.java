package com.softserveinc.ita.kaiji.chat;

import org.apache.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 01.08.14.
 */

@ServerEndpoint(value = "/users")
public class ChatServerUpdateEndpoint {
    private static final Logger LOG = Logger.getLogger(ChatServerUpdateEndpoint.class);

    @OnOpen
    public void open(Session session) {
        LOG.trace("Session openend and bound to users ");
        session.getUserProperties().put("users", "users");
    }

    @OnClose
    public void close() {
        LOG.trace("Close ChatServerUpdateEndpoint socket");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        LOG.error("Chat ServerUpdateEndpoint socket was broken. " + t.getMessage());
    }

    @OnMessage
    public void onMessage(Session session, String chatMessage) {
        String users = (String) session.getUserProperties().get("users");
        try {
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen() && users.equals(s.getUserProperties().get("users"))) {
                    s.getBasicRemote().sendText(chatMessage);
                }
            }
        } catch (IOException e) {
            LOG.error("onMessage failed. " + e.getMessage());
        }
    }
}
