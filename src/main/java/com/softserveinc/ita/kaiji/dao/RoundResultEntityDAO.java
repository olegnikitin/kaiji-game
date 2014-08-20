package com.softserveinc.ita.kaiji.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;

/**
 * @author Alexander Vorobyov
 * @version 1.0
 * @since 20.08.2014
 */
public interface RoundResultEntityDAO extends JpaRepository<RoundResultEntity, Integer> {
	
}