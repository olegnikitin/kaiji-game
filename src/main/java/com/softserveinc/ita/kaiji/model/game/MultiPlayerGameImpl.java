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
 * @author Sydorenko Oleksandra
 * @version 1.0
 * @since 09.09.14
 */
public class MultiPlayerGameImpl extends TwoPlayerGameImpl {

    private static final Logger LOG = Logger.getLogger(MultiPlayerGameImpl.class);

    public MultiPlayerGameImpl(GameInfo gameInfo) {
        super(gameInfo);
    }

    @Override
    public synchronized void makeTurn(Card card, Player player, Round round) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("<in> makeTurn()");
        }

        stateChanger.switchToGamePlaying();

        checkPlayer(player);

        round.makeTurn(card, player);

        if (round.getState() == Round.State.ROUND_READY_TO_FINISH) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Game is finishing round...");
            }

            finishRound(round);
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
