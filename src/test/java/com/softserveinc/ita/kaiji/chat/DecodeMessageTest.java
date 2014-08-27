package com.softserveinc.ita.kaiji.chat;


import org.junit.BeforeClass;
import org.junit.Test;

import javax.websocket.DecodeException;

import static org.junit.Assert.assertTrue;

public class DecodeMessageTest {

    private static ChatMessageDecoder messageDecoder;

    @BeforeClass
    public static void setUp() {
        messageDecoder = new ChatMessageDecoder();
    }

    @Test
    public void decodeChatMessage() throws DecodeException {

        String encodedString = "{\"message\":\"Hi\",\"sender\":\"dude\"}";

        ChatMessage decodedMessage = messageDecoder.decode(encodedString);

        assertTrue(decodedMessage.getMessage().equals("Hi")
                && decodedMessage.getSender().equals("dude")
                && decodedMessage.getReceived().toString()!= null);
    }
}
