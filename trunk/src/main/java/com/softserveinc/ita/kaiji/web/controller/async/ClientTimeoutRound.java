package com.softserveinc.ita.kaiji.web.controller.async;

import org.apache.log4j.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by kbardtc on 8/28/2014.
 */
@ClientEndpoint
public class ClientTimeoutRound {
    private static final Logger LOG = Logger.getLogger(ClientTimeoutRound.class);

    @OnOpen
    public void onOpen(Session session) throws IOException {
        session.getBasicRemote().sendText(session.getId());
    }
}
