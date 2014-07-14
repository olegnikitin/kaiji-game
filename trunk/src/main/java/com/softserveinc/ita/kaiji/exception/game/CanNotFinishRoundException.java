package com.softserveinc.ita.kaiji.exception.game;

/**
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 23.03.14
 */
public class CanNotFinishRoundException extends RuntimeException {

    public CanNotFinishRoundException() {
        super();
    }

    public CanNotFinishRoundException(String message) {
        super(message);
    }

    public CanNotFinishRoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotFinishRoundException(Throwable cause) {
        super(cause);
    }
}
