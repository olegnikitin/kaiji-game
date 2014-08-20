package com.softserveinc.ita.kaiji.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;

/**
 * @author Alexander Vorobyov
 * @version 1.0
 * @since 19.08.2014
 */
public interface GameHistoryEntityDAO extends JpaRepository<GameHistoryEntity, Integer> {

	@Query("from GameHistoryEntity as g where ?1 in elements(g.winners)")
	List<GameHistoryEntity> findByWinner(Integer userId);
	
}