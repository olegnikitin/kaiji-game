package com.softserveinc.ita.kaiji.web.controller.async;

import org.apache.log4j.Logger;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

/**
 * Checks for timeouts
 *
 * @author Ievgen Sukhov
 * @version 1.0
 * @since 20.04.14.
 */
public class TimeoutListener implements AsyncListener {

    private static final Logger LOG = Logger.getLogger(TimeoutListener.class);

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        LOG.trace("AsyncListener Event completed." + event.toString());
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        LOG.error("AsyncListener Event error." + event.toString());
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        LOG.error("AsyncListener Event started." + event.toString());
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://" + event.getAsyncContext().
                getRequest().getServletContext().getAttribute("hostname") + "/timeout/" + event.getSuppliedRequest()
                .getAttribute("id");
        try {
            container.connectToServer(ClientTimeoutRound.class,
                    URI.create(uri));
        } catch (IOException | javax.websocket.DeploymentException e) {
            System.out.println(e.getMessage() + " " + URI.create(uri));
        }
        event.getAsyncContext().dispatch("/game/join?timeout=true");

    }
}

