package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * @author Paziy Evgeniy
 * @version 2.1
 * @since 27.03.14
 */
public class TwoPlayerGameImpl extends AbstractGame {

    private static final WinnerStrategy WINNER_STRATEGY = TwoPlayersWinnerStrategyImpl.getInstance();

    private static final Logger LOG = Logger.getLogger(TwoPlayerGameImpl.class);

    private volatile Round currentRound = new StateRoundImpl();

    public TwoPlayerGameImpl(GameInfo gameInfo) {
        super(gameInfo, WINNER_STRATEGY);
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
    public void makeTurn(Card card, Player player) {
        makeTurn(card, player, null);
    }

    @Override
    public synchronized void makeTurn(Card card, Player player, Round round) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("<in> makeTurn()");
        }

        stateChanger.switchToGamePlaying();

        checkPlayer(player);

        currentRound.makeTurn(card, player);

        if (currentRound.getState() == Round.State.ROUND_READY_TO_FINISH) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Game is finishing round...");
            }

            finishRound(currentRound);

            if (canContinueGame()) {
                currentRound = new StateRoundImpl();
            } else {
                finishGame();
            }
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
            currentRound.interruptRound();
            gameInfo.setGameFinishTime(new Date());
        }

        return getState();
    }
}
