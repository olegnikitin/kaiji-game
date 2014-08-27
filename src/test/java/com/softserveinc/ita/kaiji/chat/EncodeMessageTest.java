package com.softserveinc.ita.kaiji.chat;


import org.junit.BeforeClass;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import java.io.StringReader;
import java.util.Date;

import static org.junit.Assert.assertTrue;

public class EncodeMessageTest {

    private static ChatMessage chatMessage;

    private static ChatMessageEncoder messageEncoder;

    @BeforeClass
    public static void setUp() {
        chatMessage = new ChatMessage();
        messageEncoder = new ChatMessageEncoder();
    }

    @Test
    public void encodeChatMessage() throws EncodeException {

        Date currentDate = new Date();
        chatMessage.setMessage("Hello");
        chatMessage.setSender("dude");
        chatMessage.setReceived(currentDate);

        JsonObject obj = Json.createReader(new StringReader(messageEncoder.encode(chatMessage)))
                .readObject();

        assertTrue(obj.getString("message").equals("Hello")
                && obj.getString("sender").equals("dude")
                && obj.getString("received").equals(currentDate.toString()));

    }
}
