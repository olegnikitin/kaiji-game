package com.softserveinc.ita.kaiji.model.game;

/**
 * Provide additional methods to update <code>GameHistory</code>
 * @see com.softserveinc.ita.kaiji.model.game.GameHistory
 * @see com.softserveinc.ita.kaiji.model.game.AbstractGame
 *
 * @author Paziy Evgeniy
 * @version 1.2
 * @since 29.03.14
 */
interface ModifiableGameHistory extends GameHistory {

    /**
     * Adds new <code>RoundResult</code> to game history and update
     * @param roundResult for adding to history
     */
    void addRoundResult(RoundResult roundResult);

    /**
     * Determines winners of the game
     */
    void determineWinners(WinnerStrategy winnerStrategy);
}
