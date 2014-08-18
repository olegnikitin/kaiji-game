package com.softserveinc.ita.kaiji.rest.dto;

import java.util.HashSet;
import java.util.Set;

public class GameJoinRestDto {

    private Integer id;
    private String gameName;
    private Set<String> players = new HashSet<>();
    private Integer numberOfCards;

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

    public Set<String> getPlayers() {
        return players;
    }

    public void setPlayers(Set<String> players) {
        this.players = players;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }
}
