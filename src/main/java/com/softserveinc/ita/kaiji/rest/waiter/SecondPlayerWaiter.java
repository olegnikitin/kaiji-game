package com.softserveinc.ita.kaiji.rest.waiter;


import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;

public class SecondPlayerWaiter implements Runnable {

    private final static Logger LOG = Logger.getLogger(GameWaiter.class);

    private String gameName;
    private GameService gameService;

    public SecondPlayerWaiter(String gameName, GameService gameService){
        this.gameName = gameName;
        this.gameService = gameService;
    }

    @Override
    public void run() {
        Integer gameId = null;
        try {
            gameId = gameService.getGameId(gameName);
            while(gameId == null) {
                gameId = gameService.getGameId(gameName);
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            LOG.error( "Failed to wait for game creation. " + e.getMessage());
        }
    }
}
