package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Sydorenko Oleksandra
 * @version 1.0
 * @since 17.09.2014
 */
class MultiPlayerWinnerStrategyImpl implements WinnerStrategy {

    private static final MultiPlayerWinnerStrategyImpl INSTANCE = new MultiPlayerWinnerStrategyImpl();

    private MultiPlayerWinnerStrategyImpl() {
    }

    public static MultiPlayerWinnerStrategyImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Set<Player> getWinners(Set<Player> players) {
        Set<Player> winners = new HashSet<>();

        for (Player p : players) {
            if (p.getStar().getQuantity() > 0) {
                winners.add(p);
            }
        }

        return winners;
    }
}
