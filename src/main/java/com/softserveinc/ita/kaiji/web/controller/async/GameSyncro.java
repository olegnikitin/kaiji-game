package com.softserveinc.ita.kaiji.web.controller.async;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

@Component
public class GameSyncro {

    private ConcurrentMap<Integer, CountDownLatch> roundWaiter = new ConcurrentHashMap<>();
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
}
