package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;

import javax.servlet.AsyncContext;
import java.util.concurrent.TimeUnit;

public class MultiplayerWaiter implements Runnable {

    private static final Logger LOG = Logger.getLogger(MultiplayerWaiter.class);

    static final Object lock = new Object();

    private GameService gameService;
    private AsyncContext asyncContext;
    private GameInfo info;
    private Integer gameId;
    private Long timeout;

    public MultiplayerWaiter(AsyncContext asyncContext,
                             Integer gameId,
                             GameService gameService,
                             Long timeout) {
        this.asyncContext = asyncContext;
        this.gameId = gameId;
        this.gameService = gameService;
        this.timeout = timeout;
    }

    @Override
    public void run() {

        try {
            synchronized (MultiplayerWaiter.lock) {
                Long tBefore = System.currentTimeMillis();
                info = gameService.getGameInfo(gameId);
                if (info.getPlayers().size() != 2) {
                    MultiplayerWaiter.lock.wait(timeout);
                } else {
                    MultiplayerWaiter.lock.notifyAll();
                }

                if (System.currentTimeMillis() - tBefore >= timeout)
                {
                    LOG.info("Join game connection timeout");
                    return;
                }
            }
            gameId = gameService.createGame(info);
        } catch (InterruptedException e) {
            LOG.error("Failed asynchronously check player. " + e.getMessage());

        }

        asyncContext.dispatch("/game/multiplayer/play");
    }
}
