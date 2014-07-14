package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.Identifiable;

import java.util.List;
import java.util.Set;

/**
 * Immutable history of the game.
 * Includes <code>GameInfo</code>, full list of rounds, statistic of the game and winners
 * Game build his interface instances
 * @see com.softserveinc.ita.kaiji.model.game.Game
 * @author Paziy Evgeniy
 * @version 1.12
 * @since 17.03.14
 */
public interface GameHistory extends Identifiable {

    /**
     * Method returns game info which history belongs.
     * @return game info which history belongs.
     */
    GameInfo getGameInfo();

    /**
     * Returns list of results for rounds which already ended.
     * @return list of results for rounds which already ended.
     */
    List<RoundResult> getRoundResults();

    /**
     * Returns <code>RoundResult</code> for last played round in game.
     * Can return <code>null</code> if there are no finished rounds
     * @param player player for whom looking last <code>RoundResult</code>
     * @return last round result
     */
    RoundResult getLastRoundResultFor(Player player);

    /**
     * Returns player who leading in game or winner if game is finished.
     * Can return <code>null</code> if for current moment draw.
     * @return player who leading in game or winner if game is finished
     */
    Set<Player> getWinners();

    /**
     * Returns current status of the game
     * @return current status of the game
     */
    Game.State getGameStatus();

}
