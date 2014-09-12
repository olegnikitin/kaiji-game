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
            RoundResult currentRoundResult = roundResults.get(i - 1);
            if (currentRoundResult.getPlayers().contains(player) && currentRoundResult.getPlayers().contains(enemy)) {
                pageContext.setAttribute("numberOfRound", i);

                pageContext.setAttribute("playerStatus", currentRoundResult.getDuelResult(player));
                pageContext.setAttribute("enemyStatus", currentRoundResult.getDuelResult(enemy));

                pageContext.setAttribute("playerCardIconUrl", getCardIconUrlByCard(currentRoundResult.getCard(player)));
                pageContext.setAttribute("enemyCardIconUrl", getCardIconUrlByCard(currentRoundResult.getCard(enemy)));
                getJspBody().invoke(null);

            }
        }
    }

    private String getCardIconUrlByCard(Card card) {

        String cardIcon;

        switch (card) {

            case PAPER:
                cardIcon = "http://i58.tinypic.com/2u6pb93.png";
                break;
            case ROCK:
                cardIcon = "http://i61.tinypic.com/2cyntlk.png";
                break;
            case SCISSORS:
                cardIcon = "http://i62.tinypic.com/xngqb9.png";
                break;
            default:
                cardIcon = null;
        }

        return cardIcon;

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
