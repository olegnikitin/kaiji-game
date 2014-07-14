package com.softserveinc.ita.kaiji.model.util;

/**
 * If some class using <code>FiniteStateMachine</code>
 * it can implement this interface to get access to some usefully methods
 * @see com.softserveinc.ita.kaiji.model.util.FiniteStateMachine
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 22.03.14
 */
public interface Statable<T> {

    /**
     * Returns current state of object
     * @return state of object
     */
    T getState();

}
