package com.softserveinc.ita.kaiji.rest;

import java.util.Set;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.softserveinc.ita.kaiji.dto.rest.RestGameInfoDto;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.Deck;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.GameService;

/**
 * @author Boiko Eduard
 * @version 1.1
 * @since 05.04.14
 */

//@RestController
public class RestPlayTrainingWithBotController {
/*    private static final Logger LOG = Logger.getLogger(RestPlayTrainingWithBotController.class);

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/rest/training/{gameId}/{chosenCard}", method = RequestMethod.GET)
    public RestGameInfoDto makeTurn(@PathVariable Integer gameId, @PathVariable Card chosenCard) {

        Integer playerId = gameService.getPlayerIdFromGame(gameId);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Game Id: " + gameId + ". Card chosen: " + chosenCard + ". Player Id: " + playerId);
        }
        gameService.makeTurn(gameId, playerId, chosenCard);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Game made turn. Creating JSON Object with round results.");
        }
        RestGameInfoDto restGameInfoDto = new RestGameInfoDto();
        GameHistory gameHistory = gameService.getGameHistory(gameId);
        Game.State gameState = gameHistory.getGameStatus();
        GameInfo gameInfo = gameHistory.getGameInfo();
        Set<Player> players = gameInfo.getPlayers();
        String playerName = null;
        String botName = null;
        String botCard = null;
        Deck playersDeck = null;
        String roundResultForPlayer = null;
        for (Player player : players) {
            if (player.getId().equals(playerId)) {
                roundResultForPlayer = gameHistory.getLastRoundResultFor(player).getDuelResult(player).toString();
                playerName = player.getName();
                playersDeck = player.getDeck();
            } else {
                botName = player.getName();
                botCard = gameHistory.getLastRoundResultFor(player).getCard(player).toString();
            }
        }
        restGameInfoDto.setRoundWinner(roundResultForPlayer);
        restGameInfoDto.setCardPaperLeft(playersDeck.getCardTypeCount(Card.PAPER));
        restGameInfoDto.setCardRockLeft(playersDeck.getCardTypeCount(Card.ROCK));
        restGameInfoDto.setCardScissorsLeft(playersDeck.getCardTypeCount(Card.SCISSORS));
        restGameInfoDto.setPlayerName(playerName);
        restGameInfoDto.setBotName(botName);
        restGameInfoDto.setGameState(gameState);
        restGameInfoDto.setCard(chosenCard.toString());
        restGameInfoDto.setBotChosenCard(botCard);
        restGameInfoDto.setGameId(gameId);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Filled restGameInfoDto with info.");
        }
        return restGameInfoDto;

    }

    @RequestMapping(value = "/rest/info/{gameName}", method = RequestMethod.GET)
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
