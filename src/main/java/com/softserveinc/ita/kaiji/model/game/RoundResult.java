package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.exception.game.NoSuchPlayerInRoundException;
import com.softserveinc.ita.kaiji.exception.game.PlayerNotMadeTurnException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Fixes result of one round for two players.
 * Players mast have state <code>TURN_FINISHED</code>
 * Because RoundResult use <code>getChosenCard</code> method of player
 * @see com.softserveinc.ita.kaiji.model.player.Player
 * @see com.softserveinc.ita.kaiji.model.player.Player.PlayerStatus
 *
 * @author Paziy Evgeniy
 * @version 2.5
 * @since 17.03.14
 */
public class RoundResult {

    private static final Logger LOG = Logger.getLogger(RoundResult.class);

    /**
     * Contains card chosen by player and duel result for this card in current round.
     */
    private static class Entry {

        private final Card card;
        private final Card.DuelResult duelResult;

        public Entry(Card card, Card.DuelResult duelResult) {
            this.card = card;
            this.duelResult = duelResult;
        }

        public Card getCard() {
            return card;
        }

        public Card.DuelResult getDuelResult() {
            return duelResult;
        }
    }

    private final Map<Player, Entry> round = new HashMap<>(2);
    private final Date endTime = new Date();

    public RoundResult(Player one, Player two) {
        checkPlayer(one);
        checkPlayer(two);

        Card playerOneCard = one.getChosenCard();
        Card playerTwoCard = two.getChosenCard();

        Card.DuelResult duelResultForPlayerOne = playerOneCard.match(playerTwoCard);
        Card.DuelResult duelResultForPlayerTwo = playerTwoCard.match(playerOneCard);

        Entry playerOneEntry = new Entry(playerOneCard, duelResultForPlayerOne);
        Entry playerTwoEntry = new Entry(playerTwoCard, duelResultForPlayerTwo);

        round.put(one, playerOneEntry);
        round.put(two, playerTwoEntry);
    }

    private void checkPlayer(Player player) {
        if (player == null) {
            String message = "Player can't be null";
            NullPointerException ex = new NullPointerException(message);
            LOG.error(message, ex);
            throw ex;
        }

        if (player.getState() != Player.PlayerStatus.TURN_FINISHED) {
            String message = "RoundResult got player who didn't make turn";
            PlayerNotMadeTurnException ex = new PlayerNotMadeTurnException(message);
            LOG.error(message, ex);
            throw ex;
        }
    }


    private Entry getEntry(Player player) {
        Entry playerResultEntry = round.get(player);

        if (playerResultEntry == null) {
            String message = "Player was not in this round";
            NoSuchPlayerInRoundException ex = new NoSuchPlayerInRoundException(message);
            LOG.warn(message, ex);
            throw ex;
        }

        return  playerResultEntry;
    }

    /**
     * Returns set of round players
     * @return set of round players
     */
    public Set<Player> getPlayers() {
        return round.keySet();
    }

    /**
     * Returns card which was chosen by player in this round
     * @param player for whom will return card
     * @return card which was chosen by player in this round
     */
    public Card getCard(Player player) {
        return getEntry(player).getCard();
    }

    /**
     * Returns duel result which was performed for player in this round
     * @param player for whom will return duel result
     * @return duel result which was performed for player in this round
     */
    public Card.DuelResult getDuelResult(Player player) {
        return getEntry(player).getDuelResult();
    }

    /**
     * Return time when round was ended
     * @return time when round was ended
     */
    public Date getEndTime() {
        return endTime;
    }
}
