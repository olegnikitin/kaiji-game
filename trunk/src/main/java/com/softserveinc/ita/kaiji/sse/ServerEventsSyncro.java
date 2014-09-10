package com.softserveinc.ita.kaiji.sse;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ServerEventsSyncro {

    private Object createdGames = new Object();

    public Object getCreatedGames() {
        return createdGames;
    }

}
