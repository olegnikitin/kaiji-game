package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.exception.MakeTurnException;
import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import com.softserveinc.ita.kaiji.model.util.FiniteStateMachine;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Game wrapper for FiniteStateMachine
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 31.03.14
 */
class GameFiniteStateMachine extends FiniteStateMachine<Game.State> {

    private static final Logger LOG = Logger.getLogger(GameFiniteStateMachine.class);

    GameFiniteStateMachine(Game.State beginningState, Map<Game.State, Set<Game.State>> stateGraph) {
            super(beginningState, stateGraph);
    }

    public static Map<Game.State, Set<Game.State>> getDefaultStateGraph() {
        if (LOG.isTraceEnabled()) {
            LOG.trace("initialization statuses schema");
        }

        Map<Game.State, Set<Game.State>> statusMap = new HashMap<>();

        Set<Game.State> gameInitialization = new HashSet<>();
        gameInitialization.add(Game.State.GAME_STARTED);
        gameInitialization.add(Game.State.GAME_BROKEN);

        Set<Game.State> gameStarted = new HashSet<>();
        gameStarted.add(Game.State.GAME_PLAYING);
        gameStarted.add(Game.State.GAME_FINISHED);
        gameStarted.add(Game.State.GAME_INTERRUPTED);
        gameStarted.add(Game.State.GAME_BROKEN);

        Set<Game.State> gamePlaying = new HashSet<>();
        gamePlaying.add(Game.State.GAME_PLAYING);
        gamePlaying.add(Game.State.GAME_FINISHED);
        gamePlaying.add(Game.State.GAME_INTERRUPTED);
        gamePlaying.add(Game.State.GAME_BROKEN);

        Set<Game.State> gameFinished = new HashSet<>();

        Set<Game.State> gameInterrupted = new HashSet<>();

        Set<Game.State> gameBroken = new HashSet<>();

        statusMap.put(Game.State.GAME_INITIALIZATION, gameInitialization);
        statusMap.put(Game.State.GAME_STARTED, gameStarted);
        statusMap.put(Game.State.GAME_PLAYING, gamePlaying);
        statusMap.put(Game.State.GAME_FINISHED, gameFinished);
        statusMap.put(Game.State.GAME_INTERRUPTED, gameInterrupted);
        statusMap.put(Game.State.GAME_BROKEN, gameBroken);

        if (LOG.isTraceEnabled()) {
            LOG.trace("statuses schema initialized");
        }
        return statusMap;
    }

    public synchronized void switchToGamePlaying() {
        try {
            switchState(Game.State.GAME_PLAYING);
        } catch (SwitchStateException e) {
            String message = "Wrong game state can't make turn";
            MakeTurnException makeTurnException = new MakeTurnException(message, e);
            LOG.error(makeTurnException);
            throw makeTurnException;
        }
    }

}
