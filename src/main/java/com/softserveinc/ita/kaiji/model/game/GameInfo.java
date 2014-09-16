package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.Identifiable;
import com.softserveinc.ita.kaiji.model.util.pool.Poolable;

import java.util.Date;
import java.util.Set;

/**
 * Contains the information of a game
 *
 * @author Bohdan Shaposhnik
 * @author Paziy Evgeniy
 * @version 1.9
 * @since 17.03.2014
 */
public interface GameInfo extends Identifiable, Poolable<Integer> {

    /**
     * @return all players
     */
    Set<Player> getPlayers();


    /**
     *
     * @param nickname player's nickname
     * @return player by defined nickname
     */
    Player getPlayerByName(String nickname);

    /**
     * @return the name of the game
     */
    String getGameName();

    /**
     * @return the number of cards of each type
     */
    Integer getNumberOfCards();

    /**
     * @return the number of cards of each type
     */
    Integer getNumberOfStars();

    /**
     * @return the number of players in game
     */
    Integer getNumberOfPlayers();

    /**
     * @return set number of players in game
     */
    void setNumberOfPlayers(Integer numberOfPlayers);

    /**
     * Returns type of game.
     *
     * @return type of game.
     * @see com.softserveinc.ita.kaiji.model.game.Game.Type
     */
    Game.Type getGameType();

    /**
     * Sets game type
     *
     * @param gameType type of game
     */
    void setGameType(Game.Type gameType);

    /**
     * @return time when game was started.
     */
    Date getGameStartTime();

    /**
     * Sets time when game was started.
     *
     * @param gameStartTime time when game wes started
     */
    void setGameStartTime(Date gameStartTime);

    /**
     * @return time when game was ended.
     */
    Date getGameFinishTime();

    /**
     * Sets time when game was finished.
     *
     * @param gameFinishTime time when game was finished.
     */
    void setGameFinishTime(Date gameFinishTime);

    /**
     * Extra temp id for db only
     *
     * @return db-specific integer id
     */
    public Integer getDatabaseId();

    /**
     * Sets extra temp id for db
     *
     * @param databaseId - generated id from db save
     */
    public void setDatabaseId(Integer databaseId);

}
