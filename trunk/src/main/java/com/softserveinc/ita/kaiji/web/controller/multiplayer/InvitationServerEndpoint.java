package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.multiplayer.PlayersStatus;
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
        int enemyIndexEnd = message.indexOf('/');
        int ownIndexEnd = message.substring(enemyIndexEnd + 1, message.length())
                .indexOf('/') + enemyIndexEnd + 1;
        int gameIdIndexEnd = message.substring(ownIndexEnd, message.length())
                .indexOf('/') + ownIndexEnd;
        String enemyLogin = message.substring(0, enemyIndexEnd);
        String ownLogin = message.substring(enemyIndexEnd + 1, ownIndexEnd);
        Integer gameId = Integer.parseInt(message.substring(ownIndexEnd + 1, message.indexOf('#')));

        String data = null;
        if (message.indexOf("#") != message.length() - 1)
            data = message.substring(message.indexOf("#") + 1, message.length());

        Boolean isPlaying = (data == null) || ("yes").equals(data);
        System.err.println(enemyLogin + " " + ownLogin + " " + isPlaying);
        for (Player player : PlayersStatus.getPlayersStatus().get(gameId)) {
            if (player.getName().equals(ownLogin) || player.getName().equals(enemyLogin)) {
                player.playing(isPlaying);
            }

        }
        synchronized (PlayersStatus.getInvitePlayers().get(gameId)) {
            System.err.println("Notify all");
            PlayersStatus.getInvitePlayers().get(gameId).notifyAll();
        }

        try {
            for (Session s : session.getOpenSessions()) {
                if (enemyLogin.equals(s.getUserProperties().get("playerLogin")))
                    if (data == null) {
                        s.getBasicRemote().sendText(String.valueOf(session.getUserProperties().get("playerLogin")));
                    } else {
                        s.getBasicRemote().sendText(data);
                    }
            }
        } catch (IOException e) {
            LOG.error("onMessage failed. " + e.getMessage());
        }
    }

}
