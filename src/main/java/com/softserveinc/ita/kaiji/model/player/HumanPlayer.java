package com.softserveinc.ita.kaiji.model.player;

import com.softserveinc.ita.kaiji.exception.MakeTurnException;
import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.DeckImpl;
import com.softserveinc.ita.kaiji.model.Star;
import com.softserveinc.ita.kaiji.model.User;
import org.apache.log4j.Logger;

/**
 * Represents basic real player
 *
 * @author Ievgen Sukhov
 * @version 2.0
 * @since 16.03.14
 */

public class HumanPlayer extends AbstractPlayer {

    private static final Logger LOG = Logger.getLogger(HumanPlayer.class);

    private HumanPlayer() {
    }

    public HumanPlayer(User user, Integer cardNumber, Integer starNumber) {
        this.name = user.getNickname();
        this.deck = new DeckImpl(cardNumber);
        this.star = new Star(starNumber);
        this.user = user;
        try {
            statusChanger.switchState(PlayerStatus.TURN_READY);
        } catch (SwitchStateException sse) {
            throw new IllegalStateException("Problems with initialization", sse);
        }
        this.canPlay = true;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public Boolean isBot() {
        return false;
    }

    @Override
    public void makeTurn(Card card) {
        LOG.info("Making turn for player: " + this.getName());
        if (statusChanger.getState() == PlayerStatus.TURN_READY) {

            if (this.deck.pickCard(card)) {
                this.chosenCard = card;
                try {
                    statusChanger.switchState(PlayerStatus.TURN_FINISHED);
                } catch (SwitchStateException sse) {
                    throw new RuntimeException(sse);
                }
            } else {
                this.chosenCard = null;
                throw new MakeTurnException("No such card in the deck!");
            }

        } else {
            throw new IllegalStateException("Can not make turn, wrong state!");
        }

    }

}
