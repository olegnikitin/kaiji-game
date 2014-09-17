package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.exception.game.NoSuchPlayerInGameException;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Abstract game with provide basic general functions.
 *
 * @author Paziy Evgeniy
 * @version 1.4
 * @see com.softserveinc.ita.kaiji.model.game.Game
 * @since 26.03.14
 */
abstract class AbstractGame implements Game {

    private static final Logger LOG = Logger.getLogger(AbstractGame.class);

    private volatile Integer id;

    protected final GameInfo gameInfo;
    protected final ModifiableGameHistory gameHistory;
    protected final GameFiniteStateMachine stateChanger;

    AbstractGame(GameInfo gameInfo, WinnerStrategy winnerStrategy) {
        this(gameInfo, winnerStrategy, State.GAME_INITIALIZATION, GameFiniteStateMachine.getDefaultStateGraph());
    }

    AbstractGame(GameInfo gameInfo,
                 WinnerStrategy winnerStrategy,
                 State beginningState,
                 Map<State, Set<State>> stateGraph) {
        stateChanger = new GameFiniteStateMachine(beginningState, stateGraph);

        if (LOG.isDebugEnabled()) {
            LOG.debug("AbstractGame initialization. State=" + getState());
        }

        this.gameInfo = gameInfo;
        this.gameInfo.setGameStartTime(new Date());
        if (LOG.isTraceEnabled()) {
            LOG.trace("Game info: " + gameInfo);
        }

        gameHistory = new ModifiableGameHistoryImpl(gameInfo, stateChanger);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Game history initialized: " + gameHistory);
        }

        stateChanger.trySwitchState(State.GAME_STARTED);

        if (LOG.isDebugEnabled()) {
            LOG.debug("AbstractGame initialized. State=" + getState());
        }
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Integer getPoolKey() {
        return this.id;
    }

    @Override
    public State getState() {
        return stateChanger.getState();
    }

    @Override
    public GameInfo getGameInfo() {
        return gameInfo;
    }

    @Override
    public GameHistory getGameHistory() {
        return gameHistory;
    }

    @Override
    public synchronized State finishGame() {
        if (canFinishGame()) {
            stateChanger.trySwitchState(State.GAME_FINISHED);

            gameInfo.setGameFinishTime(new Date());
            if (gameInfo.getGameType().equals(Type.KAIJI_GAME)) {
                gameHistory.determineWinners(MultiPlayerWinnerStrategyImpl.getInstance());
            } else {
                gameHistory.determineWinners(TwoPlayersWinnerStrategyImpl.getInstance());
            }
        }

        return stateChanger.getState();
    }

    protected void checkPlayer(Player player) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Checking player: " + player + " gameStatus=" + getState());
        }

        if (!gameInfo.getPlayers().contains(player)) {
            StringBuilder message = new StringBuilder();
            message.append("Player ");
            message.append(player);
            message.append(" not in gameInfo players: ");
            message.append(gameInfo.getPlayers());

            NoSuchPlayerInGameException ex = new NoSuchPlayerInGameException(message.toString());
            LOG.error(message.toString(), ex);
            throw ex;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Checked player: " + player + ". gameStatus=" + getState());
        }
    }

    protected void finishRound(Round round) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("<in> finishRound(); status=" + getState());
        }

        round.finishRound();
        RoundResult roundResult = round.getRoundResult();

        if (roundResult == null) {
            stateChanger.trySwitchState(State.GAME_BROKEN);
            throw new IllegalStateException("roundResult is null after finishing round");
        }

        gameHistory.addRoundResult(roundResult);

        if (LOG.isTraceEnabled()) {
            LOG.trace("roundResult = " + roundResult);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("<out> finishRound(); status=" + getState());
        }
    }
}