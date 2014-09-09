package com.softserveinc.ita.kaiji.sse;

import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.sse.dto.CreatedGameInfoDto;
import org.springframework.stereotype.Component;

@Component
public class SSEUtils {

    public CreatedGameInfoDto ToGameInfoDto(GameInfo info, Integer number) {

        CreatedGameInfoDto createdGameInfoDto = new CreatedGameInfoDto();
        createdGameInfoDto.setId(info.getId());
        createdGameInfoDto.setGameName(info.getGameName());
        for (Player player : info.getPlayers()) {
            createdGameInfoDto.getPlayers().add(player.getName());
        }
        createdGameInfoDto.setNumber(number);
        createdGameInfoDto.setNumberOfCards(info.getNumberOfCards());
        createdGameInfoDto.setNumberOfStars(info.getNumberOfStars());
        createdGameInfoDto.setNumberOfPlayers(info.getNumberOfPlayers());

        return createdGameInfoDto;
    }
}
