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

import java.util.HashSet;
import java.util.Set;

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

            if (canFinishGame()) {
                finishGame();
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("<out> makeTurn()");
        }
    }

    @Override
    public boolean canFinishGame() {
        Set<Player> gamePlayers = gameInfo.getPlayers();
        Set<Player> playersOutOfGame = new HashSet<>();

        for (Player player : gamePlayers) {
            if (player.getCardCount() == 0 || player.getStar().getQuantity() == 0) {
                playersOutOfGame.add(player);
            }
        }

        return (playersOutOfGame.size() == gamePlayers.size() - 1);
    }

}
