package com.softserveinc.ita.kaiji.model.player.creators;

import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Provides real player creation if {@link User} is valid
 *
 * @author Ievgen Sukhov
 * @version 1.0
 * @since 30.03.14
 */
@Component
@Scope(value = "singleton")
public class RealPlayerCreator implements PlayerCreator {
    @Override
    public boolean isValid(User creatorInfo) {
        return creatorInfo != null;

    }

    @Override
    public Player create(User creatorInfo) {
        return new HumanPlayer(creatorInfo, 1, 1);
    }
}
