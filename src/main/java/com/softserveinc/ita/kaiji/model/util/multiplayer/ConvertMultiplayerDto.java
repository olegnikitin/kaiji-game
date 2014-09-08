package com.softserveinc.ita.kaiji.model.util.multiplayer;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.MultiplayerGameInfoDto;
import com.softserveinc.ita.kaiji.model.game.Game;
import org.springframework.stereotype.Component;

@Component
public class ConvertMultiplayerDto {

    public GameInfoDto toGameInfoDto(MultiplayerGameInfoDto multiplayerGameInfoDto){

        GameInfoDto gameInfoDto = new GameInfoDto();
        gameInfoDto.setNumberOfStars(multiplayerGameInfoDto.getNumberOfStars());
        gameInfoDto.setGameName(multiplayerGameInfoDto.getGameName());
        gameInfoDto.setNumberOfCards(multiplayerGameInfoDto.getNumberOfCards());
        gameInfoDto.setGameType(Game.Type.KAIJI_GAME);
        gameInfoDto.setNumberOfPlayers(multiplayerGameInfoDto.getNumberOfPlayers());

        return gameInfoDto;
    }
}
