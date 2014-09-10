package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dao.RoundResultEntityDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ALEXANDRA on 09.09.2014.
 */
public class MultiPlayerGameImpl extends TwoPlayerGameImpl {

    private static final Logger LOG = Logger.getLogger(MultiPlayerGameImpl.class);

    private volatile Round currentRound = new StateRoundImpl();

    public MultiPlayerGameImpl(GameInfo gameInfo) {
        super(gameInfo);
    }

    @Override
    public synchronized void makeTurn(Card card, Player player) {
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
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("<out> makeTurn()");
        }
    }

    public void finishRound(Round round) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("<in> finishRound(); status=" + getState());
        }

        round.finishRound();
        RoundResult roundResult = round.getRoundResult();

        if (roundResult == null) {
            stateChanger.trySwitchState(State.GAME_BROKEN);
            throw new IllegalStateException("roundResult is null after finishing round");
        }

        gameHistory.addRoundResult(roundResult);

        if (LOG.isTraceEnabled()) {
            LOG.trace("roundResult = " + roundResult);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("<out> finishRound(); status=" + getState());
        }
    }

}
