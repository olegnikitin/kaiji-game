package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.service.UserService;
import com.softserveinc.ita.kaiji.web.controller.async.GameSyncro;
import com.softserveinc.ita.kaiji.web.controller.async.TimeoutListener;
import com.softserveinc.ita.kaiji.web.controller.async.TurnChecker;
import com.softserveinc.ita.kaiji.web.controller.async.TurnMultiplayerChecker;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private GameSyncro gameSyncro;

    @RequestMapping(value = "/{gameId}", method = RequestMethod.GET)
    public String createGame(@PathVariable("gameId") Integer gameId) {
        return "redirect:/game/multiplayer/join/" + gameId;
    }

    @RequestMapping(value = "/join/{gameId}", method = RequestMethod.GET)
    public String createdGame(@PathVariable("gameId") Integer gameId,
                              Model model,
                              Principal principal) {

        //GameInfo info = gameService.getGameInfo(gameId);
        Set<Player> gamePlayers = gameService.getAllOtherPlayers(gameId,principal.getName());//new ArrayList<>(info.getPlayers());
        /*Player playerForRemoving = null;
        for (Player player : gamePlayers) {
            if (player.getName().equals(principal.getName())) {
                playerForRemoving = player;
            }
        }
        gamePlayers.remove(playerForRemoving);*/

        model.addAttribute("gameId", gameId);
        model.addAttribute("playersList", gamePlayers);
        return "join-multiplayer-game";
    }

    @RequestMapping(value = "/play/{gameId}", method = RequestMethod.GET)
    public String initGame(@PathVariable("gameId") Integer gameId, Model model, Principal principal) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Starting initGame for multiplayer game.");
        }

        GameHistory gameHistory = gameService.getGameHistory(gameId);
        GameInfo gameInfo = gameHistory.getGameInfo();
        Set<Player> players = gameInfo.getPlayers();

        Player person = null;
        Player enemy = null;

        for (Player player : players) {
            if (player.getName().equals(principal.getName())) {
                person = player;
            } else {
                enemy = player;
            }
        }

        if (person != null && enemy != null) {
            person.playing(true);
            enemy.playing(true);
        }

        model.addAttribute("gameId", gameId);
        model.addAttribute("gameHistory", gameHistory);
        model.addAttribute("playerObject", person);
        model.addAttribute("enemyObject", enemy);
        model.addAttribute("playerNumCards", person.getCardCount());
        model.addAttribute("enemyNumCards", enemy.getCardCount());

        return "play-multiplayer-game";
    }

    @RequestMapping(value = "/{gameId}/card/{cardName}/",
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

        Player enemy = (Player) model.asMap().get("enemyObject");
        Integer enemyNumCards = (Integer) model.asMap().get("enemyNumCards");

        gameService.makeTurn(gameId, personId, playerChosenCard);

        if (enemy.getCardCount() != (enemyNumCards - 1)) {
            final AsyncContext asyncContext = rq.startAsync(rq, rsp);
            asyncContext.setTimeout(TimeUnit.MILLISECONDS.convert(systemConfigurationService.getSystemConfiguration().getRoundTimeout(), TimeUnit.SECONDS));
            asyncContext.addListener(new TimeoutListener(), rq, rsp);
            gameSyncro.getRoundWaiter().put(gameId, new CountDownLatch(1));
            asyncContext.start(new TurnMultiplayerChecker(asyncContext, gameId, gameSyncro.getRoundWaiter().get(gameId),
                    systemConfigurationService.getSystemConfiguration().getRoundTimeout()));
        } else {
            gameSyncro.getRoundWaiter().get(gameId).countDown();
            rsp.sendRedirect(rq.getContextPath() + "/game/multiplayer/play/" + gameId + "/");
        }
    }
}
