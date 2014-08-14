package com.softserveinc.ita.kaiji.dao;

import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.model.User;

import java.util.List;

/**
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 13.04.14
 */
public interface GameHistoryEntityDAO extends GenericDAO<GameHistoryEntity> {

    List<GameHistoryEntity> getGameHistoryByWinner(Integer userId);
}
