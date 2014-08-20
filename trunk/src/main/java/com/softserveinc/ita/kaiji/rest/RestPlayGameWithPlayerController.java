package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.rest.dto.ConvertToRestDto;
import com.softserveinc.ita.kaiji.rest.dto.CurrentGameRestInfoDto;
import com.softserveinc.ita.kaiji.rest.waiter.TurnWaiter;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("rest/playergame/play")
public class RestPlayGameWithPlayerController {

    private static final Logger LOG = Logger.getLogger(RestPlayGameWithPlayerController.class);

    @Autowired
    private ConvertToRestDto convertToRestDto;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private RestUtils restUtils;

    // http://localhost:8080/rest/playergame/create?name=petya&gamename=GAME&cards=2&stars=3
    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<CurrentGameRestInfoDto> makeTurn(@RequestParam("playerId") Integer playerId,
                                                           @RequestParam("gameId") Integer gameId,
                                                           @RequestParam("chosenCard") Card chosenCard) {

        if (restUtils.isGameFinished(gameService.getAllGameInfos(), gameId)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        for (Player player : gameService.getGameInfo(gameId).getPlayers()) {
            if (player.getId().equals(playerId)) {
                if (player.getDeck().getCardTypeCount(chosenCard) == 0) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            }
        }

        gameService.makeTurn(gameId, playerId, chosenCard);

        Long timeout = TimeUnit.MILLISECONDS.convert(
                systemConfigurationService.getSystemConfiguration().getRoundTimeout(), TimeUnit.SECONDS);
        Integer player1 = null;
        Integer player2 = null;
        for (Player player : gameService.getGameInfo(gameId).getPlayers()) {
            if (player1 == null) {
                player1 = player.getId();
            } else {
                player2 = player.getId();
            }
        }
        Thread turnWaiter = new Thread(new TurnWaiter(player1, player2, userService));
        turnWaiter.start();
        try {
            turnWaiter.join(timeout);
        } catch (InterruptedException e) {
            LOG.error("Failed to join secondPlayerWaiter thread. " + e.getMessage());
        }
        if (turnWaiter.isAlive()) {
            turnWaiter.interrupt();
            gameService.clearGameInfo(gameId);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        GameHistory gameHistory = gameService.getGameHistory(gameId);
        CurrentGameRestInfoDto currentGameRestInfoDto = convertToRestDto.currentGameInfoToDto(playerId, gameId, chosenCard, gameHistory);

        synchronized (this) {
            if (gameHistory.getGameStatus().equals(Game.State.GAME_FINISHED)) {
                Integer numberOfPlayers = gameService.getGameInfo(gameId).getNumberOfPlayers();
                gameService.getGameInfo(gameId).setNumberOfPlayers(--numberOfPlayers);
                if (gameService.getGameInfo(gameId).getNumberOfPlayers() == 0) {
                    gameService.finishGame(gameId);
                }
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        return new ResponseEntity<CurrentGameRestInfoDto>(currentGameRestInfoDto, headers, HttpStatus.OK);
    }
}
