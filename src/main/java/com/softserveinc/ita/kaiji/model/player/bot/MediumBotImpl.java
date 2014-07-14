package com.softserveinc.ita.kaiji.model.player.bot;

import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import com.softserveinc.ita.kaiji.model.DeckImpl;
import com.softserveinc.ita.kaiji.model.Gender;
import com.softserveinc.ita.kaiji.model.User;

/**
 * Medium bot implementation
 * @author  Ievgen Sukhov
 * @since 19.03.14.
 * @version 1.0
 */
public class MediumBotImpl extends Bot{

    private MediumBotImpl() {}

    /**
     * @param cardNumber number of cards of each type in deck
     */
    protected MediumBotImpl(Integer cardNumber) {
        this.deck = new DeckImpl(cardNumber);
        this.user = new User("MEDIUM_BOT", "MEDIUM_BOT", "-");
        this.user.setId(3);
        this.user.setName("MEDIUM_BOT");
        this.user.setGender(Gender.UNKNOWN);
        try {
            statusChanger.switchState(PlayerStatus.TURN_READY);
        } catch (SwitchStateException sse) {
            throw new IllegalStateException("Problems with initialization", sse);
        }
        this.canPlay = true;
    }

    @Override
    protected void executeStrategy() {
        this.chosenCard = deck.getRandomCard();
        this.deck.pickCard(chosenCard);
    }
}
