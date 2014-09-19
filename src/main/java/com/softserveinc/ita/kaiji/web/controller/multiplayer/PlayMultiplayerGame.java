package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.MultiPlayerRoundFactory;
import com.softserveinc.ita.kaiji.model.game.Round;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.multiplayer.PlayersStatus;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.web.controller.async.GameSyncro;
import com.softserveinc.ita.kaiji.web.controller.async.TimeoutListener;
import com.softserveinc.ita.kaiji.web.controller.async.TurnChecker;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Oleksandra Sydorenko
 * @author Konstantin Shevchuk
 * @author Alexander Vorobyov
 * @author Kyrylo Bardachov
 * @version 1.5
 * @since 02.09.14.
 */

@Controller
@RequestMapping("/game/multiplayer")
@SessionAttributes(value = {"gameId", "enemyObject", "playerObject", "enemyNumCards"})
public class PlayMultiplayerGame {

    private static final Logger LOG = Logger.getLogger(PlayMultiplayerGame.class);

    @Autowired
    private GameService gameService;

    @Autowired
    MultiPlayerRoundFactory mrFactory;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private GameSyncro gameSyncro;

    @RequestMapping(value = "/{gameId}", method = RequestMethod.GET)
    public String createGame(@PathVariable("gameId") Integer gameId) {
        return "redirect:/game/multiplayer/join/" + gameId;
    }

    @RequestMapping(value = "/join/{gameId}", method = RequestMethod.GET)
    public String createdGame(@PathVariable("gameId") Integer gameId,
                              HttpServletRequest request,
                              Model model,
                              Locale locale,
                              Principal principal) {

        if (request.getAttribute("manyPlayers") == Boolean.TRUE ||
                request.getAttribute("finishedGame") == Boolean.TRUE) {
            String errorMessage = request.getAttribute("manyPlayers") == Boolean.TRUE ?
                    messageSource.getMessage("TooManyPlayers.error", null, locale) :
                    messageSource.getMessage("FinishedGame.error", null, locale);
            model.addAttribute("notification", errorMessage);
            model.addAttribute("openedGames", gameService.getRealPlayerInGame());
            return "join-game";
        } else {
            model.addAttribute("gameId", gameId);
            model.addAttribute("playersList", gameService.getAllOtherPlayers(gameId, principal.getName()));
            return "join-multiplayer-game";
        }
    }

    @RequestMapping(value = "/play/{gameId}", method = RequestMethod.GET)
    public String initGame(@PathVariable("gameId") Integer gameId,
                           @RequestParam(value = "enemy", required = false) String enemyPlayerName,
                           Model model, Principal principal) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Starting initGame for multiplayer game.");
        }

        GameHistory gameHistory = gameService.getGameHistory(gameId);
        GameInfo gameInfo = gameHistory.getGameInfo();
        Set<Player> players = gameInfo.getPlayers();

        Player person = (Player) model.asMap().get("playerObject");
        Player enemy = (Player) model.asMap().get("enemyObject");

        if (person == null) {
            for (Player player : players) {
                if (player.getName().equals(principal.getName())) {
                    person = player;
                }
            }
        }

        if (enemy == null) {
            for (Player player : players) {
                if (player.getName().equals(enemyPlayerName)) {
                    enemy = player;
                }
            }
        }

        if (person != null) {
            if (mrFactory.getMultiPlayerRoundBy(person.getName()) == null) {
                if (enemy != null) {
                    mrFactory.addMultiPlayerRound(person.getName(), enemy.getName());
                    person.startPlaying();
                    enemy.startPlaying();
                }
            }
        }

        model.addAttribute("gameId", gameId);
        model.addAttribute("gameHistory", gameHistory);
        model.addAttribute("playerObject", person);
        model.addAttribute("enemyObject", enemy);
        model.addAttribute("enemyNumCards", enemy != null ? enemy.getCardCount() : null);

        return "play-multiplayer-game";
    }

    @RequestMapping(value = "/play/{gameId}/card/{cardName}/",
            method = RequestMethod.GET)
    public void getCard(@PathVariable Integer gameId,
                        @PathVariable String cardName, Model model, Principal principal,
                        HttpServletRequest rq, HttpServletResponse rsp) throws IOException {

        Integer personId = gameService.getPlayerIdFromGame(principal.getName(), gameId);

        Card playerChosenCard = null;
        for (Card card : Card.values()) {
            if (card.name().equals(cardName)) {
                playerChosenCard = card;
            }
        }

        Player player = (Player) model.asMap().get("playerObject");
        Player enemy = (Player) model.asMap().get("enemyObject");
        Integer enemyNumCards = (Integer) model.asMap().get("enemyNumCards");

        Round playerRound = mrFactory.getMultiPlayerRoundBy(player.getName()).getRound();

        gameService.makeTurn(gameId, personId, playerChosenCard, playerRound);

        if (enemy.getCardCount() != (enemyNumCards - 1)) {
            final AsyncContext asyncContext = rq.startAsync(rq, rsp);
            asyncContext.setTimeout(TimeUnit.MILLISECONDS.convert(systemConfigurationService.getSystemConfiguration().getRoundTimeout(), TimeUnit.SECONDS));
            asyncContext.addListener(new TimeoutListener(), rq, rsp);
            gameSyncro.getMultiplayerRoundWaiter().put(playerRound, new CountDownLatch(1));
            asyncContext.start(new TurnChecker(asyncContext, gameId, gameSyncro.getMultiplayerRoundWaiter().get(playerRound),
                    systemConfigurationService.getSystemConfiguration().getRoundTimeout(),
                    "/game/multiplayer/play/"));
            rsp.sendRedirect(rq.getContextPath() + "/game/multiplayer/finishRound/" + gameId);
        } else {
            gameSyncro.getMultiplayerRoundWaiter().get(playerRound).countDown();
            rsp.sendRedirect(rq.getContextPath() + "/game/multiplayer/finishRound/" + gameId);
        }
    }

    @RequestMapping(value = "finish/{gameId}")
    public String finishGame(@PathVariable Integer gameId, ModelMap model, HttpSession session) {
        Player player = (Player) model.get("playerObject");
        mrFactory.removeMultiPlayerRound(player.getName());

        gameService.finishGame(gameId);

        cleanupSessionAttributes(session, model);
        return "redirect:/";
    }

    @RequestMapping(value = "/finishRound/{gameId}", method = RequestMethod.GET)
    public String finishRound(@PathVariable Integer gameId,
                              Model model,
                              RedirectAttributes redirectAttributes) throws IOException {
        GameHistory gameHistory = gameService.getGameHistory(gameId);
        Player player = (Player) model.asMap().get("playerObject");

        redirectAttributes.addFlashAttribute("endRound", true);

        if (gameHistory.getRoundResults().size() != 0)
            redirectAttributes.addFlashAttribute("result", gameHistory.getRoundResults().get(gameHistory.getRoundResults().size() - 1)
                    .getDuelResult(player));
        if (player.getCardCount() == 0 || player.getStar().getQuantity() == 0) {
            redirectAttributes.addFlashAttribute("gameOver", "true");
        } else {
            redirectAttributes.addFlashAttribute("gameOver", "false");
        }
        return "redirect:/game/multiplayer/play/" + gameId;
    }

    @RequestMapping(value = "/finishRound", method = RequestMethod.GET)
    public String finishRound(@RequestParam("gameOver") boolean playerGameOver,
                              ModelMap model,
                              HttpSession session, Principal principal) throws IOException {
        Player player = (Player) model.get("playerObject");
        mrFactory.removeMultiPlayerRound(player.getName());
        Integer gameId = (Integer) model.get("gameId");
        GameInfo gameInfo = gameService.getGameInfo(gameId);

        cleanupSessionAttributes(session, model);

        if (playerGameOver) {
            return "redirect:/";
        } else {
            gameInfo.getPlayerByName(principal.getName()).stopPlaying();
            gameInfo.forceUpdateAllPlayers();
            synchronized (PlayersStatus.getInvitePlayers().get(gameId)) {
                PlayersStatus.getInvitePlayers().get(gameId).notifyAll();
            }
            return "redirect:/game/multiplayer/join/" + gameId;
        }
    }

    private void cleanupSessionAttributes(HttpSession session, ModelMap model) {
        model.remove("enemyObject");
        model.remove("gameId");
        model.remove("enemyNumCards");

        session.removeAttribute("enemyObject");
        session.removeAttribute("gameId");
        session.removeAttribute("enemyNumCards");
    }

}
