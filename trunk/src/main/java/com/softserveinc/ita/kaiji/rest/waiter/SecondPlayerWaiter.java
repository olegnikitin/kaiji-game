package com.softserveinc.ita.kaiji.rest.waiter;

import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;

/**
 * @author Konstantin Shevchuk
 * @version 1.1
 * @since 17.08.14.
 */

public class SecondPlayerWaiter implements Runnable {

    private final static Logger LOG = Logger.getLogger(GameWaiter.class);

    private String gameName;
    private GameService gameService;

    public SecondPlayerWaiter(String gameName, GameService gameService) {
        this.gameName = gameName;
        this.gameService = gameService;
    }

    @Override
    public void run() {
        Integer gameId = null;
        try {
            gameId = gameService.getGameId(gameName);
            while (gameId == null) {
                gameId = gameService.getGameId(gameName);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            LOG.error("Failed to wait for game creation. " + e.getMessage());
        }
    }
}
