package com.softserveinc.ita.kaiji.exception.game;

/**
 * Throws when <code>RoundResult</code> got player who didn't make turn
 * @see com.softserveinc.ita.kaiji.model.game.RoundResult
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 23.03.14
 */
public class PlayerNotMadeTurnException extends RuntimeException {

    public PlayerNotMadeTurnException() {
        super();
    }

    public PlayerNotMadeTurnException(String message) {
        super(message);
    }

    public PlayerNotMadeTurnException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotMadeTurnException(Throwable cause) {
        super(cause);
    }
}
