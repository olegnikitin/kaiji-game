package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.rest.convertors.ConvertToRestDto;
import com.softserveinc.ita.kaiji.service.GameService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author Boiko Eduard
 * @version 1.1
 * @since 05.04.14
 */

@Path("/botgame/play")
@Component
public class RestPlayGameWithBotController {
    private static final Logger LOG = Logger.getLogger(RestPlayGameWithBotController.class);

    @Autowired
    ConvertToRestDto convertToRestDto;

    @Autowired
    private GameService gameService;

    @GET
    @Produces("application/json")
    @Path("/{gameId}/{chosenCard}")
    public Response makeTurn(@PathParam("gameId") Integer gameId,
                             @PathParam("chosenCard") Card chosenCard) {

        if(gameService.getAllGameInfos().size() == 0){
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
        }

        Integer playerId = gameService.getPlayerIdFromGame(gameId);
        gameService.makeTurn(gameId, playerId,chosenCard);
        GameHistory gameHistory = gameService.getGameHistory(gameId);
        if(gameHistory.getGameStatus().equals(Game.State.GAME_FINISHED)) {
            gameService.finishGame(gameId);
        }
        return Response.ok(convertToRestDto.currentGameInfoToDto(playerId,gameId,chosenCard,gameHistory)).build();
    }

/*    @RequestMapping(value = "/rest/info/{gameName}", method = RequestMethod.GET)
    public GameInfoDto getGame(@PathVariable String gameName) {
        Set<GameInfo> infos = gameService.getAllGameInfos();
        for (GameInfo gi : infos) {
            if (gi.getGameName().equals(gameName)) {
                GameInfoDto res = new GameInfoDto();
                res.setGameName(gi.getGameName());
                res.setPlayerName(gi.getPlayers().toString());
                res.setNumberOfCards(gi.getNumberOfCards());
                res.setGameId(gi.getId());
                res.setGameType(gi.getGameType());
                return res;
            }
        }

        return null;
    }*/
}
