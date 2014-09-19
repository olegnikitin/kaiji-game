package com.softserveinc.ita.kaiji.sse;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 09.09.14.
 */

@Component
public class ServerEventsSyncro {

    private Object createdGames = new Object();

    public Object getCreatedGames() {
        return createdGames;
    }

}
