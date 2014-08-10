package com.softserveinc.ita.kaiji.chat;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;

@ClientEndpoint
public class ChatClientUpdateEndpoint {

    private static final Logger LOG = Logger.getLogger(ChatClientUpdateEndpoint.class);

    @OnOpen
    public void onOpen(Session session) {
        try {
            JSONObject obj = new JSONObject();
            JSONArray list = new JSONArray();
            for(String user : ChatUtils.getActiveUsers()) {
                list.add(user);
            }
            obj.put("users", list);
            String users = obj.toJSONString();
            session.getBasicRemote().sendText(users);
        } catch (IOException e) {
            LOG.error("Failed to open client session. " + e.getMessage());
        }
    }
}

