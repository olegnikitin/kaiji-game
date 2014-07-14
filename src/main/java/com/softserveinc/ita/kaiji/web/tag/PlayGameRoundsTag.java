package com.softserveinc.ita.kaiji.web.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.RoundResult;
import com.softserveinc.ita.kaiji.model.player.Player;

/**
 * @author Eugene Semenkov
 * @version 1.0.3
 * @since 26.03.14
 */

public class PlayGameRoundsTag extends SimpleTagSupport {

    private GameHistory gameHistory;
    private Player player;
    private Player enemy;

    @Override
    public void doTag() throws JspException, IOException {

        PageContext pageContext = (PageContext) getJspContext();

        List<RoundResult> roundResults = gameHistory.getRoundResults();

        for (int i = roundResults.size(); i > 0; i--) {

            pageContext.setAttribute("numberOfRound", i);

            pageContext.setAttribute("playerStatus", roundResults.get(i - 1).getDuelResult(player));
            pageContext.setAttribute("enemyStatus", roundResults.get(i - 1).getDuelResult(enemy));

            pageContext.setAttribute("playerCardIconUrl", getCardIconUrlByCard(roundResults.get(i - 1).getCard(player)));
            pageContext.setAttribute("enemyCardIconUrl", getCardIconUrlByCard(roundResults.get(i - 1).getCard(enemy)));
            getJspBody().invoke(null);

        }

    }

    private String getCardIconUrlByCard(Card card) {

        switch (card) {

            case PAPER:
                return "http://i58.tinypic.com/2u6pb93.png";

            case ROCK:
                return "http://i61.tinypic.com/2cyntlk.png";

            case SCISSORS:
                return "http://i62.tinypic.com/xngqb9.png";

        }

        return null;

    }

    public GameHistory getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(GameHistory gameHistory) {
        this.gameHistory = gameHistory;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getEnemy() {
        return enemy;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }

}
