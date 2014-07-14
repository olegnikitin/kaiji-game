package com.softserveinc.ita.kaiji.dao;

import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.model.User;

/**
 * DAO API for user
 * @see com.softserveinc.ita.kaiji.model.User
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 08.04.14
 */
public interface UserDAO extends GenericDAO<User> {

    /**
     * Returns user from repository by nickname
     * @param nickname nickname of user
     * @return user from repository by nickname
     */
    public User getByNickname(String nickname);

    public User getByEmail(String email);

    /**
     * Returns some global stats for current user
     * @param user - user entity
     * @return StatisticsDTO with stats
     */
    public StatisticsDTO getStatistics(User user);
}
