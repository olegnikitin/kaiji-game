package com.softserveinc.ita.kaiji.dto.rest;

import com.softserveinc.ita.kaiji.model.game.Game;


/**
 * Data Transfer Object used for REST game creation.
 *
 * @author Eduard Boiko
 * @version 1.0
 * @since 19.03.14.
 */

public class RestGameInfoDto {

    private int gameId;
    private String playerName;
    private String botName;
    private Game.State gameState;
    private int cardPaperLeft;
    private int cardScissorsLeft;
    private int cardRockLeft;
    private String botChosenCard;
    private String roundResultForPlayer;
    private String card;

    public String getRoundWinner() {
        return roundResultForPlayer;
    }

    public void setRoundWinner(String roundResultForPlayer) {
        this.roundResultForPlayer = roundResultForPlayer;
    }

    public String getBotChosenCard() {
        return botChosenCard;
    }

    public void setBotChosenCard(String botChosenCard) {
        this.botChosenCard = botChosenCard;
    }

    public int getCardPaperLeft() {
        return cardPaperLeft;
    }

    public void setCardPaperLeft(int cardPaperLeft) {
        this.cardPaperLeft = cardPaperLeft;
    }

    public int getCardScissorsLeft() {
        return cardScissorsLeft;
    }

    public void setCardScissorsLeft(int cardScissorsLeft) {
        this.cardScissorsLeft = cardScissorsLeft;
    }

    public int getCardRockLeft() {
        return cardRockLeft;
    }

    public void setCardRockLeft(int cardRockLeft) {
        this.cardRockLeft = cardRockLeft;
    }

    public Game.State getGameState() {
        return gameState;
    }

    public void setGameState(Game.State gameState) {
        this.gameState = gameState;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

}
