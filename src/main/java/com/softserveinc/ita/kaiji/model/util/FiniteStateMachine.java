package com.softserveinc.ita.kaiji.model.util;

import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Universal finite-state machine.
 * <link>http://en.wikipedia.org/wiki/Finite-state_machine</link>
 * Recommended implement interface <code>Statable</code> for class with using state machine
 * @param <T> type of states
 * @see com.softserveinc.ita.kaiji.model.util.Statable
 * @see com.softserveinc.ita.kaiji.exception.util.SwitchStateException
 *
 * State machine using examples:
 * @see com.softserveinc.ita.kaiji.model.game.Game
 * @see com.softserveinc.ita.kaiji.model.game.Round
 * @see com.softserveinc.ita.kaiji.model.player.Player
 *
 * @author Paziy Evgeniy
 * @version 2.11
 * @since 19.03.14
 */
public class FiniteStateMachine<T> implements Statable<T> {
    private static final int DELTA_INDEX_FOR_PREVIOUS_STATE = 2;

    private static final Logger LOG = Logger.getLogger(FiniteStateMachine.class);

    /**
     * Key of Map is a state from which we wont to switch.
     * Value of Map is a Set of states to which we can switch.
     */
    private final Map<T, Set<T>> stateGraph;

    private volatile T currentState;

    private List<T> statusHistory = new ArrayList<>();

    public FiniteStateMachine(T startState, Map<T, Set<T>> stateGraph) {
        currentState = startState;
        this.stateGraph = Collections.unmodifiableMap(stateGraph);

        statusHistory.add(startState);
    }

    /**
     * Returns current state of machine.
     * @return current state of machine
     */
    @Override
    public T getState() {
        return currentState;
    }

    /**
     * Returns state previous to current state of machine.
     * Return null if there was no one switch of state.
     * @return previous state
     */
    public T getPreviousState() {
        if (statusHistory.size() < DELTA_INDEX_FOR_PREVIOUS_STATE) {
            return null;
        }

        return statusHistory.get(statusHistory.size() - DELTA_INDEX_FOR_PREVIOUS_STATE);
    }

    /**
     * Full status history of finite state machine.
     * @return list of states
     */
    public List<T> getStatusHistory() {
        return Collections.unmodifiableList(statusHistory);
    }

    /**
     * Returns unmodifiable Map of state schema.
     * @return unmodifiable Map of state schema
     */
    public Map<T, Set<T>> getStateGraph() {
        return stateGraph;
    }

    /**
     * Returns true if state can be switched to new state from current state.
     * @param newState state to which will machine switch
     * @return true if state can be switched
     */
    public boolean canSwitchTo(T newState) {
        boolean result;

        synchronized (this) {
            result = stateGraph.get(currentState).contains(newState);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Changing state for " + currentState + " to " + newState + " allowed: " + result);
        }

        return  result;
    }

    /**
     * Switch state of machine from current to new state.
     * @throws com.softserveinc.ita.kaiji.exception.util.SwitchStateException if machine can't be switched to new state
     * @param newState state to which will machine switch
     */
    public synchronized void switchState(T newState) throws SwitchStateException {
        if (!trySwitchState(newState)) {
            String errorMessage = "Can't switch status from: " + currentState + " to: " + newState;
            SwitchStateException ex = new SwitchStateException(errorMessage);
            LOG.warn("Switching state error", ex);
            throw ex;
        }
    }

    /**
     * Switch state of machine from current to new state.
     * Returns true if state was successfully switched or false if not.
     * @param newState state to which will machine switch
     * @return true if state was successfully switched or false if not
     */
    public synchronized boolean trySwitchState(T newState) {
        if (canSwitchTo(newState)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("State switched from: " + currentState + " to: " + newState);
            }

            currentState = newState;
            statusHistory.add(newState);

            return true;
        }

        return false;
    }
}
