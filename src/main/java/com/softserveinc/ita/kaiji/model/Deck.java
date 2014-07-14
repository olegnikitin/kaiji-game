package com.softserveinc.ita.kaiji.model;

import java.util.List;

/**
 * Represents deck that is used by players
 * @author Vladyslav Shelest
 * @version 1.7
 * @since 17.03.14.
 */
public interface Deck {

    /**
     * Returns amount of the specified card
     * @param card
     * @return amount of the specified card
     */

    int getCardTypeCount(Card card);

    /**
     * Returns size of the deck
     * @return size of the deck
     */
    int getDeckSize();

    /**
     * Returns true if deck contains the card
     * removes the specified card from the deck
     * @param card
     * @return true if deck contains the card
     * @return false if no such card was found
     */
    boolean pickCard(Card card);

    /**
     * Returns copy of the deck
     * @return copy of the deck
     */
    List<Card> getCardsCopy();


    /**
     * Returns next card in the list
     * deletes the returned card
     * @return next card in the list
     */
    Card getNextCard();

    /**
     * Returns random card from the list
     * deletes the returned card
     * @return random card from the list
     */
    Card getRandomCard();


    /**
     * Adds card to the list
     * @param card
     */

    void addCard(Card card);


}