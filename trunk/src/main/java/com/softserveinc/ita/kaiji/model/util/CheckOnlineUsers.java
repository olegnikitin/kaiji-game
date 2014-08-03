package com.softserveinc.ita.kaiji.model.util;

import com.softserveinc.ita.kaiji.chat.ActiveUsers;
import com.softserveinc.ita.kaiji.chat.ChatClientUpdateEndpoint;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CheckOnlineUsers implements HttpSessionListener {

    public static final String KEY = CheckOnlineUsers.class.getName();

    private Set<String> activeUsers = Collections.synchronizedSet(new HashSet<String>());


    public void sessionCreated(HttpSessionEvent se) {
        registerInServletContext(se.getSession().getServletContext());
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        Set<String> users = (Set<String>) se.getSession().getServletContext().getAttribute("nicknames");
        if (users.remove(se.getSession().getAttribute("nickname"))) {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = "ws://localhost:8080/users";
            try {
                container.connectToServer(ChatClientUpdateEndpoint.class,
                        URI.create(uri));
            } catch (IOException | javax.websocket.DeploymentException e) {
                System.out.println(e.getMessage() + " " + URI.create(uri));
            }
        }
        ActiveUsers.setUsers(users);
    }


    private void registerInServletContext(ServletContext servletContext) {

        if (servletContext.getAttribute(KEY) == null) {
            servletContext.setAttribute("nicknames", activeUsers);
            servletContext.setAttribute(KEY, this);
        }
    }

}
