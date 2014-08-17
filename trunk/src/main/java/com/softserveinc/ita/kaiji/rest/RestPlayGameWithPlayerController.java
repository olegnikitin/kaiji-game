package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.rest.convertors.ConvertToRestDto;
import com.softserveinc.ita.kaiji.rest.waiter.SecondPlayerWaiter;
import com.softserveinc.ita.kaiji.rest.waiter.TurnWaiter;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

/**
 * @author Boiko Eduard
 * @version 1.1
 * @since 05.04.14
 */

@Path("/playergame/play")
@Component
public class RestPlayGameWithPlayerController {

    private static final Logger LOG = Logger.getLogger(RestPlayGameWithPlayerController.class);

    @Autowired
    ConvertToRestDto convertToRestDto;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    SystemConfigurationService systemConfigurationService;

    @GET
    @Produces("application/json")
    @Path("/{playerId}/{gameId}/{chosenCard}")
    public Response makeTurn(@PathParam("playerId") Integer playerId,
                             @PathParam("gameId") Integer gameId,
                             @PathParam("chosenCard") Card chosenCard) {

      /*  if(gameService.getAllGameInfos().size() == 0){
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }
        Integer currentGameId = null;

        for(GameInfo gameInfo :gameService.getAllGameInfos()){
            if (gameId.equals(gameInfo.getId())){
                currentGameId = gameId;
                break;
            }
        }
        if(currentGameId == null){
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        }*/


        gameService.makeTurn(gameId, playerId,chosenCard);

        Long timeout = TimeUnit.MILLISECONDS.convert(
                systemConfigurationService.getSystemConfiguration().getRoundTimeout(), TimeUnit.SECONDS);
        Integer player1 = null;
        Integer player2 = null;
        for(Player player :gameService.getGameInfo(gameId).getPlayers()){
            if (player1==null){
                player1 = player.getId();
            } else{
                player2 = player.getId();
            }
        }
        Thread turnWaiter = new Thread(new TurnWaiter(gameId, player1, player2, userService));
        turnWaiter.start();
        try {
            turnWaiter.join(timeout);
        } catch (InterruptedException e) {
            LOG.error("Failed to join secondPlayerWaiter thread. " + e.getMessage());
        }
        if (turnWaiter.isAlive()) {
            turnWaiter.interrupt();
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        GameHistory gameHistory = gameService.getGameHistory(gameId);
        if(gameHistory.getGameStatus().equals(Game.State.GAME_FINISHED)) {
            gameService.finishGame(gameId);
        }
        return Response.ok(convertToRestDto.currentGameInfoToDto(playerId,gameId,chosenCard,gameHistory)).build();
    }

}
