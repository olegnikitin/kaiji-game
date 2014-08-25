package com.softserveinc.ita.kaiji.chat;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{group}", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class ChatServerEndpoint {

    private final Logger LOG = Logger.getLogger(ChatServerEndpoint.class);

    private static final Integer CHAT_MESSAGE_NUMBER = 5;

    @OnOpen
    public void open(Session session, @PathParam("group") String group) {
        LOG.trace("Session opened and bound to group: " + group);
        session.getUserProperties().put("group", group);
    }

    @OnClose
    public void close() {
        LOG.trace("Close ChatServerEndpoint socket");
    }

    @OnMessage
    public void onMessage(Session session, ChatMessage chatMessage) {
        String group = (String) session.getUserProperties().get("group");
        LOG.trace("ChatServerEndpoint Message " + chatMessage);
        try {
            if (ChatUtils.getMessages().size() > CHAT_MESSAGE_NUMBER) {
                ChatUtils.getMessages().remove(0);
            }

            String JSONMessage = new ChatMessageEncoder().encode(chatMessage);
            JSONMessage = JSONMessage.replace("'", "\\'");
            ChatUtils.getMessages().add(JSONMessage);
            String sender = Json.createReader(new StringReader(JSONMessage))
                    .readObject().getString("sender");
            System.out.println(JSONMessage);
            for (String user : ChatUtils.getActiveUsers()) {
                if (!user.equals(sender)) {
                    ChatUtils.getUnReadMessages().put(user, true);
                }
            }

            for (Session s : session.getOpenSessions()) {
                if (s.isOpen()
                        && group.equals(s.getUserProperties().get("group"))) {
                    s.getBasicRemote().sendObject(chatMessage);

                }
            }
        } catch (IOException | EncodeException e) {
            LOG.error("onMessage failed." + e.getMessage());
        }
    }
}
