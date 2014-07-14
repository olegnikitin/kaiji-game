package com.softserveinc.ita.kaiji.model.game.creator;

import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.TwoPlayerGameImpl;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 01.04.14
 */
@Component
@Scope(value = "singleton")
public class TwoPlayerGameCreatorImpl implements GameCreator {

    private static final Game.Type GAME_TYPE = Game.Type.TWO_PLAYER_GAME;
    private static final int COUNT_OF_PLAYERS = 2;

    private static final Logger LOG = Logger.getLogger(TwoPlayerGameCreatorImpl.class);

    @Override
    public boolean isValid(GameInfo gameInfo) {
        if (!gameInfo.getGameType().equals(GAME_TYPE)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Wrong Game.Type for game : TwoPlayerGameImpl");
            }
            return false;
        }

        if (gameInfo.getPlayers().size() != COUNT_OF_PLAYERS) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Wrong count of players for game : BotGameImpl");
            }
            return false;
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("GameInfo is valid for game : BotGameImpl");
        }

        return true;
    }

    @Override
    public Game create(GameInfo gameInfo) {
        return new TwoPlayerGameImpl(gameInfo);
    }
}