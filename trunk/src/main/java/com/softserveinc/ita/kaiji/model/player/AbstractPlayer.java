package com.softserveinc.ita.kaiji.model.player;

import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.Deck;
import com.softserveinc.ita.kaiji.model.DeckImpl;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.util.FiniteStateMachine;
import com.softserveinc.ita.kaiji.model.util.PlayerStates;
import org.apache.log4j.Logger;

/**
 * Represents basic real player and bot for future implementations
 *
 * @author Ievgen Sukhov
 * @version 2.0
 * @since 20.03.14
 */

public abstract class AbstractPlayer implements Player {

    protected User user;

    protected Integer id;
    protected String name;
    protected Deck deck;
    protected Card chosenCard;
    protected Boolean canPlay;

    protected PlayerStatistics statistic = new PlayerStatistics();

    private static final Logger LOG = Logger.getLogger(AbstractPlayer.class);

    protected FiniteStateMachine<PlayerStatus> statusChanger =
            new FiniteStateMachine<>(PlayerStatus.PLAYER_INITIALIZATION, PlayerStates.init());

    @Override
    public abstract void makeTurn(Card card);

    @Override
    public abstract Boolean isBot();


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getPoolKey() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Deck getDeck() {
        return this.deck;
    }

    public void setDeck(Integer cardNumber) {
        this.deck = new DeckImpl(cardNumber);
    }

    @Override
    public Card getChosenCard() {
        LOG.info(this.name + " has chosen card: " + this.chosenCard);
        if (statusChanger.getState() == PlayerStatus.TURN_FINISHED) {
            return this.chosenCard;
        } else {
            throw new IllegalStateException("Can not get current card, wrong state");
        }
    }

    @Override
    public void commitTurn(Card.DuelResult result) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Commiting turn for " + this.name);
        }
        if (this.deck.getDeckSize() > 0) {
            try {
                statusChanger.switchState(PlayerStatus.TURN_READY);
            } catch (SwitchStateException sse) {
                throw new RuntimeException("Can not commit turn, wrong state!", sse);
            }
        } else {
            try {
                statusChanger.switchState(PlayerStatus.FINISHED);
                canPlay = false;
            } catch (SwitchStateException sse) {
                throw new RuntimeException("Can not commit turn, wrong state", sse);
            }
        }
        this.chosenCard = null;
        this.statistic.incrementStats(result);
    }

    @Override
    public void rollbackTurn() {
        if (statusChanger.getState() == PlayerStatus.TURN_FINISHED) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Making rollback for card: " + this.chosenCard);
            }
            deck.addCard(chosenCard);
            this.chosenCard = null;
            try {
                statusChanger.switchState(PlayerStatus.TURN_READY);
            } catch (SwitchStateException sse) {
                throw new IllegalStateException(sse);
            }
        } else {

                statusChanger.trySwitchState(PlayerStatus.PLAYER_BROKEN);
                throw new IllegalStateException("Can not rollback turn, wrong state");

        }
    }

    @Override
    public Integer getCardCount() {
        if (statusChanger.getState() != PlayerStatus.PLAYER_BROKEN) {
            return deck.getDeckSize();
        } else {
            throw new IllegalStateException("Can not get deck size, wrong state");
        }
    }

    @Override
    public Boolean canPlay() {
        if (statusChanger.getState() != PlayerStatus.PLAYER_BROKEN) {
            return canPlay;
        } else {
            return false;
        }
    }

    @Override
    public void finish() {
        try {
            statusChanger.switchState(PlayerStatus.FINISHED);
            this.canPlay = false;

        } catch (SwitchStateException sse) {
            throw new IllegalStateException("Can not finish, player broken", sse);
        }
    }

    @Override
    public PlayerStatus getState() {
        return statusChanger.getState();
    }

    @Override
    public PlayerStatistics getStatistic() {
        return statistic;
    }


    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(Player player) {
        return this.id.compareTo(player.getId());
    }

    @Override
    public boolean equals(Object object) {
        if(object == null){
            return false;
        }
        if(this == object){
            return true;
        }

        if(object instanceof AbstractPlayer){
            AbstractPlayer player = (AbstractPlayer)object;
            if (this.id.equals(player.getId())){
                return true;
            }
        }
        return false;
    }

}
