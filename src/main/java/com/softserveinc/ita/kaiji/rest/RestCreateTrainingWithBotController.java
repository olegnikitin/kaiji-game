package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


/**
 * Creates new game with REST. GET-request creates default game-settings object.
 * POST-request creates final game-settings object, which can be passed to RestPlayGameController.
 *
 * @author Boiko Eduard
 * @version 1.1
 * @since 1.04.14
 */


//@RestController
//@RequestMapping(value = "/rest/create-training", produces = "application/json")
@Path("/rest")
@Component
public class RestCreateTrainingWithBotController {

    private static final Logger LOG = Logger.getLogger(RestCreateTrainingWithBotController.class);

    @Autowired
    private GameService gameService;
    @Autowired
    SystemConfigurationService systemConfigurationService;

    /*
     *  Example of JSON client will receive:
     *  {"gameName":"Duel","playerName":"user1","botGame":true,
     *  "numberOfCards":4,"botType":"EASY","gameType":null,"gameId":null}
     */
    //@RequestMapping(method = RequestMethod.GET)
    @GET
    @Path("/create-training")
    @Produces("application/json")
    public GameInfoDto createRestGameWithDefaultSettings() {

        SystemConfiguration systemConfiguration = systemConfigurationService.getSystemConfiguration();

        if (LOG.isTraceEnabled()) {
            LOG.trace("Received system configuration. Creating default GameInfoDto object");
        }

        GameInfoDto gameInfoDto = new GameInfoDto();
        gameInfoDto.setGameName(systemConfiguration.getGameName());
        gameInfoDto.setPlayerName(systemConfiguration.getUserName());
        gameInfoDto.setNumberOfCards(systemConfiguration.getNumberOfCards());
        gameInfoDto.setBotType(systemConfiguration.getBotType());

        if (LOG.isTraceEnabled()) {
            LOG.trace("Default GameInfoDto created. Passing it to REST user");
        }
        return gameInfoDto;
    }

    /*
     *  Client creates game with this method.
     *  Now we can make turn with GET-method to %root%/rest/training/{gameId}/{chosenCard}
     */
    //@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @POST
    @Path("/create-training")
    public Integer createRestGameWithCustomSettings(@RequestBody GameInfoDto gameInfoDto) {

        Integer infoId = gameService.setGameInfo(gameInfoDto);
        GameInfo info = gameService.getGameInfo(infoId);
        Integer gameId = gameService.createGame(info);
        if (LOG.isTraceEnabled()) {
            LOG.trace("REST training with bot created. Returning gameId");
        }
        return gameId;
    }

}
