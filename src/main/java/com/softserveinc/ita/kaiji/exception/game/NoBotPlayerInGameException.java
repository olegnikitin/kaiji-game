package com.softserveinc.ita.kaiji.exception.game;

/**
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 23.03.14
 */
public class NoBotPlayerInGameException extends RuntimeException {

    public NoBotPlayerInGameException() {
        super();
    }

    public NoBotPlayerInGameException(String message) {
        super(message);
    }

    public NoBotPlayerInGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoBotPlayerInGameException(Throwable cause) {
        super(cause);
    }
}
