package com.softserveinc.ita.kaiji.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.model.User;

public class UserDAOImpl implements UserDAOCustom {

    @PersistenceContext
	EntityManager entityManager;

	@Override
	public StatisticsDTO findStatistics(User user) {
		return (StatisticsDTO) entityManager.createNativeQuery("SELECT count(IF(duel_result='WIN',1,null)) as win,\n" +
								" count(IF(duel_result='LOSE',1,null)) as lose,\n" +
								"  count(IF(duel_result='DRAW',1,null)) as draw,\n" +
								"  (SELECT COUNT(*) from game_winners as gw where gw.user_id=:id) as gameWins,\n" +
								" (SELECT COUNT(*) from link_game_to_user as links where links.user_id=:id) as totalGames" +
								" FROM user as u, round_detail as rd where u.id=:id and rd.user_id=:id", StatisticsDTO.class)
								.setParameter("id", user.getId());
	}
}
