package com.softserveinc.ita.kaiji.model;

/**
 * @author Ievgen Sukhov
 * Created on March 15, 2014
 * @version 1.2
 */

public enum Card {

    ROCK(1), PAPER(2), SCISSORS(0);

    private int numberOfCardTypes = 3;
    private int condition;

    Card(int condition) {
        this.condition = condition;

    }

    public int getCondition() {
        return condition;
    }

    /**
     * Compares two cards
     * @param opponent
     * Another Card entity to compare with
     * @return  Outcome enum: if cards are equals - DRAW,
     * if first player won - WIN
     * and LOSE if opponent won
     */
    public DuelResult match(Card opponent) {
        int myCard = this.getCondition();
        int opponentsCard = opponent.getCondition();
        int result;

        if (myCard == opponentsCard){
            return DuelResult.DRAW;
        }

        result = (numberOfCardTypes + myCard - opponentsCard) % numberOfCardTypes;

        return (result == (numberOfCardTypes-1)) ? DuelResult.LOSE : DuelResult.WIN;
    }

    public  enum DuelResult {
        WIN, LOSE, DRAW
    }
}




