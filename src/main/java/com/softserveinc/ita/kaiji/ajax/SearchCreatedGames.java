package com.softserveinc.ita.kaiji.ajax;

import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents AJAX live search for created games
 *
 * @author Konstantin Shevchuk
 * @version 1.1
 * @since 25.08.14.
 */

@Controller
@RequestMapping(value = "/game/createdgames")
public class SearchCreatedGames {

    @Autowired
    private GameService gameService;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<String> allCreatedGames(@RequestParam("term") String gameName) {

        List<String> foundGames = new ArrayList<>();
        for (GameInfo gameInfo : gameService.getAllGameInfos()) {
            if (gameInfo.getGameName().startsWith(gameName)) {
                foundGames.add(gameInfo.getGameName());
            }
        }

        return foundGames;
    }
}
