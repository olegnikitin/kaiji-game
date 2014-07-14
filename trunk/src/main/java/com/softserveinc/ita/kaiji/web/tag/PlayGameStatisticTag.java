package com.softserveinc.ita.kaiji.web.tag;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.Player;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.Map;

/**
 * @author Eugene Semenkov
 * @version 1.0.2
 * @since 26.03.14
 */

public class PlayGameStatisticTag extends SimpleTagSupport {

    private Player player;

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspFragment jspBody = getJspBody();

        Map<Card.DuelResult, Integer> statistic = player.getStatistic().getStatistic();

        pageContext.setAttribute("duelResult", Card.DuelResult.WIN);
        pageContext.setAttribute("count", statistic.get(Card.DuelResult.WIN));
        jspBody.invoke(null);

        pageContext.setAttribute("duelResult", Card.DuelResult.DRAW);
        pageContext.setAttribute("count", statistic.get(Card.DuelResult.DRAW));
        jspBody.invoke(null);

        pageContext.setAttribute("duelResult", Card.DuelResult.LOSE);
        pageContext.setAttribute("count", statistic.get(Card.DuelResult.LOSE));
        jspBody.invoke(null);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
