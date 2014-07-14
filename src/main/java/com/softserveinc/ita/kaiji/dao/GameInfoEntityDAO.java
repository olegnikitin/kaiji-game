package com.softserveinc.ita.kaiji.dao;

import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.model.User;

import java.util.List;

/**
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 09.04.14
 */
public interface GameInfoEntityDAO extends GenericDAO<GameInfoEntity> {

    /**
     * Returns list of games in which user was playing
     * @param user for whom looking games
     * @return list of games in which user was playing
     */
    List<GameInfoEntity> getGameInfoFor(User user);
}
