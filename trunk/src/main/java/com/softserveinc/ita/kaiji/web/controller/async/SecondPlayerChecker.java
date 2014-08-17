package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.AsyncContext;

/**
 * Checks asynchronously if second player joined the game
 * @author Ievgen Sukhov
 * @since 20.04.14.
 * @version 1.0
 */
public class SecondPlayerChecker implements Runnable {

    private static final Logger LOG  = Logger.getLogger(SecondPlayerChecker.class);

    private GameService gameService;
    AsyncContext asyncContext;
    GameInfo info;
    Integer gameId;

    public SecondPlayerChecker(AsyncContext asyncContext, Integer gameId, GameService gameService) {
        this.asyncContext = asyncContext;
        this.gameId = gameId;
        this.gameService = gameService;
    }

    @Override
    public void run() {

        try {
            info = gameService.getGameInfo(gameId);
            while(info.getPlayers().size() < 2) {
                info = gameService.getGameInfo(gameId);
                Thread.sleep(3000);
            }
            gameId = gameService.createGame(info);
        } catch (InterruptedException e) {
            LOG.error( "Failed asynchronously check second player. " + e.getMessage());

        }

        asyncContext.dispatch("/game/" + gameId +"/");
    }
}
