package com.softserveinc.ita.kaiji.model.game;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Sydorenko Oleksandra
 * @version 1.0
 * @since 10.09.14
 */
public class MultiPlayerRoundFactory {

    public static class MultiPlayerRound {
        private Set<String> playersNames;
        private Round round;

        public MultiPlayerRound() {
        }

        public MultiPlayerRound(Set<String> players, Round round) {
            this.playersNames = players;
            this.round = round;
        }

        public Set<String> getPlayersNames() {
            return playersNames;
        }

        public Round getRound() {
            return round;
        }
    }

    private static ConcurrentLinkedQueue<MultiPlayerRound> multiPlayerRoundList = new ConcurrentLinkedQueue<>();

    public ConcurrentLinkedQueue<MultiPlayerRound> getMultiplayerRoundList() {
        return multiPlayerRoundList;
    }

    public MultiPlayerRound getMultiPlayerRoundBy(String playerName) {
        for (MultiPlayerRound mr : multiPlayerRoundList) {
            for (String p : mr.getPlayersNames()) {
                if (p.equals(playerName)) {
                    return mr;
                }
            }
        }
        return null;
    }

    public void addMultiPlayerRound(String firstPlayerName, String secondPlayerName) {
        Set<String> playersName = new HashSet<>();
        playersName.add(firstPlayerName);
        playersName.add(secondPlayerName);
        MultiPlayerRound mRound = new MultiPlayerRound(playersName, new StateRoundImpl());
        multiPlayerRoundList.add(mRound);
    }

    public void removeMultiPlayerRound(String playerName) {
        MultiPlayerRound roundForRemoving = null;
        for (MultiPlayerRound mr : multiPlayerRoundList) {
            for (String p : mr.getPlayersNames()) {
                if (p.equals(playerName)) {
                    roundForRemoving = mr;
                }
            }
        }
        if (roundForRemoving != null) {
            multiPlayerRoundList.remove(roundForRemoving);
        }
    }
}















