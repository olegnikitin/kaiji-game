package com.softserveinc.ita.kaiji.sse;

import org.springframework.stereotype.Component;

@Component
public class ServerEventsSyncro {

    private Object createdGames = new Object();
    private Object invitePlayers = new Object();

    public Object getCreatedGames() {
        return createdGames;
    }

    public Object getInvitePlayers() {
        return invitePlayers;
    }
}
