package com.softserveinc.ita.kaiji.model.game.creator;

import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.util.Creator;

/**
 * Game creator creates concrete type of game if game info matches to some rules.
 * @see com.softserveinc.ita.kaiji.model.util.Creator
 * @see com.softserveinc.ita.kaiji.model.game.GameFactory
 *
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 28.03.14
 */
public interface GameCreator extends Creator<GameInfo, Game> {
}
