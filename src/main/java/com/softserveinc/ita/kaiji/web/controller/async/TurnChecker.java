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

    //private UserService userService;
    AsyncContext asyncContext;
    //Player enemy;
    //Player player;
    Integer gameId;
    private CountDownLatch latch;
    private Long timeout;

    public TurnChecker(AsyncContext asyncContext,
                       Integer gameId,
                       //UserService userService,
                       //Integer enemyId,
                       //Integer personId,
                       CountDownLatch latch,
                       Long timeout) {
        this.asyncContext = asyncContext;
        this.gameId = gameId;
        //this.userService = userService;
        //enemy = userService.getPlayerById(enemyId);
        //player = userService.getPlayerById(personId);
        this.latch = latch;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        boolean status = false;
        try {
             status = latch.await(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("Failed to check second player turn. " + e.getMessage());
        }
        /*if (!enemy.isBot()) {
            try {

                while (!enemy.getCardCount().equals(player.getCardCount())) {
                    Thread.sleep(1000);
                    enemy = userService.getPlayerById(enemy.getId());
                    player = userService.getPlayerById(player.getId());
                }
            } catch (InterruptedException e) {
                LOG.error("Failed to check second player turn. " + e.getMessage());
            }
        }*/
        if(status) {
            asyncContext.dispatch("/game/" + gameId + "/");
        }
    }
}

