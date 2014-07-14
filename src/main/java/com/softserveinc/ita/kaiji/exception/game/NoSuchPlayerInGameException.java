package com.softserveinc.ita.kaiji.exception.game;

/**
 * <code>Game</code> throws this exception if player doesn't contains in game info.
 * @see com.softserveinc.ita.kaiji.model.game.Game
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 21.03.14
 */
public class NoSuchPlayerInGameException extends RuntimeException {
    public NoSuchPlayerInGameException() {
        super();
    }

    public NoSuchPlayerInGameException(String message) {
        super(message);
    }

    public NoSuchPlayerInGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchPlayerInGameException(Throwable cause) {
        super(cause);
    }
}
