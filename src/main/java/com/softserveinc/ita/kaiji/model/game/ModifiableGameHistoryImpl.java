package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.FiniteStateMachine;

import java.util.*;

/**
 * @author Paziy Evgeniy
 * @version 2.2
 * @since 29.03.14
 */
class ModifiableGameHistoryImpl implements ModifiableGameHistory {

    private volatile Integer id;
    private final GameInfo gameInfo;
    private final List<RoundResult> roundResults = new LinkedList<>();
    private Set<Player> winners = new HashSet<>();
    private final FiniteStateMachine<Game.State> stateSwitcher;

    ModifiableGameHistoryImpl(
            GameInfo gameInfo,
            FiniteStateMachine<Game.State> stateSwitcher) {
        this.gameInfo = gameInfo;
        this.stateSwitcher = stateSwitcher;
    }

    @Override
    public synchronized void addRoundResult(RoundResult roundResult) {
        roundResults.add(roundResult);
    }

    @Override
    public List<RoundResult> getRoundResults() {
        return Collections.unmodifiableList(roundResults);
    }

    @Override
    public RoundResult getLastRoundResultFor(Player player) {
        for (int i = roundResults.size() - 1; i >= 0; --i) {
            RoundResult currentRoundResult = roundResults.get(i);

            if (currentRoundResult.getPlayers().contains(player)) {
                return currentRoundResult;
            }
        }
        return null;
    }

    @Override
    public Set<Player> getWinners() {
        return winners;
    }

    @Override
    public GameInfo getGameInfo() {
        return gameInfo;
    }

    @Override
    public synchronized void determineWinners(WinnerStrategy winnerStrategy) {
        winners = winnerStrategy.getWinners(gameInfo.getPlayers());
    }

    @Override
    public Game.State getGameStatus() {
        return stateSwitcher.getState();
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
