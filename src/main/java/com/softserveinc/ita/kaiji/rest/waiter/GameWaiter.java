package com.softserveinc.ita.kaiji.rest.waiter;

import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;

/**
 * @author Konstantin Shevchuk
 * @version 1.1
 * @since 17.08.14.
 */

public class GameWaiter implements Runnable {

    private final static Logger LOG = Logger.getLogger(GameWaiter.class);

    private Integer gameId;
    private GameService gameService;

    public GameWaiter(Integer gameId, GameService gameService) {
        this.gameId = gameId;
        this.gameService = gameService;
    }

    @Override
    public void run() {
        try {
            GameInfo info = gameService.getGameInfo(gameId);
            while (info.getPlayers().size() < 2) {
                info = gameService.getGameInfo(gameId);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            LOG.error("Failed to wait for second player. " + e.getMessage());
        }
    }
}
