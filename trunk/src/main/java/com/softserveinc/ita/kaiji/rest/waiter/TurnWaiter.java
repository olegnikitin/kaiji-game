package com.softserveinc.ita.kaiji.rest.waiter;

import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.UserService;
import org.apache.log4j.Logger;

/**
 * @author Konstantin Shevchuk
 * @version 1.2
 * @since 17.08.14.
 */

public class TurnWaiter implements Runnable {

    private static final Logger LOG = Logger.getLogger(TurnWaiter.class);

    private UserService userService;
    private Player enemy;
    private Player player;

    public TurnWaiter(Integer enemyId,
                      Integer personId,
                      UserService userService) {
        this.userService = userService;
        this.enemy = userService.getPlayerById(enemyId);
        this.player = userService.getPlayerById(personId);
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (enemy == null || player == null) {
                    break;
                } else if (enemy.getCardCount().equals(player.getCardCount())) {
                    break;
                }
                Thread.sleep(1000);
                enemy = userService.getPlayerById(enemy.getId());
                player = userService.getPlayerById(player.getId());
            }
        } catch (InterruptedException e) {
            LOG.error("Failed to check second player turn. " + e.getMessage());
        }
    }
}
