package com.softserveinc.ita.kaiji.rest.convertors;

import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;
import com.softserveinc.ita.kaiji.model.game.Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameHistoryJson {

    private Integer id;
    private GameInfoJson gameInfoJson;
    private List<RoundResultJson> roundResults = new ArrayList<>();
    private Set<String> winners = new HashSet<>();
    private Game.State gameState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GameInfoJson getGameInfoJson() {
        return gameInfoJson;
    }

    public void setGameInfoJson(GameInfoJson gameInfoJson) {
        this.gameInfoJson = gameInfoJson;
    }

    public List<RoundResultJson> getRoundResults() {
        return roundResults;
    }

    public void setRoundResults(List<RoundResultJson> roundResults) {
        this.roundResults = roundResults;
    }

    public Set<String> getWinners() {
        return winners;
    }

    public void setWinners(Set<String> winners) {
        this.winners = winners;
    }

    public Game.State getGameState() {
        return gameState;
    }

    public void setGameState(Game.State gameState) {
        this.gameState = gameState;
    }
}
