package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;

/**
 * Checks asynchronously if game is available
 * @author Ievgen Sukhov
 * @since 20.04.14.
 * @version 1.0
 */

public class GameChecker implements Runnable{

    private static final Logger LOG  = Logger.getLogger(SecondPlayerChecker.class);

    private GameService gameService;
    AsyncContext asyncContext;
    String gameName;
    Integer gameId;

    public GameChecker(AsyncContext asyncContext, String gameName, GameService gameService) {
        this.asyncContext = asyncContext;
        this.gameName = gameName;
        this.gameService = gameService;
    }

    @Override
    public void run() {

        try {
            gameId = gameService.getGameId(gameName);
            while(gameId == null) {
                gameId = gameService.getGameId(gameName);
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            LOG.error( "Failed to check game asynchronously. " + e.getMessage());
        }
        asyncContext.dispatch("/game/" + gameId + "/");
    }
}
