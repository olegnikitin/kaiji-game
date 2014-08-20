package com.softserveinc.ita.kaiji.dao;

import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Alexander Vorobyov
 * @version 1.0
 * @since 20.08.2014
 */
public interface GameInfoEntityDAO extends JpaRepository<GameInfoEntity, Integer> {

    /**
     * Returns a list of games in which user took part
     * @param user whose games are to be found
     * @return list of games in which user took part
     */
	@Query("from GameInfoEntity as g where ?1 in elements(g.users)")
    List<GameInfoEntity> findByUser(Integer userId);
    
}