package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.exception.MakeTurnException;
import com.softserveinc.ita.kaiji.exception.game.NoBotPlayerInGameException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Implementation of <code>Game</code> where can play one user and one bot.
 * Holds only one round in one period of time.
 * @see com.softserveinc.ita.kaiji.model.game.Game
 *
 * @author Paziy Evgeniy
 * @version 2.0
 * @since 26.03.14
 */
public class BotGameImpl extends AbstractGame {

    private static final WinnerStrategy WINNER_STRATEGY = TwoPlayersWinnerStrategyImpl.getInstance();

    private static final Logger LOG = Logger.getLogger(BotGameImpl.class);

    private Player bot;

    public BotGameImpl(GameInfo gameInfo) {
        super(gameInfo, WINNER_STRATEGY);
        bot = findBotPlayer();
    }

    private Player findBotPlayer() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Looking for Bot player. gameStatus=" + getState());
        }

        Player botPlayer = null;
        for (Player p : gameInfo.getPlayers()) {
            if (p.isBot()) {
                botPlayer = p;
                break;
            }
        }

        if (botPlayer == null) {
            stateChanger.trySwitchState(State.GAME_BROKEN);
            String message = "There is no bot player in game marked like game with bot";
            NoBotPlayerInGameException ex = new NoBotPlayerInGameException(message);
            LOG.error("No bot player", ex);
            throw ex;
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("Chosen bot player: " + botPlayer);
        }

        return botPlayer;
    }

    private boolean canContinueGame() {
        boolean result = true;
        for (Player p : gameInfo.getPlayers()) {
            if (!p.canPlay()) {
                result = false;
                break;
            }
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("Can continue game: " + result);
        }

        return result;
    }

    @Override
    public synchronized void makeTurn(Card card, Player player) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("<in> makeTurn()");
        }

        stateChanger.switchToGamePlaying();

        checkPlayer(player);

        Round currentRound = new StateRoundImpl();

        currentRound.makeTurn(null, bot);
        currentRound.makeTurn(card, player);

        if (currentRound.getState() != Round.State.ROUND_READY_TO_FINISH) {
            stateChanger.trySwitchState(State.GAME_BROKEN);
            String message = "Round not ready to finish after two turns";
            MakeTurnException makeTurnException = new MakeTurnException(message);
            LOG.error(makeTurnException);
            throw makeTurnException;
        }

        if (LOG.isTraceEnabled()) {
            LOG.trace("Game is finishing round...");
        }
        finishRound(currentRound);

        if (!canContinueGame()) {
            finishGame();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("<out> makeTurn()");
        }
    }

    @Override
    public boolean canFinishGame() {
        return stateChanger.canSwitchTo(Game.State.GAME_FINISHED)
                && !canContinueGame();
    }

    @Override
    public boolean canInterruptGame() {
        return stateChanger.canSwitchTo(Game.State.GAME_INTERRUPTED);
    }

    @Override
    public synchronized State interruptGame() {
        if (canFinishGame()) {
            finishGame();
        } else {
            stateChanger.trySwitchState(State.GAME_INTERRUPTED);
            gameInfo.setGameFinishTime(new Date());
        }

        return getState();
    }
}