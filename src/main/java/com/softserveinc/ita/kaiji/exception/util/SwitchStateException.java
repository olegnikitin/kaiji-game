package com.softserveinc.ita.kaiji.exception.util;

/**
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 24.03.14
 */
public class SwitchStateException extends Exception {

    public SwitchStateException() {
    }

    public SwitchStateException(String message) {
        super(message);
    }

    public SwitchStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwitchStateException(Throwable cause) {
        super(cause);
    }
}
