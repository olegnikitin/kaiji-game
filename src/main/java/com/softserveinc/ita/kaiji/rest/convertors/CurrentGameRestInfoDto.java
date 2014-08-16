package com.softserveinc.ita.kaiji.rest.convertors;

import com.softserveinc.ita.kaiji.model.game.Game;


/**
 * Data Transfer Object used for REST game creation.
 *
 * @author Eduard Boiko
 * @version 1.0
 * @since 19.03.14.
 */

public class CurrentGameRestInfoDto {

    private int gameId;
    private String playerName;
    private String enemyName;
    private Game.State gameState;
    private int cardPaperLeft;
    private int cardScissorsLeft;
    private int cardRockLeft;
    private String enemyChosenCard;
    private String yourCard;
    private String roundResultForPlayer;
    private Integer playerWin;
    private Integer enemyWin;
    private Integer draws;

    public String getRoundWinner() {
        return roundResultForPlayer;
    }

    public void setRoundWinner(String roundResultForPlayer) {
        this.roundResultForPlayer = roundResultForPlayer;
    }

    public String getEnemyChosenCard() {
        return enemyChosenCard;
    }

    public void setEnemyChosenCard(String enemyChosenCard) {
        this.enemyChosenCard = enemyChosenCard;
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

    public String getYourCard() {
        return yourCard;
    }

    public void setYourCard(String yourCard) {
        this.yourCard = yourCard;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Integer getPlayerWin() {
        return playerWin;
    }

    public void setPlayerWin(Integer playerWin) {
        this.playerWin = playerWin;
    }

    public Integer getEnemyWin() {
        return enemyWin;
    }

    public void setEnemyWin(Integer enemyWin) {
        this.enemyWin = enemyWin;
    }

    public Integer getDraws() {
        return draws;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }
}
