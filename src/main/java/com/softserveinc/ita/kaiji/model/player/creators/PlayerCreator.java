package com.softserveinc.ita.kaiji.model.player.creators;

import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.Creator;

/**
 * This creator creates {@link com.softserveinc.ita.kaiji.model.player.Player} entity ready for game
 * Consumes {@link User} to define what player (or bot) to create
 *@author Ievgen Sukhov
  * @version 1.0
 * @since 30.03.14
 *
 * @see com.softserveinc.ita.kaiji.model.util.Creator
 */
public interface PlayerCreator extends Creator<User, Player> {

}
