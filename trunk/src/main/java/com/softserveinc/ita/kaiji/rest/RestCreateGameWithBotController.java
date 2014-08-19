package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.sun.jersey.api.view.Viewable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/botgame/create")
@Component
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
    @POST
    @Produces("application/json")
    public Response createRestGameWithCustomSettings(@QueryParam("name") String name,
                                                     @QueryParam("gamename") String gameName,
                                                     @QueryParam("cards") Integer cards,
                                                     @QueryParam("bot") Bot.Types typeOfBot) {


        if (name == null || gameName == null || cards == null || typeOfBot == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if ("".equals(name) && "".equals(gameName) && "".equals(typeOfBot)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (userDAO.getByNickname(name) == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        SystemConfiguration systemConfiguration = systemConfigurationService.getSystemConfiguration();

        if (cards < 0 || cards > systemConfiguration.getNumberOfCards()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
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

        return Response.ok(gameInfoDto).build();
    }

    /*
     *  Client creates game with this method.
     *  Now we can make turn with GET-method to %root%/rest/training/{gameId}/{chosenCard}
     */
    //@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
/*    @POST
    @Path("/create-custom")
    public Integer createRestGameWithCustomSettings(@RequestBody GameInfoDto gameInfoDto) {

        Integer infoId = gameService.setGameInfo(gameInfoDto);
        GameInfo info = gameService.getGameInfo(infoId);
        Integer gameId = gameService.createGame(info);
        if (LOG.isTraceEnabled()) {
            LOG.trace("REST training with bot created. Returning gameId");
        }
        return gameId;
    }*/

}
