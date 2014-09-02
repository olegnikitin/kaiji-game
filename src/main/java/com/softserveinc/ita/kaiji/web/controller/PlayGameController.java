package com.softserveinc.ita.kaiji.web.controller;


import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.service.UserService;
import com.softserveinc.ita.kaiji.web.controller.async.TimeoutListener;
import com.softserveinc.ita.kaiji.web.controller.async.TurnChecker;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Eugene Semenkov
 * @version 2.0.0
 * @since 17.03.14
 */

@Controller
@RequestMapping("/game")
@SessionAttributes({"playerId", "enemyObject"})
public class PlayGameController {


    private static final Logger LOG = Logger
            .getLogger(PlayGameController.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private UserService userService;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    @RequestMapping(value = "/{gameId}/",
            method = {RequestMethod.GET, RequestMethod.POST})
    public String initGame(@PathVariable Integer gameId, HttpServletRequest request,
                           Model model) {
        request.getSession().setAttribute("gameId",gameId);
        Integer playerId = (Integer) model.asMap().get("playerId");

        if (LOG.isTraceEnabled()) {
            LOG.trace("Starting initGame.");
        }

        LOG.info("GameId: " + gameId + ". PersonId: " + playerId);

        GameHistory gameHistory = gameService.getGameHistory(gameId);
        GameInfo gameInfo = gameHistory.getGameInfo();
        Set<Player> players = gameInfo.getPlayers();

        Player person = null;
        Player enemy = null;

        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                person = player;
            } else {
                enemy = player;
            }
        }

        model.addAttribute("gameId", gameId);
        model.addAttribute("gameHistory", gameHistory);
        model.addAttribute("playerObject", person);
        model.addAttribute("enemyObject", enemy);
       // request.getSession().setAttribute("id", gameId);

        return "play-game";

    }

    @RequestMapping(value = "/{gameId}/card/{cardName}/",
            method = RequestMethod.GET)
    public void getCard(@PathVariable Integer gameId,
                        @PathVariable String cardName,
                        Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Integer personId = (Integer) model.asMap().get("playerId");
        if (LOG.isTraceEnabled()) {
            LOG.trace("Starting getCard.");
        }

        LOG.info("GameId: " + gameId + ". CardId: "
                + cardName + ". PersonId: " + personId);

        Card playerChosenCard = null;
        for (Card card : Card.values()) {
            if (card.name().equals(cardName)) {
                playerChosenCard = card;
            }
        }

        gameService.makeTurn(gameId, personId, playerChosenCard);

        Player player = userService.getPlayerById(personId);
        Player enemy = (Player) model.asMap().get("enemyObject");

        request.setAttribute("id",gameId);

        //starting async
        if (!enemy.isBot() & (enemy.getCardCount() != player.getCardCount())) {
            final AsyncContext asyncContext = request.startAsync(request, response);
            asyncContext.setTimeout(TimeUnit.MILLISECONDS.convert(systemConfigurationService.getSystemConfiguration().getRoundTimeout(), TimeUnit.SECONDS));
            asyncContext.addListener(new TimeoutListener(), request, response);

            asyncContext.start(new TurnChecker(asyncContext, gameId, userService, enemy.getId(), personId));
        } else {
            response.sendRedirect(request.getContextPath() + "/game/" + gameId + "/");
        }
    }


    @RequestMapping(value = "finish/{gameId}")
    public String finishGame(@PathVariable Integer gameId) {
        gameService.finishGame(gameId);
        return "redirect:/game/join";
    }

    @RequestMapping(value = "cleanup/{gameInfoId}")
    public String deleteGame(@PathVariable Integer gameInfoId) {
        gameService.clearGameInfo(gameInfoId);
        return "redirect:/game/join";
    }

    @RequestMapping(value = "join")
    public String getJoinForm(HttpServletRequest request, Model model, Locale locale) {
        if ("true".equals(request.getParameter("timeout"))) {
            String errorMessage = messageSource.getMessage("Timeout.error", null, locale);
            model.addAttribute("notification", errorMessage);
            return "start-page";
        } else if ((Boolean) request.getAttribute("manyPlayers") == Boolean.TRUE) {
            String errorMessage = messageSource.getMessage("TooManyPlayers.error", null, locale);
            model.addAttribute("notification", errorMessage);

        }
        model.addAttribute("openedGames", gameService.getRealPlayerGames());

        return "join-game";
    }

}

