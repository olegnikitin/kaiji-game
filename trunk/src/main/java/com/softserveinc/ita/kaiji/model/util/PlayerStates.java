package com.softserveinc.ita.kaiji.model.util;

import com.softserveinc.ita.kaiji.model.player.Player.PlayerStatus;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ievgen Sukhov
 * @since 20.03.14.
 * @version 1.0
 */
public class PlayerStates {

    private static final Logger LOG = Logger.getLogger(PlayerStates.class);

    private PlayerStates() {}

    public static Map<PlayerStatus, Set<PlayerStatus>> init() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Initializing player statuses schema");
        }

        Map<PlayerStatus, Set<PlayerStatus>> statusMap = new HashMap<>();

        Set<PlayerStatus> playerInitialization = new HashSet<>();
        playerInitialization.add(PlayerStatus.PLAYER_BROKEN);
        playerInitialization.add(PlayerStatus.TURN_READY);

        Set<PlayerStatus> turnReady = new HashSet<>();
        turnReady.add(PlayerStatus.TURN_FINISHED);
        turnReady.add(PlayerStatus.FINISHED);
        turnReady.add(PlayerStatus.PLAYER_BROKEN);

        Set<PlayerStatus> turnFinished = new HashSet<>();
        turnFinished.add(PlayerStatus.TURN_READY);
        turnFinished.add(PlayerStatus.PLAYER_BROKEN);
        turnFinished.add(PlayerStatus.FINISHED);

        Set<PlayerStatus> gameFinished = new HashSet<>();
        gameFinished.add(PlayerStatus.PLAYER_INITIALIZATION);
        gameFinished.add(PlayerStatus.PLAYER_BROKEN);

        Set<PlayerStatus> playerBroken = new HashSet<>();

        statusMap.put(PlayerStatus.PLAYER_INITIALIZATION, playerInitialization);
        statusMap.put(PlayerStatus.TURN_READY, turnReady);
        statusMap.put(PlayerStatus.TURN_FINISHED, turnFinished);
        statusMap.put(PlayerStatus.FINISHED, gameFinished);
        statusMap.put(PlayerStatus.PLAYER_BROKEN, playerBroken);

        if (LOG.isTraceEnabled()) {
            LOG.trace("Statuses schema initialized");
        }
        return statusMap;
    }
}
