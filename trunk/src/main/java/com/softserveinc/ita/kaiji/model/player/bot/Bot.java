package com.softserveinc.ita.kaiji.model.player.bot;

import com.softserveinc.ita.kaiji.exception.MakeTurnException;
import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.AbstractPlayer;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import org.apache.log4j.Logger;

/**
 * @author Ievgen Sukhov
 * @version 2.5
 * @since 19.03.14.
 */
public abstract class Bot extends AbstractPlayer {

    private static final Logger LOG = Logger.getLogger(Bot.class);

    protected Types type;
    protected GameHistory gameData;

    /**
     * Executes specified strategy for different types of bots
     *
     * @throws java.lang.IllegalStateException if no GameHistory is available
     */
    protected abstract void executeStrategy();

    @Override
    public Boolean isBot() {
        return true;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    /**
     * Set <code>GameHistory</code> instance from current game
     * Relevant for some strategies
     *
     * @param gameData - <code>GameHistory</code> object that stores statistics from game
     * @see GameHistory
     */
    public void setGameData(GameHistory gameData) {
        this.gameData = gameData;
    }

    public GameHistory getGameData() {
        return this.gameData;
    }

    /**
     * @param card needed only for testing purposes,
     *             <code>null</code> is desirable so bot can choose card
     */
    @Override
    public void makeTurn(Card card) {
        LOG.info("Making turn for " + type + " bot");
        if (statusChanger.getState() == PlayerStatus.TURN_READY) {

            if (card == null) {
                executeStrategy();
                try {
                    statusChanger.switchState(PlayerStatus.TURN_FINISHED);
                } catch (SwitchStateException sse) {
                    throw new IllegalStateException(sse);
                }
            } else if (this.deck.pickCard(card)) {
                this.chosenCard = card;
                try {
                    statusChanger.switchState(PlayerStatus.TURN_FINISHED);
                } catch (SwitchStateException sse) {
                    throw new IllegalStateException(sse);
                }
            } else {
                this.chosenCard = null;
                throw new MakeTurnException("No such card in the deck!");
            }

        } else {
            throw new MakeTurnException("Wrong state for making turn!");
        }
    }

    public static enum Types {
        EASY, MEDIUM, HARD;
    }
}




