package com.softserveinc.ita.kaiji.chat;

import java.util.Date;

/**
 * Represents websocket chat logic
 *
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 01.08.14.
 */

public class ChatMessage {
    private String message;
    private String sender;
    private Date received;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }

    @Override
    public String toString() {
        return "ChatMessage [message=" + message + ", sender=" + sender
                + ", received=" + received + "]";
    }
}
