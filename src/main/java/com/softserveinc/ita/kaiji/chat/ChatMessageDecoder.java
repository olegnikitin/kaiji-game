package com.softserveinc.ita.kaiji.chat;

import org.apache.log4j.Logger;

import java.io.StringReader;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 01.08.14.
 */

public class ChatMessageDecoder implements Decoder.Text<ChatMessage> {

    private final Logger LOG = Logger.getLogger(ChatMessageDecoder.class);

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public ChatMessage decode(final String textMessage) throws DecodeException {

        LOG.trace("Decode message " + textMessage);
        ChatMessage chatMessage = new ChatMessage();

        JsonObject obj = Json.createReader(new StringReader(textMessage))
                .readObject();
        chatMessage.setMessage(obj.getString("message"));
        chatMessage.setSender(obj.getString("sender"));
        chatMessage.setReceived(new Date());
        return chatMessage;
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }
}
