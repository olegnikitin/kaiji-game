package com.softserveinc.ita.kaiji.dto.rest;

import com.softserveinc.ita.kaiji.model.Card;

/**
 * Data Transfer Object used for making turn in REST Training with bot.
 *
 * @author Eduard Boiko
 * @version 1.0
 * @since 19.03.14.
 */

public class RestTrainingTurnDto {

    private int gameId;
    private Card chosenCard;

    public Card getChosenCard() {
        return chosenCard;
    }

    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

}
