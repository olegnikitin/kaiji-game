package com.softserveinc.ita.kaiji.exception.game;

/**
 * <code>Round</code> throws this exception if player already made turn in this round.
 * @see com.softserveinc.ita.kaiji.model.game.Round
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 20.03.14
 */
public class PlayerAlreadyMadeTurnInThisRoundException extends RuntimeException {

    public PlayerAlreadyMadeTurnInThisRoundException() {
        super();
    }

    public PlayerAlreadyMadeTurnInThisRoundException(String message) {
        super(message);
    }

    public PlayerAlreadyMadeTurnInThisRoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerAlreadyMadeTurnInThisRoundException(Throwable cause) {
        super(cause);
    }
}
