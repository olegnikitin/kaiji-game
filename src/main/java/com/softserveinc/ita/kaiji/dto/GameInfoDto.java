package com.softserveinc.ita.kaiji.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.softserveinc.ita.kaiji.model.game.Game;
import org.hibernate.validator.constraints.Range;


import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import org.springframework.stereotype.Component;

/**
 * Represents Data Transfer Object which is used
 * to pass information about the game via a form
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 19.03.14.
 */
@Component
public class GameInfoDto implements Cloneable {

    @Size(min = 1, max = 10, message = "{Size.gameinfodtoimpl.gamename}")
    private String gameName;
    @Size(min = 1, max = 10, message = "{Size.gameinfodtoimpl.playername}")
    private String playerName;

    private boolean botGame = true;
    @NotNull(message = "{NotNull.gameinfodtoimpl.numberofcards}")
    @Range(min = 1, max = 5, message = "{Range.gameinfodtoimpl.numberofcards}")
    private Integer numberOfCards;
    @NotNull(message = "{NotNull.gameinfodtoimpl.numberofstars}")
    @Range(min = 1, max = 10, message = "{Range.gameinfodtoimpl.numberofstars}")
    private Integer numberOfStars;

    private Integer gameId;
    private Bot.Types botType;
    private Game.Type gameType;
    private Integer playerId;

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Integer getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(Integer numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public Game.Type getGameType() {
        return gameType;
    }

    public void setGameType(Game.Type gameType) {
        this.gameType = gameType;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean getBotGame() {
        return botGame;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }


    public Bot.Types getBotType() {
        return botType;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setBotGame(boolean botGame) {
        this.botGame = botGame;
    }

    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public void setBotType(Bot.Types botType) {
        this.botType = botType;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
