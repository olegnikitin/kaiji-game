package com.softserveinc.ita.kaiji.model;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Vladyslav Shelest
 * @since 18.03.14.
 */
@WebAppConfiguration
public class DeckImplTest {

    private Deck deck;

    @Before
    public void setUp() {
        deck = new DeckImpl(1);
    }

    @Test
    public void pickCardTest() {

        boolean result1 = deck.pickCard(Card.PAPER);
        assertEquals(true, result1);

        boolean result2 = deck.getCardsCopy().contains(Card.PAPER);
        assertNotEquals(true, result2);
    }

    @Test
    public void getCardTypeCountTest() {

        int expectedNumberOfCards = 1;

        int actualNumberOfCards = deck.getCardTypeCount(Card.PAPER);

        assertEquals(expectedNumberOfCards, actualNumberOfCards);
    }

    @Test
    public void getDeckSizeTest() {

        int expectedSize = 3;

        int actualSize = deck.getDeckSize();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    public void getNextCardTest() {

        Card card1 = deck.getNextCard();
        Card card2 = deck.getNextCard();
        Card card3 = deck.getNextCard();

        assertEquals(Card.PAPER, card1);
        assertEquals(Card.SCISSORS, card2);
        assertEquals(Card.ROCK, card3);
    }

    @Test
    public void addCardTest() {
        deck.getNextCard();
        deck.getNextCard();
        deck.getNextCard();

        assertNotEquals(true, deck.pickCard(Card.ROCK));

        deck.addCard(Card.ROCK);

        assertEquals(true, deck.pickCard(Card.ROCK));
    }

    @Test
    public void getRandomCardTest() {

        Card card = deck.getRandomCard();

        assertThat(card, is(anyOf(is(Card.PAPER),
                is(Card.ROCK),
                is(Card.SCISSORS))));
    }

}
