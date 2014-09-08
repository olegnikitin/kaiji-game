package com.softserveinc.ita.kaiji.web.controller.multiplayer;

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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/game/multiplayer/play")
public class PlayMultiplayerGame {

    private static final Logger LOG = Logger.getLogger(PlayMultiplayerGame.class);

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/{gameId}",method = RequestMethod.GET)
    public String createGame(@PathVariable("gameId") Integer gameId, Model model, Principal principal) {
        GameInfo info = gameService.getGameInfo(gameId);
        List<Player> gamePlayers = new ArrayList<>(info.getPlayers());
        Player playerForRemoving = null;
        for (Player player : gamePlayers) {
            if (player.getName().equals(principal.getName())) {
                playerForRemoving = player;
            }
        }
        gamePlayers.remove(playerForRemoving);

        model.addAttribute("playersList", gamePlayers);
        return "join-multiplayer-game";
    }


}
