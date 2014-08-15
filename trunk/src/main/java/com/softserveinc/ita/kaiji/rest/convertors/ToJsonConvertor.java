package com.softserveinc.ita.kaiji.rest.convertors;

import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.RoundResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ToJsonConvertor {

    public UserJson UserToJson(User user) {

        UserJson userJson = new UserJson();
        userJson.setId(user.getId());
        userJson.setName(user.getName());
        userJson.setNickname(user.getNickname());
        userJson.setEmail(user.getEmail());
        userJson.setRoles(user.getRoles());
        String registrationDate = user.getRegistrationDate().toString();
        userJson.setRegistrationDate(registrationDate.substring(0, registrationDate.length() - 2));

        return userJson;
    }

    public GameInfoJson GameInfoToJson(GameInfoEntity gameInfo) {
        GameInfoJson gameInfoJson = new GameInfoJson();
        gameInfoJson.setId(gameInfo.getId());
        gameInfoJson.setGameName(gameInfo.getGameName());
        gameInfoJson.setGameType(gameInfo.getGameType());
        gameInfoJson.setNumberOfCards(gameInfo.getNumberOfCards());
        for (User user : gameInfo.getUsers()) {
            gameInfoJson.getUsers().add(user.getNickname());
        }
        String date = gameInfo.getGameStartTime().toString();
        gameInfoJson.setGameStartTime(date.substring(0, date.length() - 2));
        date = gameInfo.getGameFinishTime().toString();
        gameInfoJson.setGameFinishTime(date.substring(0, date.length() - 2));
        return gameInfoJson;
    }

    public GameHistoryJson GameHistoryToJson(GameHistoryEntity gameHistory) {
        GameHistoryJson gameHistoryJson = new GameHistoryJson();
        gameHistoryJson.setId(gameHistory.getId());
        gameHistoryJson.setGameState(gameHistory.getGameState());

        for(RoundResultEntity roundResult : gameHistory.getRoundResults()) {
            gameHistoryJson.getRoundResults().add(RoundResultToJson(roundResult));
        }
        for (User winner : gameHistory.getWinners()) {
            gameHistoryJson.getWinners().add(winner.getNickname());
        }
        gameHistoryJson.setGameInfoJson(GameInfoToJson(gameHistory.getGameInfo()));
        return gameHistoryJson;
    }

    public RoundResultJson RoundResultToJson(RoundResultEntity roundResult) {
        RoundResultJson roundResultJson = new RoundResultJson();
        RoundResultEntry roundEntry;
        roundResultJson.setId(roundResult.getId());
        for (Map.Entry<User, RoundResultEntity.Entry> entry : roundResult.getRound().entrySet()) {
            roundEntry = new RoundResultEntry(entry.getKey().getName(),
                    entry.getValue().getCard(),
                    entry.getValue().getDuelResult());
            roundResultJson.getEntries().add(roundEntry);
        }
        return roundResultJson;
    }
}
