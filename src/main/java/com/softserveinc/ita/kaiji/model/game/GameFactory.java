package com.softserveinc.ita.kaiji.model.game;


import com.softserveinc.ita.kaiji.model.game.creator.GameCreator;
import com.softserveinc.ita.kaiji.model.util.Factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Factory of games creates game based on <code>GameInfo</code>
 * Before creating factory will validate <code>GameInfo</code>.
 * @see com.softserveinc.ita.kaiji.model.game.creator.GameCreator
 *
 * @author Paziy Evgeniy
 * @version 4.0
 * @since 23.03.14
 */
@Component
@Scope("singleton")
public final class GameFactory {

    private final Factory<GameInfo, Game, GameCreator> gameFactory;

    @Autowired
    public GameFactory(Set<GameCreator> creators) {
        gameFactory = new Factory<>(creators);
    }

    public Game makeGame(GameInfo gameInfo) {
        return gameFactory.create(gameInfo);
    }
}