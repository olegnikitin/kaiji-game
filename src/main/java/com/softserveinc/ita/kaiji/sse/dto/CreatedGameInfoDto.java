package com.softserveinc.ita.kaiji.sse.dto;

import com.softserveinc.ita.kaiji.model.game.Game;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Konstantin Shevchuk
 * @version 1.3
 * @since 26.08.14.
 */

public class CreatedGameInfoDto {

    private Integer id;
    private Integer number;
    private Integer numberOfCards;
    private String gameName;
    private Set<String> players = new HashSet<>();
    private Integer numberOfStars;
    private Integer numberOfPlayers;
    private Game.Type gameType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    public Integer getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(Integer numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Game.Type getGameType() {
        return gameType;
    }

    public void setGameType(Game.Type gameType) {
        this.gameType = gameType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatedGameInfoDto that = (CreatedGameInfoDto) o;

        if (!gameName.equals(that.gameName)) return false;
        if (!id.equals(that.id)) return false;
        if (!number.equals(that.number)) return false;
        if (!numberOfCards.equals(that.numberOfCards)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + numberOfCards.hashCode();
        result = 31 * result + gameName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CreatedGameInfoDto{" +
                "id=" + id +
                ", number=" + number +
                ", numberOfCards=" + numberOfCards +
                ", gameName='" + gameName + '\'' +
                ", players=" + players +
                '}';
    }
}

