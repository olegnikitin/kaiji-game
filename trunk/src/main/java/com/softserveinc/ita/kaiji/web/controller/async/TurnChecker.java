package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.AsyncContext;

/**
 * Checks for second player`s turn
 * @author Ievgen Sukhov
 * @since 20.04.14.
 * @version 1.0
 */
public class TurnChecker implements Runnable{

    private static final Logger LOG  = Logger.getLogger(TurnChecker.class);

    private UserService userService;
    AsyncContext asyncContext;
    Player enemy;
    Player player;
    Integer gameId;

    public TurnChecker(AsyncContext asyncContext,
                            Integer gameId,
                            UserService userService,
                            Integer enemyId,
                            Integer personId) {
        this.asyncContext = asyncContext;
        this.gameId = gameId;
        this.userService = userService;
        enemy = userService.getPlayerById(enemyId);
        player = userService.getPlayerById(personId);
    }

    @Override
    public void run() {
        if (!enemy.isBot()) {
            try {

                while(!enemy.getCardCount().equals(player.getCardCount()) ){
                    Thread.sleep(1000);
                    enemy = userService.getPlayerById(enemy.getId());
                    player = userService.getPlayerById(player.getId());
                }
            } catch (InterruptedException e) {
                LOG.error( "Failed to check second player turn. " + e.getMessage());
            }
        }
            asyncContext.dispatch("/game/"+gameId+"/");

    }
}

