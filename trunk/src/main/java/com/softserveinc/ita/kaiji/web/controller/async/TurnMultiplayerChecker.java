package com.softserveinc.ita.kaiji.web.controller.async;

import org.apache.log4j.Logger;

import javax.servlet.AsyncContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ALEXANDRA on 10.09.2014.
 */
public class TurnMultiplayerChecker extends TurnChecker {

    private static final Logger LOG = Logger.getLogger(TurnMultiplayerChecker.class);

    public TurnMultiplayerChecker(AsyncContext asyncContext,
                                  Integer gameId,
                                  CountDownLatch latch,
                                  Long timeout) {
        super(asyncContext, gameId, latch, timeout);
    }

    @Override
    public void run() {
        boolean status = false;
        try {
            System.err.println(latch);
            status = latch.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("Failed to check second player turn. " + e.getMessage());
        }
        if (status) {
            asyncContext.dispatch("/game/multiplayer/play/" + gameId + "/");
        }
    }
}
