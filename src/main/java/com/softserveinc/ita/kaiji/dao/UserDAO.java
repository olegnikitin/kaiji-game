package com.softserveinc.ita.kaiji.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.softserveinc.ita.kaiji.model.User;

/**
 * @author Alexander Vorobyov
 * @version 1.0
 * @since 20.08.2014
 */
public interface UserDAO extends JpaRepository<User, Integer>, UserDAOCustom {

	/**
	 * Returns user from repository by nickname
	 * @param nickname nickname of user
	 * @return user from repository by nickname
	 */
	public User findByNickname(String nickname);

	public User findByEmail(String email);

}
