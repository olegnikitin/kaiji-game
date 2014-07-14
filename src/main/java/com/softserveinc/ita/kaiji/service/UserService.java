package com.softserveinc.ita.kaiji.service;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;

import java.util.Set;

/**
 * Interface for interactions with User and Player entities
 * @author Ievgen Sukhov
 * @version 1.0
 * @since 12.04.14
 *
 * Will be redesigned later!
 */
public interface UserService {

    /**
     * Gets User from database by his unique ID
     * @param id - ID of specified User
     * @return User entity
     */
    User getUserById(Integer id);

    /**
     * Gets User from database by his nickname
     * @param nickname - unique nickname for User to find
     * @return User entity
     */
    User findUser(String nickname);

    /**
     * Updates current User data in database if possible
     * @param user - user to to be updated
     */
    void updateUser(User user);

    /**
     * Adds current entity of User to database
     * @param user - User which will be saved
     * @return - generated unique ID of this User in database
     */
    Integer saveUser(User user);

    /**
     * Deletes current user from database if possible
     * @param user - User entity which will be delated
     */
    void deleteUser(User user);

    /**
     * Creates new player from current user and puts it in player cache
     * @param gameInfoDto - game data needed for creation of player
     * @return Player object
     */
    public Set<Player> createPlayer(GameInfoDto gameInfoDto);

    /**
     * Gets player from cache if it is available there
     * @return Player entity
     */
    Player getPlayerById(Integer playerId);

    /**
     * Adds player to cache
     * @param nickname - name of the user to convert into player
     * @param gameInfo - info about the current game
     * @return Player entity
     */
    Player addPlayer(String nickname, GameInfo gameInfo);

    /**
     * Removes player from cache
     * @param playerId - id of the player to remove
     */
    void removePlayer(Integer playerId);

    /**
     * Gets some global statistics for selected user
     * @param nickname - unique name of user to look for
     * @return StatisticsDTO object with some info about user's progress
     */
    StatisticsDTO getStatsForUser(String nickname);


}
