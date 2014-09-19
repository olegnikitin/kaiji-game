package com.softserveinc.ita.kaiji.rest.dto;

import com.softserveinc.ita.kaiji.model.game.Game;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Konstantin Shevchuk
 * @version 1.3
 * @since 15.08.14.
 */

public class GameInfoRestDto {

    private Integer id;
    private String gameName;
    private Integer numberOfCards;
    private Game.Type gameType;
    private String gameStartTime;
    private String gameFinishTime;
    private Set<String> users = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public Game.Type getGameType() {
        return gameType;
    }

    public void setGameType(Game.Type gameType) {
        this.gameType = gameType;
    }

    public String getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(String gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public String getGameFinishTime() {
        return gameFinishTime;
    }

    public void setGameFinishTime(String gameFinishTime) {
        this.gameFinishTime = gameFinishTime;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
