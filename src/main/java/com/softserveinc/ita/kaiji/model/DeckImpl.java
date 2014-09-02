package com.softserveinc.ita.kaiji.model;


import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Vladyslav Shelest
 * @version 1.7
 * @since 16.03.14
 */
public class DeckImpl implements Deck {

    private static final Logger LOG = Logger.getLogger(DeckImpl.class);

    private List<Card> cards = new LinkedList<>();

    /*
     * Receives a number of how many cards
     * should be of each kind.
     * Fills the list accordingly.
     */

    public DeckImpl(int n) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("DeckImpl Constructor");
        }

        for (int i = 0; i < n; i++) {
            cards.add(Card.PAPER);
            cards.add(Card.SCISSORS);
            cards.add(Card.ROCK);
        }
    }

    @Override
    public boolean pickCard(Card card) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("pickCard");
        }

        if (cards.contains(card)) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("removing the card");
            }

            cards.remove(card);
            return true;
        }

        return false;
    }

    @Override
    public int getDeckSize() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("getDeckSize");
        }

        return cards.size();
    }

    @Override
    public int getCardTypeCount(Card card) {
        return Collections.frequency(cards, card);
    }

    @Override
    public Card getNextCard() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("getNextCard");
        }
        Card card = cards.get(0);
        if (LOG.isDebugEnabled()) {
            LOG.debug("removing the card");
        }
        cards.remove(0);
        return card;
    }

    @Override
    public Card getRandomCard() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("getCardsCopy");
        }
        Random random = new Random();
        int rand = random.nextInt(getDeckSize());
        Collections.shuffle(cards);
        return cards.get(rand);
    }

    @Override
    public List<Card> getCardsCopy() {

        if (LOG.isTraceEnabled()) {
            LOG.trace("getCardsCopy");
        }

        return Collections.unmodifiableList(cards);
    }

    @Override
    public void addCard(Card card) {

        if (LOG.isTraceEnabled()) {
            LOG.trace("addCard");
        }

        cards.add(card);
    }
}
