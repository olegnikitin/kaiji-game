package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Konstantin Shevchuk
 * @version 1.5
 * @since 14.07.14.
 */

@RestController
@RequestMapping("/rest/botgame/create")
public class RestCreateGameWithBotController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    /*
     *  Example of JSON client will receive:
     *  {"gameName":"Duel","playerName":"user1","botGame":true,
     *  "numberOfCards":4,"numberOfStars":2,"botType":"EASY","gameType":"BOT_GAME","gameId":?}
     *  http://localhost:8080/rest/botgame/create?name=petya&gamename=Game&cards=2&bot=EASY
     */

    @RequestMapping(produces = "application/json", method = RequestMethod.POST)
    public ResponseEntity<GameInfoDto> createRestGameWithCustomSettings(@RequestParam("name") String name,
                                                             @RequestParam("gamename") String gameName,
                                                             @RequestParam("cards") Integer cards,
                                                             @RequestParam("bot") Bot.Types typeOfBot) {


        if (name == null || gameName == null || cards == null || typeOfBot == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if ("".equals(name) && "".equals(gameName) && "".equals(typeOfBot)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (userDAO.findByNickname(name) == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        SystemConfiguration systemConfiguration = systemConfigurationService.getSystemConfiguration();

        if (cards < 0 || cards > systemConfiguration.getNumberOfCards()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        GameInfoDto gameInfoDto = new GameInfoDto();
        gameInfoDto.setGameName(gameName);
        gameInfoDto.setPlayerName(name);
        gameInfoDto.setNumberOfCards(cards);
        gameInfoDto.setNumberOfStars(0);
        gameInfoDto.setGameType(Game.Type.BOT_GAME);
        gameInfoDto.setBotType(typeOfBot);
        gameInfoDto.setPlayerId(0);
        GameInfo info = gameService.getGameInfo(gameService.setGameInfo(gameInfoDto));
        gameInfoDto.setGameId(gameService.createGame(info));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity(gameInfoDto,headers,HttpStatus.OK);
    }

}
