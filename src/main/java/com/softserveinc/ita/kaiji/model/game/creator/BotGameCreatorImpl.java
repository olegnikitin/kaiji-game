package com.softserveinc.ita.kaiji.model.game.creator;

import com.softserveinc.ita.kaiji.model.game.BotGameImpl;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Creator validate <code>GameInfo</code> and create game <code>BotGameImpl</code> if valid.
 * @see com.softserveinc.ita.kaiji.model.game.creator.GameCreator
 * @see com.softserveinc.ita.kaiji.model.game.GameInfo
 * @see com.softserveinc.ita.kaiji.model.game.Game
 * @see com.softserveinc.ita.kaiji.model.game.BotGameImpl
 *
 * @author Paziy Evgeniy
 * @version 1.3
 * @since 28.03.14
 */
@Component
@Scope(value = "singleton")
public class BotGameCreatorImpl implements GameCreator {

    private static final Game.Type GAME_TYPE = Game.Type.BOT_GAME;
    private static final int COUNT_OF_PLAYERS = 2;
    private static final int COUNT_OF_BOT_PLAYERS = 1;

    private static final Logger LOG = Logger.getLogger(BotGameCreatorImpl.class);

    @Override
    public boolean isValid(GameInfo gameInfo) {
        if (!gameInfo.getGameType().equals(GAME_TYPE)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Wrong Game.Type for game : BotGameImpl");
            }
            return false;
        }

        if (gameInfo.getPlayers().size() != COUNT_OF_PLAYERS) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Wrong count of players for game : BotGameImpl");
            }
            return false;
        }

        int botPlayers = 0;
        for(Player p : gameInfo.getPlayers()) {
            if (p.isBot()) {
                ++botPlayers;
            }
        }
        if (COUNT_OF_BOT_PLAYERS != botPlayers) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Wrong count of bot players for game : BotGameImpl");
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
        return new BotGameImpl(gameInfo);
    }
}
