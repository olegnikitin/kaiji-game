package com.softserveinc.ita.kaiji.chat;

import org.apache.log4j.Logger;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 01.08.14.
 */

public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {

    private final Logger LOG = Logger.getLogger(ChatMessageEncoder.class);

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(ChatMessage chatMessage) throws EncodeException {
        LOG.trace("Encode message " + chatMessage);
        return Json.createObjectBuilder()
                .add("message", chatMessage.getMessage())
                .add("sender", chatMessage.getSender())
                .add("received", chatMessage.getReceived().toString()).build()
                .toString();
    }
}
