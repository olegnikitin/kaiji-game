package com.softserveinc.ita.kaiji.dao;

import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.model.User;

/**
 * Custom Spring Data repository interface that
 * adds a method to retrieve user statistics
 * @author Alexander Vorobyov
 * @version 1.0
 * @since 20.08.2014
 */
public interface UserDAOCustom {
	
    /**
     * Returns global statistics for user
     * @param user user entity
     * @return StatisticsDTO with statistics
     */
	public StatisticsDTO findStatistics(User user);
	
}
