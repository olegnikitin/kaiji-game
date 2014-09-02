package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;

import javax.servlet.AsyncContext;

public class MultiplayerWaiter implements Runnable {

    private static final Logger LOG = Logger.getLogger(MultiplayerWaiter.class);

    static final Object lock = new Object();

    private GameService gameService;
    private AsyncContext asyncContext;
    private GameInfo info;
    private Integer gameId;

    public MultiplayerWaiter(AsyncContext asyncContext, Integer gameId, GameService gameService) {
        this.asyncContext = asyncContext;
        this.gameId = gameId;
        this.gameService = gameService;
    }

    @Override
    public void run() {

        try {
            synchronized (MultiplayerWaiter.lock) {
                info = gameService.getGameInfo(gameId);
                if (info.getPlayers().size() != 2) {
                    System.err.println("Wait for other players");
                    MultiplayerWaiter.lock.wait();
                } else {
                    MultiplayerWaiter.lock.notifyAll();
                    System.err.println("Notify all players");
                }
            }
            gameId = gameService.createGame(info);
        } catch (InterruptedException e) {
            LOG.error("Failed asynchronously check player. " + e.getMessage());

        }
        System.err.println("Ready to play");
        asyncContext.dispatch("/game/multiplayer/play");
    }
}
