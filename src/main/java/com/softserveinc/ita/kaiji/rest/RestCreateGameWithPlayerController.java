package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.rest.dto.ConvertToRestDto;
import com.softserveinc.ita.kaiji.rest.dto.GameJoinRestDto;
import com.softserveinc.ita.kaiji.rest.waiter.GameWaiter;
import com.softserveinc.ita.kaiji.rest.waiter.SecondPlayerWaiter;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Path("/playergame/create")
@Component
public class RestCreateGameWithPlayerController {

    private static final Logger LOG = Logger.getLogger(RestCreateGameWithPlayerController.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ConvertToRestDto convertToRestDto;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    private GameInfoDto gameInfoDto;

    /*
     *  Example of JSON client will receive:
     *  {"gameName":"Duel","playerName":"user1","botGame":true,
     *  "numberOfCards":4, "numberOfStars":2,"botType":"EASY","gameType":"BOT_GAME","gameId":?}
     * http://localhost:8080/rest/playergame/create?name=petya&gamename=GAME&cards=2&stars=3
     */
    @POST
    @Produces("application/json")
    public Response createRestGameWithPlayer(@QueryParam("name") String name,
                                             @QueryParam("gamename") String gameName,
                                             @QueryParam("cards") Integer cards,
                                             @QueryParam("stars") Integer stars) {


        if (name == null || gameName == null || cards == null || stars == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if ("".equals(name) || "".equals(gameName)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (userDAO.getByNickname(name) == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        SystemConfiguration systemConfiguration = systemConfigurationService.getSystemConfiguration();

        if (cards < 0 || cards > systemConfiguration.getNumberOfCards()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (stars < 0 || stars > systemConfiguration.getNumberOfCards()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        gameInfoDto = new GameInfoDto();
        gameInfoDto.setGameName(gameName);
        gameInfoDto.setPlayerName(name);
        gameInfoDto.setNumberOfCards(cards);
        gameInfoDto.setNumberOfStars(stars);
        gameInfoDto.setGameType(Game.Type.TWO_PLAYER_GAME);
        gameInfoDto.setBotType(Bot.Types.EASY);
        gameInfoDto.setBotGame(false);
        gameInfoDto.setPlayerId(0);
        Integer gameId = gameService.setGameInfo(gameInfoDto);
        GameInfo info = gameService.getGameInfo(gameId);
        info.setNumberOfPlayers(1);
        Long timeout = TimeUnit.MILLISECONDS.convert(
                systemConfigurationService.getSystemConfiguration().getGameConnectionTimeout(), TimeUnit.SECONDS);

        Thread gameWaiter = new Thread(new GameWaiter(gameId, gameService));
        gameWaiter.start();
        try {
            gameWaiter.join(timeout);
        } catch (InterruptedException e) {
            LOG.error("Failed to join gameWaiter thread. " + e.getMessage());
        }

        if (gameWaiter.isAlive()) {
            gameWaiter.interrupt();
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        gameId = gameService.createGame(info);
        gameInfoDto.setGameId(gameId);
        return Response.ok(gameInfoDto).build();
    }

    //http://localhost:8080/rest/playergame/create/joingames
    @GET
    @Produces("application/json")
    @Path("/joingames")
    public Response getOpenedGames() {

        List<GameJoinRestDto> joinGameInfos = new ArrayList<>();
        for (GameInfo gameInfo : gameService.getAllGameInfos()) {
            joinGameInfos.add(convertToRestDto.joinGameInfoToDto(gameInfo));
        }
        return Response.ok(joinGameInfos).build();
    }

    @POST
    @Produces("application/json")
    @Path("/joingame")
    public Response joinOpenGame(@QueryParam("nickname") String nickname,
                                 @QueryParam("gamename") String gameName) {

        if (gameName == null || nickname == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if ("".equals(gameName) || "".equals(nickname)) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        if (userDAO.getByNickname(nickname) == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        String currentGameName = null;
        GameInfo info = null;
        for (GameInfo gameInfo : gameService.getAllGameInfos()) {
            if (gameName.equals(gameInfo.getGameName())) {
                currentGameName = gameName;
                info = gameInfo;
                break;
            }
        }
        if (currentGameName == null) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Integer playerId = gameService.addPlayer(nickname, gameName);
        Long timeout = TimeUnit.MILLISECONDS.convert(
                systemConfigurationService.getSystemConfiguration().getGameConnectionTimeout(), TimeUnit.SECONDS);
        Thread secondPlayerWaiter = new Thread(new SecondPlayerWaiter(gameName, gameService));
        secondPlayerWaiter.start();

        try {
            secondPlayerWaiter.join(timeout);
        } catch (InterruptedException e) {
            LOG.error("Failed to join secondPlayerWaiter thread. " + e.getMessage());
        }
        if (secondPlayerWaiter.isAlive()) {
            secondPlayerWaiter.interrupt();
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        Integer numberOfPlayers = info.getNumberOfPlayers();
        info.setNumberOfPlayers(++numberOfPlayers);
        GameInfoDto infoDto = null;
        try {
            infoDto = (GameInfoDto) gameInfoDto.clone();
        } catch (CloneNotSupportedException e) {
            LOG.error("Can't clone GameInfoDto object " + e.getMessage());
            throw new RuntimeException(e);
        }
        infoDto.setPlayerId(playerId);
        infoDto.setPlayerName(nickname);
        return Response.ok(infoDto).build();
    }
}
