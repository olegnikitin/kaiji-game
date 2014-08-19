package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RestUtils {

    @Autowired
    private GameService gameService;

    public boolean isGameFinished(Set<GameInfo> gameInfos, Integer gameId){

        Integer currentGameId = null;
        for (GameInfo gameInfo : gameService.getAllGameInfos()) {
            if (gameId.equals(gameInfo.getId())) {
                currentGameId = gameId;
                break;
            }
        }

        return  currentGameId == null;
    }
}
