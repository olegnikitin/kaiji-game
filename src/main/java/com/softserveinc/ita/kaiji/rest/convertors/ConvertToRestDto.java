package com.softserveinc.ita.kaiji.rest.convertors;

import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.Deck;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConvertToRestDto {


    @Autowired
    GameService gameService;

    public UserRestDto userToDto(User user) {

        UserRestDto userRestDto = new UserRestDto();
        userRestDto.setId(user.getId());
        userRestDto.setName(user.getName());
        userRestDto.setNickname(user.getNickname());
        userRestDto.setEmail(user.getEmail());
        userRestDto.setRoles(user.getRoles());
        String registrationDate = user.getRegistrationDate().toString();
        userRestDto.setRegistrationDate(registrationDate.substring(0, registrationDate.length() - 2));

        return userRestDto;
    }

    public GameInfoRestDto gameInfoToDto(GameInfoEntity gameInfo) {
        GameInfoRestDto gameInfoRestDto = new GameInfoRestDto();
        gameInfoRestDto.setId(gameInfo.getId());
        gameInfoRestDto.setGameName(gameInfo.getGameName());
        gameInfoRestDto.setGameType(gameInfo.getGameType());
        gameInfoRestDto.setNumberOfCards(gameInfo.getNumberOfCards());
        for (User user : gameInfo.getUsers()) {
            gameInfoRestDto.getUsers().add(user.getNickname());
        }
        String date = gameInfo.getGameStartTime().toString();
        gameInfoRestDto.setGameStartTime(date.substring(0, date.length() - 2));
        date = gameInfo.getGameFinishTime().toString();
        gameInfoRestDto.setGameFinishTime(date.substring(0, date.length() - 2));
        return gameInfoRestDto;
    }

    public GameHistoryRestDto gameHistoryToDto(GameHistoryEntity gameHistory) {
        GameHistoryRestDto gameHistoryRest = new GameHistoryRestDto();
        gameHistoryRest.setId(gameHistory.getId());
        gameHistoryRest.setGameState(gameHistory.getGameState());

        for(RoundResultEntity roundResult : gameHistory.getRoundResults()) {
            gameHistoryRest.getRoundResults().add(roundResultToDto(roundResult));
        }
        for (User winner : gameHistory.getWinners()) {
            gameHistoryRest.getWinners().add(winner.getNickname());
        }
        gameHistoryRest.setGameInfoRest(gameInfoToDto(gameHistory.getGameInfo()));
        return gameHistoryRest;
    }

    public RoundResultRestDto roundResultToDto(RoundResultEntity roundResult) {
        RoundResultRestDto roundResultRestDto = new RoundResultRestDto();
        RoundResultEntryDto roundEntry;
        roundResultRestDto.setId(roundResult.getId());
        for (Map.Entry<User, RoundResultEntity.Entry> entry : roundResult.getRound().entrySet()) {
            roundEntry = new RoundResultEntryDto(entry.getKey().getName(),
                    entry.getValue().getCard(),
                    entry.getValue().getDuelResult());
            roundResultRestDto.getEntries().add(roundEntry);
        }
        return roundResultRestDto;
    }


    public CurrentGameRestInfoDto currentGameInfoToDto(Integer playerId, Integer gameId, Card chosenCard, GameHistory gameHistory){

        CurrentGameRestInfoDto currentGameRestInfoDto = new CurrentGameRestInfoDto();

        String playerName = null;
        String enemyName = null;
        String enemyCard = null;
        Deck playersDeck = null;
        String roundWinner = "DRAW";

        for (Player player : gameHistory.getGameInfo().getPlayers()) {
            if (player.getId().equals(playerId)) {
                playerName = player.getName();
                playersDeck = player.getDeck();
                currentGameRestInfoDto.setPlayerWin(player.getStatistic().getSpecificStat(Card.DuelResult.WIN));
            } else {
                enemyName = player.getName();
                enemyCard = gameHistory.getLastRoundResultFor(player).getCard(player).toString();
                currentGameRestInfoDto.setEnemyWin(player.getStatistic().getSpecificStat(Card.DuelResult.WIN));
            }
            if (gameHistory.getLastRoundResultFor(player).getDuelResult(player).equals(Card.DuelResult.WIN)) {
                roundWinner = player.getName();
            }
            currentGameRestInfoDto.setDraws(player.getStatistic().getSpecificStat(Card.DuelResult.DRAW));
        }
        currentGameRestInfoDto.setRoundWinner(roundWinner);
        currentGameRestInfoDto.setCardPaperLeft(playersDeck.getCardTypeCount(Card.PAPER));
        currentGameRestInfoDto.setCardRockLeft(playersDeck.getCardTypeCount(Card.ROCK));
        currentGameRestInfoDto.setCardScissorsLeft(playersDeck.getCardTypeCount(Card.SCISSORS));
        currentGameRestInfoDto.setPlayerName(playerName);
        currentGameRestInfoDto.setEnemyName(enemyName);
        currentGameRestInfoDto.setGameState(gameHistory.getGameStatus());
        currentGameRestInfoDto.setYourCard(chosenCard.toString());
        currentGameRestInfoDto.setEnemyChosenCard(enemyCard);
        currentGameRestInfoDto.setGameId(gameId);

        return  currentGameRestInfoDto;
    }
}
