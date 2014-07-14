package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 29.03.14
 */
class TwoPlayersWinnerStrategyImpl implements WinnerStrategy{

    private static final TwoPlayersWinnerStrategyImpl INSTANCE = new TwoPlayersWinnerStrategyImpl();

    private TwoPlayersWinnerStrategyImpl() {
    }

    public static TwoPlayersWinnerStrategyImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Set<Player> getWinners(Set<Player> players) {
        Set<Player> winners = new HashSet<>();

        Map<Integer, Player> wins = new HashMap<>();

        for (Player p : players) {
            int winCount = p.getStatistic().getSpecificStat(Card.DuelResult.WIN);
            wins.put(winCount, p);
        }

        //second player not override result of firs player - not draw
        if (wins.size() != 1) {

            //find max win rate
            Integer max = -1;
            for (Integer i : wins.keySet()) {
                if (max < i) {
                    max = i;
                }
            }

            //winner with max win rate
            winners.add(wins.get(max));
        }

        return winners;
    }
}
