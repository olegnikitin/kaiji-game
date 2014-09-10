package com.softserveinc.ita.kaiji.service;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;

import java.util.Set;

/**
 * A service for a Game entity
 *
 * @author Bohdan Shaposhnik
 * @version 1.1
 * @since 17.03.2014
 */
public interface GameService {


    /**
     * Creates initial gameinfo object for game
     *
     * @param gameInfoDto - object is transfered from ui or rest request
     */
    Integer setGameInfo(GameInfoDto gameInfoDto);

    /**
     * @return human player id from game
     */
    Integer getPlayerIdFromGame(Integer gameId);

    /**
     * Another way to get player id from some game
     *
     * @param playerName name of the player to get
     * @param gameId   the game id in which to look for player
     * @return Integer player`s id
     */
    public Integer getPlayerIdFromGame(String playerName, Integer gameId);

    Integer getAbandonedGameId(String userName, Integer gameId);

    /**
     * Finishes the game
     *
     * @param gameId - the game id
     */
    void finishGame(Integer gameId);

    /**
     * Forces deletion of current gameInfo and any games and players connected to it
     * (if they are available)
     *
     * @param gameInfoId - integer id of GameInfo that must be deleted
     */
    public void clearGameInfo(Integer gameInfoId);

    /**
     * Initializes and inserts a new game into the pool
     *
     * @param gameInfo - a DTO for a game initialization
     */
    Integer createGame(GameInfo gameInfo);

    /**
     * Gets id of specified game
     *
     * @param gameName name of any of current games to look for
     * @return if game exists - will return it`s id
     */
    Integer getGameId(String gameName);

    /**
     * @param gameId - th game id
     * @return GameInfo -the information of a game with specified id
     */
    GameInfo getGameInfo(Integer gameId);

    /**
     * Returns all available not already started games
     *
     * @return Set of GameInfo objects
     */
    public Set<GameInfo> getAllGameInfos();

    /**
     * Executes a turn
     *
     * @param gameId
     * @param playerId
     * @param card
     */
    void makeTurn(Integer gameId, Integer playerId, Card card);

    /**
     * @param gameId - the id of the current game
     * @return true if the game is complete
     */
    Game.State getGameStatus(Integer gameId);

    /**
     * @param gameId - the id of the current game
     * @return current history of turns
     */
    GameHistory getGameHistory(Integer gameId);

    /**
     * Adds another player to current game
     *
     * @param nickname - name of player to add
     * @param gameName - game in which it will be added
     * @return generated id of added player
     */
    Integer addPlayer(String nickname, String gameName);

    /**
     * Return game entity by given id
     *
     * @param gameId game id
     * @return  game by specified id
     */
    Game getGameById(Integer gameId);

    /**
     *
     * @param gameId game id
     * @return  game by specified id
     */
    void releaseGameById(Integer gameId);


    /**
     *
     * @return set of real player games
     */
    Set<GameInfo> getRealPlayerInGame();

    /**
     *
     * @return set of real player games
     */
    Set<Player> getAllOtherPlayers(Integer gameId, String userName);

}
