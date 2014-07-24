package com.softserveinc.ita.kaiji.web.controller.async;

import org.apache.log4j.Logger;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;

/**
 * Checks for timeouts
 * @author Ievgen Sukhov
 * @since 20.04.14.
 * @version 1.0
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
        event.getAsyncContext().getRequest().setAttribute("timeout", true);
        event.getAsyncContext().dispatch("/game/join");

    }
}

