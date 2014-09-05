package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;

import javax.servlet.AsyncContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Checks asynchronously if second player joined the game
 *
 * @author Ievgen Sukhov
 * @version 1.0
 * @since 20.04.14.
 */
public class SecondPlayerChecker implements Runnable {

    private static final Logger LOG = Logger.getLogger(SecondPlayerChecker.class);

    private GameService gameService;
    private AsyncContext asyncContext;
    private Integer gameId;
    private CountDownLatch latch;
    private Long timeout;

    public SecondPlayerChecker(AsyncContext asyncContext,
                               GameService gameService,
                               Integer gameId,
                               CountDownLatch latch,
                               Long timeout) {
        this.asyncContext = asyncContext;
        this.gameId = gameId;
        this.latch = latch;
        this.timeout = timeout;
        this.gameService = gameService;
    }

    @Override
    public void run() {

        boolean status = false;
        try {
            status = latch.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("Failed to check joined player turn. " + e.getMessage());
        }
        if (status) {
            gameId = gameService.createGame(gameService.getGameInfo(gameId));
            asyncContext.dispatch("/game/" + gameId + "/");
        }

        /*try {
            info = gameService.getGameInfo(gameId);
            while(info.getPlayers().size() < 2) {
                info = gameService.getGameInfo(gameId);
                Thread.sleep(3000);
            }

        } catch (InterruptedException e) {
            LOG.error( "Failed asynchronously check second player. " + e.getMessage());

        }*/
        //asyncContext.dispatch("/game/" + gameId + "/");
    }
}
