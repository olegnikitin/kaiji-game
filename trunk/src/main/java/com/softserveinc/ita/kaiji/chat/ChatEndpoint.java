package com.softserveinc.ita.kaiji.chat;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat/{group}", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class ChatEndpoint {
	private final Logger log = Logger.getLogger(getClass().getName());

	@OnOpen
	public void open(final Session session, @PathParam("group") final String group) {
		log.info("Session openend and bound to group: " + group);
		session.getUserProperties().put("group", group);
	}

	@OnMessage
	public void onMessage(final Session session, final ChatMessage chatMessage) {
		String group = (String) session.getUserProperties().get("group");
		log.info("Message " + chatMessage);
		try {
			for (Session s : session.getOpenSessions()) {
				if (s.isOpen()
						&& group.equals(s.getUserProperties().get("group"))) {
					s.getBasicRemote().sendObject(chatMessage);
				}
			}
		} catch (IOException | EncodeException e) {
			log.log(Level.WARNING, "onMessage failed", e);
		}
	}
}
