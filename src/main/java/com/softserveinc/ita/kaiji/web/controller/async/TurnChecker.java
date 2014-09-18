package com.softserveinc.ita.kaiji.web.controller.async;

import org.apache.log4j.Logger;

import javax.servlet.AsyncContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Checks for second player`s turn
 *
 * @author Ievgen Sukhov
 * @version 1.0
 * @since 20.04.14.
 */
public class TurnChecker implements Runnable {

    private static final Logger LOG = Logger.getLogger(TurnChecker.class);

    private AsyncContext asyncContext;
    private Integer gameId;
    private CountDownLatch latch;
    private Long timeout;
    private String dispatchLink;

    public TurnChecker(AsyncContext asyncContext,
                       Integer gameId,
                       CountDownLatch latch,
                       Long timeout,
                       String dispatchLink) {
        this.asyncContext = asyncContext;
        this.gameId = gameId;
        this.latch = latch;
        this.timeout = timeout;
        this.dispatchLink = dispatchLink;
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
            asyncContext.dispatch(dispatchLink + gameId + "/");
        }
    }
}

