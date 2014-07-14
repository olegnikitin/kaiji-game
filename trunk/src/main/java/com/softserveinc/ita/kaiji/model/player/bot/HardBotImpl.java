package com.softserveinc.ita.kaiji.model.player.bot;

import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.DeckImpl;
import com.softserveinc.ita.kaiji.model.Gender;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.game.RoundResult;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Hard bot implementation
 *
 * @author Ievgen Sukhov
 * @version 1.3
 * @since 19.03.14.
 */
public class HardBotImpl extends Bot {

    private static final Logger LOG = Logger.getLogger(HardBotImpl.class);
    //AI
    private int numberOfCardTypes;
    private Player opponent;
    private int roundNumber;
    private List<RoundResult> roundResults;
    private int[][] markovCounters;
    private Map<Card, Integer> cardDictionary = new HashMap<>();
    private Boolean inited;

    private HardBotImpl() {
    }

    /**
     * @param cardNumber number of cards of each type in deck
     */
    protected HardBotImpl(Integer cardNumber) {
        this.deck = new DeckImpl(cardNumber);
        this.user = new User("HARD_BOT", "HARD_BOT", "-");
        this.user.setId(4);
        this.user.setName("HARD_BOT");
        this.user.setGender(Gender.UNKNOWN);
        //AI pre-init
        numberOfCardTypes = Card.values().length;
        markovCounters = new int[numberOfCardTypes][numberOfCardTypes];
        inited = false;
        try {
            statusChanger.switchState(PlayerStatus.TURN_READY);
        } catch (SwitchStateException sse) {
            throw new RuntimeException("Problems with initialization", sse);
        }
        this.canPlay = true;

    }

    /**
     * Strategy is based on a simple Markov chain
     * <a href="http://en.wikipedia.org/wiki/Markov_chain">More info here</a>
     *
     * @throws java.lang.IllegalStateException if no GameHistory is available for bot
     */
    @Override
    protected void executeStrategy() {
        if (this.gameData == null) {
            try {
                statusChanger.switchState(PlayerStatus.PLAYER_BROKEN);
            } catch (SwitchStateException sse) {
                throw new IllegalStateException(
                        "No Game History is available, can not configure bot for hard difficulty", sse);
            }

        }

        if (!inited) {
            initAI();
        }

        Card oneBefore;
        Card twoBefore;

        roundNumber = roundResults.size();
        if (LOG.isTraceEnabled()) {
            LOG.trace("Round results size:" + roundNumber);
        }
        if (roundNumber < 2) {
            this.chosenCard = this.deck.getRandomCard();
            this.deck.pickCard(chosenCard);
        } else {
            oneBefore = roundResults.get(roundNumber - 1).getCard(opponent);
            twoBefore = roundResults.get(roundNumber - 2).getCard(opponent);
            if (LOG.isTraceEnabled()) {
                LOG.trace("Bot hard AI debug: onebefore-"
                        + oneBefore + "  twoBefore-" + twoBefore);
            }

            incMarkovCounter(oneBefore, twoBefore);

            // Get data array that corresponds to opponents latest move.
            int[] counts = markovCounters[cardDictionary.get(oneBefore)];
            // If next is rock -> move paper.
            if (counts[0] > counts[1] && counts[0] > counts[2]) {
                this.chosenCard = Card.PAPER;
                if (!this.deck.pickCard(chosenCard)) {
                    this.chosenCard = Card.ROCK;
                    if (!this.deck.pickCard(chosenCard)) {
                        this.chosenCard = Card.SCISSORS;
                        this.deck.pickCard(chosenCard);
                    }
                }
            } else if (counts[1] > counts[2]) {
                // paper -> scissors.
                this.chosenCard = Card.SCISSORS;
                if (!this.deck.pickCard(chosenCard)) {
                    this.chosenCard = Card.PAPER;
                    if (!this.deck.pickCard(chosenCard)) {
                        this.chosenCard = Card.ROCK;
                        this.deck.pickCard(chosenCard);
                    }
                }
            } else {
                // scissors -> rock.
                this.chosenCard = Card.ROCK;
                if (!this.deck.pickCard(chosenCard)) {
                    this.chosenCard = Card.SCISSORS;
                    if (!this.deck.pickCard(chosenCard)) {
                        this.chosenCard = Card.PAPER;
                        this.deck.pickCard(chosenCard);
                    }
                }
            }
        }

    }

    private void initAI() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Initializing AI settings for bot");
        }

        Set<Player> players = gameData.getGameInfo().getPlayers();
        for (Player p : players) {
            if (!p.isBot()) {
                this.opponent = p;
            }
        }

        roundResults = gameData.getRoundResults();
        for (int i = 0; i < numberOfCardTypes; ++i) {
            for (int j = 0; j < numberOfCardTypes; ++j) {
                markovCounters[i][j] = 0;
            }
        }

        cardDictionary.put(Card.ROCK, 0);
        cardDictionary.put(Card.PAPER, 1);
        cardDictionary.put(Card.SCISSORS, 2);
        inited = true;

    }


    private void incMarkovCounter(Card oneBefore, Card twoBefore) {
        this.markovCounters[cardDictionary.get(twoBefore)][cardDictionary.get(oneBefore)]++;
    }
}
