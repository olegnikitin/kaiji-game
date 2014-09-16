package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.MultiPlayerRoundFactory;
import com.softserveinc.ita.kaiji.model.game.Round;
import com.softserveinc.ita.kaiji.model.player.Player;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/game/multiplayer")
@SessionAttributes(value = {"gameId", "enemyObject", "playerObject", "playerNumCards", "enemyNumCards"})
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

        if ((Boolean) request.getAttribute("manyPlayers") == Boolean.TRUE) {
            String errorMessage = messageSource.getMessage("TooManyPlayers.error", null, locale);
            model.addAttribute("notification", errorMessage);
            model.addAttribute("openedGames", gameService.getRealPlayerInGame());
            return "join-game";
        } else {
            GameInfo info = gameService.getGameInfo(gameId);
            List<Player> gamePlayers = new ArrayList<>(info.getPlayers());
            Player playerForRemoving = null;
            for (Player player : gamePlayers) {
                if (player.getName().equals(principal.getName())) {
                    playerForRemoving = player;
                }
            }
            gamePlayers.remove(playerForRemoving);

            model.addAttribute("gameId", gameId);
            model.addAttribute("playersList", gamePlayers);
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

        if (person == null && enemy == null) {
            for (Player player : players) {
                if (player.getName().equals(principal.getName())) {
                    person = player;
                }
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
        if (gameHistory.getRoundResults().size() != 0)
            model.addAttribute("result", gameHistory.getRoundResults().get(gameHistory.getRoundResults().size() - 1)
                    .getDuelResult(person));

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
            rsp.sendRedirect(rq.getContextPath() + "/game/multiplayer/showPopup/" + gameId);
        } else {
            gameSyncro.getMultiplayerRoundWaiter().get(playerRound).countDown();
            rsp.sendRedirect(rq.getContextPath() + "/game/multiplayer/showPopup/" + gameId);
        }
    }

    @RequestMapping(value = "finish/{gameId}")
    public String finishGame(@PathVariable Integer gameId) {
        gameService.finishGame(gameId);
        return "redirect:/game/multiplayer/join/{gameId}";
    }

    @RequestMapping(value = "/showPopup/{gameId}", method = RequestMethod.GET)
    public String asd(@PathVariable Integer gameId,
                      Model model,
                      RedirectAttributes ra) throws IOException {
        ra.addFlashAttribute("endRound", true);
        if (((Player) model.asMap().get("playerObject")).getCardCount() == 0 ||
                ((Player) model.asMap().get("playerObject")).getStar().getQuantity() == 0) {
            ra.addFlashAttribute("gameOver", "true");
        } else {
            ra.addFlashAttribute("gameOver", "false");
        }
        return "redirect:/game/multiplayer/play/" + gameId;
    }

    @RequestMapping(value = "/finishRound/{enemy}", method = RequestMethod.GET)
    public String finishRound(@PathVariable String enemy,
                              @RequestParam("gameOver") boolean gameOver,
                              Model model,
                              RedirectAttributes ra) throws IOException {
        mrFactory.removeMultiPlayerRound(enemy);
        Integer gameId = (Integer)model.asMap().get("gameId");
        if (gameOver) {
            return "redirect:/";
        } else {
            gameService.getGameInfo(gameId).getPlayerByName(enemy).stopPlaying();
            return "redirect:/game/multiplayer/join/" + model.asMap().get("gameId");
        }
    }

}
