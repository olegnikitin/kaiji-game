package com.softserveinc.ita.kaiji.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.softserveinc.ita.kaiji.model.game.Game;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import com.softserveinc.ita.kaiji.model.player.bot.Bot;

/**
 * Represents Data Transfer Object which is used
 * to pass information about the game via a form
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 19.03.14.
 */
@Component
public class GameInfoDto {

    @Size(min = 1, max = 10, message = "{Size.gameinfodtoimpl.gamename}")
    private String gameName;
    @Size(min = 1, max = 10, message = "{Size.gameinfodtoimpl.playername}")
    private String playerName;

    private boolean botGame = true;
    @NotNull(message = "{NotNull.gameinfodtoimpl.numberofcards}")
    @Range(min = 1, max = 5, message = "{Range.gameinfodtoimpl.numberofcards}")
    private Integer numberOfCards;
    private Bot.Types botType;

    private Game.Type gameType;
    private Integer gameId;

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
}