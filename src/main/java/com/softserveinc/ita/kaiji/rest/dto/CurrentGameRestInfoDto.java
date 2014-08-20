package com.softserveinc.ita.kaiji.rest.dto;

import com.softserveinc.ita.kaiji.model.game.Game;


/**
 * Data Transfer Object used for REST game creation.
 *
 * @author Eduard Boiko
 * @version 1.0
 * @since 19.03.14.
 */

public class CurrentGameRestInfoDto {

    private Integer gameId;
    private String playerName;
    private String gameName;
    private String enemyName;
    private Game.State gameState;
    private Integer cardPaperLeft;
    private Integer cardScissorsLeft;
    private Integer cardRockLeft;
    private Integer starsLeft;
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

    public Integer getCardPaperLeft() {
        return cardPaperLeft;
    }

    public void setCardPaperLeft(Integer cardPaperLeft) {
        this.cardPaperLeft = cardPaperLeft;
    }

    public Integer getCardScissorsLeft() {
        return cardScissorsLeft;
    }

    public void setCardScissorsLeft(Integer cardScissorsLeft) {
        this.cardScissorsLeft = cardScissorsLeft;
    }

    public Integer getCardRockLeft() {
        return cardRockLeft;
    }

    public void setCardRockLeft(Integer cardRockLeft) {
        this.cardRockLeft = cardRockLeft;
    }

    public Integer getStarsLeft() {
        return starsLeft;
    }

    public void setStarsLeft(Integer starsLeft) {
        this.starsLeft = starsLeft;
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

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
