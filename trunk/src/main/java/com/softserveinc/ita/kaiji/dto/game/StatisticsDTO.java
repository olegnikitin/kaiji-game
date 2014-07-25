package com.softserveinc.ita.kaiji.dto.game;

/**
 * @author Ievgen Sukhov
 * @since  21.04.14
 * @version 1.0
 */
public class StatisticsDTO {
    private Long win;
    private Long lose;
    private Long draw;
    private Long gameWins;
    private Long totalGames;

    public StatisticsDTO() {
    }

    public Long getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(Long totalGames) {
        this.totalGames = totalGames;
    }

    public Long getGameWins() {
        return gameWins;
    }

    public void setGameWins(Long gameWins) {
        this.gameWins = gameWins;
    }

    public Long getDraw() {
        return draw;
    }

    public void setDraw(Long draw) {
        this.draw = draw;
    }

    public Long getLose() {
        return lose;
    }

    public void setLose(Long lose) {
        this.lose = lose;
    }

    public Long getWin() {
        return win;
    }

    public void setWin(Long win) {
        this.win = win;
    }


}

