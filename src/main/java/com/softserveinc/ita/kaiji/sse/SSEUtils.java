package com.softserveinc.ita.kaiji.sse;

import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.sse.dto.CreatedGameInfoDto;
import com.softserveinc.ita.kaiji.sse.dto.InvitePlayerDto;
import org.springframework.stereotype.Component;

/**
 * @author Konstantin Shevchuk
 * @version 1.0
 * @since 26.08.14.
 */

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
        createdGameInfoDto.setGameType(info.getGameType());

        return createdGameInfoDto;
    }

    public InvitePlayerDto ToInvitePlayerDto(Player player, Integer number) {
        return new InvitePlayerDto(number, player.getName(), player.isPlaying());
    }
}
