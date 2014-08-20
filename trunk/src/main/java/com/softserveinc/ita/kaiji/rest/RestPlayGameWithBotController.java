package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.rest.dto.ConvertToRestDto;
import com.softserveinc.ita.kaiji.rest.dto.CurrentGameRestInfoDto;
import com.softserveinc.ita.kaiji.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@RequestMapping("rest/botgame/play")
@RestController
public class RestPlayGameWithBotController {

    @Autowired
    private ConvertToRestDto convertToRestDto;

    @Autowired
    private GameService gameService;

    @Autowired
    private RestUtils restUtils;

    //
    @RequestMapping(value = "/{gameId}/{chosenCard}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<CurrentGameRestInfoDto> makeTurn(@PathVariable("gameId") Integer gameId,
                                                           @PathVariable("chosenCard") Card chosenCard) {

        if (gameId == null || chosenCard == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (restUtils.isGameFinished(gameService.getAllGameInfos(), gameId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Integer playerId = gameService.getPlayerIdFromGame(gameId);
        for (Player player : gameService.getGameInfo(gameId).getPlayers()) {
            if (player.getId().equals(playerId)) {
                if (player.getDeck().getCardTypeCount(chosenCard) == 0) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            }
        }
        gameService.makeTurn(gameId, playerId, chosenCard);
        GameHistory gameHistory = gameService.getGameHistory(gameId);
        if (gameHistory.getGameStatus().equals(Game.State.GAME_FINISHED)) {
            gameService.finishGame(gameId);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity(convertToRestDto.currentGameInfoToDto(playerId, gameId, chosenCard, gameHistory), headers, HttpStatus.OK);

    }
}