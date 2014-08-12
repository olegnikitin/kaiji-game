package com.softserveinc.ita.kaiji.model.player.bot;

import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import com.softserveinc.ita.kaiji.model.DeckImpl;
import com.softserveinc.ita.kaiji.model.User;

/**
 * Easy bot implementation
 * @author  Ievgen Sukhov
 * @since 19.03.14.
 * @version 1.1
 */
public class EasyBotImpl extends Bot{

    private EasyBotImpl() {}

    /**
     * @param cardNumber number of cards of each type in deck
     */
    protected EasyBotImpl(Integer cardNumber) {
        this.deck = new DeckImpl(cardNumber);
        this.user = new User("EASY_BOT", "EASY_BOT", "-");
        this.user.setId(-1);
        this.user.setName("EASY_BOT");
        try {
            statusChanger.switchState(PlayerStatus.TURN_READY);
        } catch (SwitchStateException sse) {
            throw new IllegalStateException("Problems with initialization", sse);
        }
            this.canPlay = true;
    }

        @Override
        protected void executeStrategy() {
            this.chosenCard = deck.getNextCard();
        }

}
