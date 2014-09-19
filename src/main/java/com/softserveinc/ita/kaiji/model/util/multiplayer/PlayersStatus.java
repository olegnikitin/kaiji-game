package com.softserveinc.ita.kaiji.model.util.multiplayer;

import com.softserveinc.ita.kaiji.model.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 10.09.14.
 */

public class PlayersStatus {

    private static final PlayersStatus chatUtils = createPlayersStatus();
    private static Map<Integer, Set<Player>> gamePlayers;
    private static Map<Integer, Object> invitePlayers;

    private static PlayersStatus createPlayersStatus() {
        gamePlayers = new HashMap<>();
        invitePlayers = new HashMap();
        return new PlayersStatus();
    }

    public static Map<Integer, Set<Player>> getPlayersStatus() {
        return gamePlayers;
    }

    public static void setPlayersStatus(Map<Integer, Set<Player>> gamePlayers) {
        PlayersStatus.gamePlayers = gamePlayers;
    }

    public static Map<Integer, Object> getInvitePlayers() {
        return invitePlayers;
    }

    public static void removeInvitePlayers(Integer gameId) {
        invitePlayers.remove(gameId);
    }

    public static void removePlayersStatus(Integer gameId) {
        gamePlayers.remove(gameId);
    }
}
