package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.model.game.Round;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Konstantin Shevchuk
 * @version 1.2
 * @since 03.09.14.
 */

@Component
public class GameSyncro {

    private ConcurrentMap<Integer, CountDownLatch> roundWaiter = new ConcurrentHashMap<>();
    private ConcurrentMap<Round, CountDownLatch> multiplayerRoundWaiter = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, CountDownLatch> gameWaiter = new ConcurrentHashMap<>();

    public ConcurrentMap<Integer, CountDownLatch> getRoundWaiter() {
        return roundWaiter;
    }

    public void setRoundWaiter(ConcurrentMap<Integer, CountDownLatch> roundWaiter) {
        this.roundWaiter = roundWaiter;
    }

    public ConcurrentMap<Integer, CountDownLatch> getGameWaiter() {
        return gameWaiter;
    }

    public void setGameWaiter(ConcurrentMap<Integer, CountDownLatch> gameWaiter) {
        this.gameWaiter = gameWaiter;
    }

    public ConcurrentMap<Round, CountDownLatch> getMultiplayerRoundWaiter() {
        return multiplayerRoundWaiter;
    }

    public void setMultiplayerRoundWaiter(ConcurrentMap<Round, CountDownLatch> multiplayerRoundWaiter) {
        this.multiplayerRoundWaiter = multiplayerRoundWaiter;
    }
}
