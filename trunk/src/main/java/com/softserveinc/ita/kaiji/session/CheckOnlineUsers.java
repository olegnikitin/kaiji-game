package com.softserveinc.ita.kaiji.session;

import com.softserveinc.ita.kaiji.chat.ChatClientUpdateEndpoint;
import com.softserveinc.ita.kaiji.chat.ChatUtils;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

/**
 * @author Konstantin Shevchuk
 * @version 1.3
 * @since 01.08.14.
 */

public class CheckOnlineUsers implements HttpSessionListener {

    private static final Logger LOG =  Logger.getLogger(CheckOnlineUsers.class);
    private static final String KEY = CheckOnlineUsers.class.getName();

    public void sessionCreated(HttpSessionEvent se) {
        registerInServletContext(se.getSession().getServletContext());
    }

    public void sessionDestroyed(HttpSessionEvent se) {

        String nickname = (String)se.getSession().getAttribute("nickname");
        if (ChatUtils.getActiveUsers().remove(nickname)) {
            LOG.trace("Delete Session");
            SessionUtils.getUserSession().get(nickname).getTimer().cancel();
            SessionUtils.getUserSession().remove(nickname);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            try {
                String hostname = (String)se.getSession().getServletContext().getAttribute("hostname");
                String uri = "ws://" + hostname  +"/users" ;
                container.connectToServer(ChatClientUpdateEndpoint.class,
                        URI.create(uri));
            } catch (IOException | javax.websocket.DeploymentException e) {
               LOG.error("Failed to open client WebSocket. " + e.getMessage());
            }
        }
    }

    private void registerInServletContext(ServletContext servletContext) {

        if (servletContext.getAttribute(KEY) == null) {
            servletContext.setAttribute(KEY, this);
        }
    }

}
