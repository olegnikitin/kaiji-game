package com.softserveinc.ita.kaiji.rest.dto;

import com.softserveinc.ita.kaiji.model.game.Game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Konstantin Shevchuk
 * @version 1.3
 * @since 15.08.14.
 */

public class GameHistoryRestDto {

    private Integer id;
    private GameInfoRestDto gameInfoRest;
    private List<RoundResultRestDto> roundResults = new ArrayList<>();
    private Set<String> winners = new HashSet<>();
    private Game.State gameState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GameInfoRestDto getGameInfoRest() {
        return gameInfoRest;
    }

    public void setGameInfoRest(GameInfoRestDto gameInfoRest) {
        this.gameInfoRest = gameInfoRest;
    }

    public List<RoundResultRestDto> getRoundResults() {
        return roundResults;
    }

    public void setRoundResults(List<RoundResultRestDto> roundResults) {
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
