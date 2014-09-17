package com.softserveinc.ita.kaiji.model.player;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.Deck;
import com.softserveinc.ita.kaiji.model.Star;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.util.Identifiable;
import com.softserveinc.ita.kaiji.model.util.Statable;
import com.softserveinc.ita.kaiji.model.util.pool.Poolable;

/**
 * Interface represent some instance who can play
 *
 * @author Ievgen Sukhov
 * @version 1.7
 * @since 15.03.14
 */
public interface Player extends Identifiable, Statable<Player.PlayerStatus>, Comparable<Player>, Poolable<Integer> {

    enum PlayerStatus {
        /**
         * Entity created with no problems
         */
        PLAYER_INITIALIZATION,

        /**
         * Can make another turn
         */
        TURN_READY,

        /**
         * Successfully finished round
         */
        TURN_FINISHED,

        /**
         * Errors, can not continue with current action
         */
        PLAYER_BROKEN,

        /**
         * Player stopped playing (errors or natural causes)
         */
        FINISHED
    }

    /**
     * Name of real player or bot
     *
     * @return Player`s name
     */
    String getName();

    /**
     * Get user entity associated with current player
     *
     * @return {@link User} object from which this player was created
     */
    User getUser();

    /**
     * Check if currently selected card is available
     * in player`s deck(if it is, it will be removed)
     *
     * @throws java.lang.IllegalArgumentException if  no
     *                                            such card available
     */
    void makeTurn(Card card);

    /**
     * Gets last choosen by player card
     *
     * @return Card object that represents player`s card
     * for current round
     * @throws java.lang.IllegalStateException if current card is not set
     */
    Card getChosenCard();

    /**
     * Sets player's state to ready for next turn
     * Nullifies field for chosen card in current Player
     * Only works if player has already made last turn successfully (TURN_FINISHED state)
     *
     * @param result - accepts {@link Card.DuelResult} enum type to calculate statistics for player
     *               in current game
     */
    void commitTurn(Card.DuelResult result);

    /**
     * Return deck of player's cards
     *
     * @return Deck object from current Player
     */
    Deck getDeck();

    boolean isGameWithStars();

    void setGameWithStars(boolean gameWithStars);

    /**
     * Return player's stars
     *
     * @return Star object from current Player
     */
    Star getStar();

    /**
     * Checks if this object is not real player
     *
     * @return <code>true</code> if this instance is some bot implementation
     */
    Boolean isBot();

    /**
     * Returns latest chosen card to deck if something went wrong
     * Player must be in TURN_FINISHED state already
     * Reverts player's state to TURN_READY
     */
    void rollbackTurn();

    /**
     * Returns <code>integer</code> number of all cards left in player's deck
     *
     * @return java.lang.Integer number of available cards
     */
    Integer getCardCount();

    /**
     * Returns <code>true</code> if player can make next turn
     * <code>false</code> if player quited game or some error happened inside
     *
     * @return java.lang.Boolean player state
     */
    Boolean canPlay();

    /**
     * Sets player state for current game to FINISHED
     */
    void finish();

    /**
     * Gets {@link PlayerStatistics} object for current player with stats on previous
     * rounds results
     *
     * @return PlayerStatistic instance
     */
    PlayerStatistics getStatistic();

    void startPlaying();

    void stopPlaying();

    Boolean isPlaying();

    void forceUpdate(Boolean update);

    Boolean isUpdate();
}
