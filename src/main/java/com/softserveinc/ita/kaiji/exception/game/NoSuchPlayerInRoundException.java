package com.softserveinc.ita.kaiji.exception.game;

/**
 * Throws when trying to get statistic for not existing player in round
 * @see com.softserveinc.ita.kaiji.model.game.RoundResult
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 23.03.14
 */
public class NoSuchPlayerInRoundException extends RuntimeException {

    public NoSuchPlayerInRoundException() {
        super();
    }

    public NoSuchPlayerInRoundException(String message) {
        super(message);
    }

    public NoSuchPlayerInRoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchPlayerInRoundException(Throwable cause) {
        super(cause);
    }
}
