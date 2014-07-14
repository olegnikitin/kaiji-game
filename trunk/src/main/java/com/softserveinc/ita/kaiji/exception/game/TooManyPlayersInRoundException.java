package com.softserveinc.ita.kaiji.exception.game;

/**
 * <code>Round</code> throws this exception if trying to add player when there are max count of players.
 * @see com.softserveinc.ita.kaiji.model.game.Round
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 20.03.14
 */
public class TooManyPlayersInRoundException extends RuntimeException {

    public TooManyPlayersInRoundException() {
        super();
    }

    public TooManyPlayersInRoundException(String message) {
        super(message);
    }

    public TooManyPlayersInRoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyPlayersInRoundException(Throwable cause) {
        super(cause);
    }
}

