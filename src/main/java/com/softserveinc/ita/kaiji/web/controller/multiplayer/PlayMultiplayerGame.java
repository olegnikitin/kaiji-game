package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/game/multiplayer")
@SessionAttributes("gameId")
public class PlayMultiplayerGame {

    private static final Logger LOG = Logger.getLogger(PlayMultiplayerGame.class);

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/{gameId}", method = RequestMethod.GET)
    public String createGame(@PathVariable("gameId") Integer gameId) {
        return "redirect:/game/multiplayer/join/" + gameId;
    }

    @RequestMapping(value = "/join/{gameId}", method = RequestMethod.GET)
    public String createdGame(@PathVariable("gameId") Integer gameId,
                              Model model,
                              Principal principal) {

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

    @RequestMapping(value = "/play/{gameId}",method = RequestMethod.GET)
    public String initGame(@PathVariable("gameId") Integer gameId, Model model, Principal principal){
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

        model.addAttribute("gameId", gameId);
        model.addAttribute("gameHistory", gameHistory);
        model.addAttribute("playerObject", person);
        model.addAttribute("enemyObject", enemy);

        return "play-multiplayer-game";
    }
}
