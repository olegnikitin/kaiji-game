package com.softserveinc.ita.kaiji.exception;

/**
 * Can be thrown by eny program layer when error was occurred when trying make turn
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 23.03.14
 */
public class MakeTurnException extends RuntimeException {

    public MakeTurnException() {
        super();
    }

    public MakeTurnException(String message) {
        super(message);
    }

    public MakeTurnException(String message, Throwable cause) {
        super(message, cause);
    }

    public MakeTurnException(Throwable cause) {
        super(cause);
    }
}
