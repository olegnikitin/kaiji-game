package com.softserveinc.ita.kaiji.model.player;

import com.softserveinc.ita.kaiji.model.Card.DuelResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Statistics for results on played rounds in one game is stored here
 *
 * @author Ievgen Sukhov
 * @version 1.1
 * @since 1.04.14
 */


public class PlayerStatistics {

    private Map<DuelResult, Integer> allStats;

    PlayerStatistics() {

        this.allStats = new HashMap<>();
        allStats.put(DuelResult.WIN, 0);
        allStats.put(DuelResult.LOSE, 0);
        allStats.put(DuelResult.DRAW, 0);
    }

    /**
     * Increments specific stat depending on result of round
     *
     * @param result - result of previous round
     */
    public void incrementStats(DuelResult result) {
        Integer stat = allStats.get(result);
        ++stat;
        allStats.put(result, stat);
    }

    /**
     * Returns map with all possible results and statistic for them on current
     * player
     *
     * @return {@link Map} of {@link DuelResult} keys and values for them
     */
    public Map<DuelResult, Integer> getStatistic() {
        return Collections.unmodifiableMap(allStats);
    }

    /**
     * Gets stats for specific {@link DuelResult}
     *
     * @param result type of result
     * @return {@link Integer} count on current result
     */
    public Integer getSpecificStat(DuelResult result) {

        return allStats.get(result);
    }

}
