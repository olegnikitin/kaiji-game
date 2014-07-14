package com.softserveinc.ita.kaiji.model.game;


import com.softserveinc.ita.kaiji.model.player.Player;

import java.util.Set;

/**
 * Strategy of determining game winners from statistic
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 29.03.14
 */
interface WinnerStrategy {

    /**
     * Returns set of winners or empty set if there in no winners
     * @param players of the game
     * @return set of winners or empty set if there in no winners
     */
    Set<Player> getWinners(Set<Player> players);
}
