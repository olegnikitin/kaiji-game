package com.softserveinc.ita.kaiji.dto;

import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Component
public class MultiplayerGameInfoDto {

    @Size(min = 1, max = 10 )
    private String gameName;

    @NotNull
    @Range(min = 1, max = 10 )
    private Integer numberOfPlayers;

    @NotNull
    @Range(min = 1, max = 5 )
    private Integer numberOfCards;

    @NotNull
    @Range(min = 1, max = 10)
    private Integer numberOfStars;

    private boolean botGame = false;

    /*public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }*/

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Integer getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(Integer numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    /*public Game.Type getGameType() {
        return gameType;
    }

    public void setGameType(Game.Type gameType) {
        this.gameType = gameType;
    }*/

    /*public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }*/

    public String getGameName() {
        return gameName;
    }

    public boolean getBotGame() {
        return botGame;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setBotGame(boolean botGame) {
        this.botGame = botGame;
    }

    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    @Override
    public String toString() {
        return "MultiplayerGameInfoDto{" +
                "gameName='" + gameName + '\'' +
                ", numberOfPlayers=" + numberOfPlayers +
                ", numberOfCards=" + numberOfCards +
                ", numberOfStars=" + numberOfStars +
                '}';
    }
}
