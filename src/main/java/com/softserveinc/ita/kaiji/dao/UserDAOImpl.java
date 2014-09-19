package com.softserveinc.ita.kaiji.dao;

import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Alexander Vorobyov
 * @version 1.1
 * @since 20.08.2014
 */

@Repository
public class UserDAOImpl implements UserDAOCustom {

    @PersistenceContext(name = "myPersistenceUnit")
    EntityManager entityManager;

    @Override
    public StatisticsDTO findStatistics(User user) {

        List<Object[]> results = entityManager.createNativeQuery("SELECT count(IF(duel_result='WIN',1,null)) as win,\n" +
                " count(IF(duel_result='LOSE',1,null)) as lose,\n" +
                "  count(IF(duel_result='DRAW',1,null)) as draw,\n" +
                "  (SELECT COUNT(*) from game_winners as gw where gw.user_id=:id) as gameWins,\n" +
                " (SELECT COUNT(*) from link_game_to_user as links where links.user_id=:id) as totalGames" +
                " FROM user as u, round_detail as rd where u.id=:id and rd.user_id=:id")
                .setParameter("id", user.getId()).getResultList();

        StatisticsDTO statisticsDTO = new StatisticsDTO();

        Object[] statistic = results.get(0);
        statisticsDTO.setWin(((BigInteger) statistic[0]).longValue());
        statisticsDTO.setDraw(((BigInteger) statistic[1]).longValue());
        statisticsDTO.setLose(((BigInteger) statistic[2]).longValue());
        statisticsDTO.setGameWins(((BigInteger) statistic[3]).longValue());
        statisticsDTO.setTotalGames(((BigInteger) statistic[4]).longValue());

        return statisticsDTO;
    }
}
