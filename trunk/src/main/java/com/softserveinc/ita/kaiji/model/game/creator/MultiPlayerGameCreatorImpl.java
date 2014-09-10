package com.softserveinc.ita.kaiji.model.game.creator;

import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.MultiPlayerGameImpl;
import com.softserveinc.ita.kaiji.model.game.TwoPlayerGameImpl;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class MultiPlayerGameCreatorImpl implements GameCreator {

    private static final Game.Type GAME_TYPE = Game.Type.KAIJI_GAME;
    ///private static final int COUNT_OF_PLAYERS = 2;

    private static final Logger LOG = Logger.getLogger(MultiPlayerGameCreatorImpl.class);

    @Override
    public boolean isValid(GameInfo gameInfo) {
        if (!gameInfo.getGameType().equals(GAME_TYPE)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Wrong Game.Type for game : TwoPlayerGameImpl");
            }
            return false;
        }

        /*if (gameInfo.getPlayers().size() != COUNT_OF_PLAYERS) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Wrong count of players for game : BotGameImpl");
            }
            return false;
        }*/

        if (LOG.isTraceEnabled()) {
            LOG.trace("GameInfo is valid for game : BotGameImpl");
        }

        return true;
    }

    @Override
    public Game create(GameInfo gameInfo) {
        return new MultiPlayerGameImpl(gameInfo);
    }
}