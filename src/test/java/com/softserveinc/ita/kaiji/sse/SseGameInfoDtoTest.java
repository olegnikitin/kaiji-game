package com.softserveinc.ita.kaiji.sse;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.sse.dto.CreatedGameInfoDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class SseGameInfoDtoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private SSEUtils sseUtils;

    @Autowired
    private GameService gameService;


    @Before
    public void setUp() {
        executeSqlScript("file:src/test/resources/insert_h2.sql", false);
    }

    @Test
    public void convertGameInfoToDto()
    {

        GameInfoDto gameInfoDto = new GameInfoDto();
        gameInfoDto.setGameName("Game");
        gameInfoDto.setPlayerName("petya");
        gameInfoDto.setNumberOfCards(5);
        gameInfoDto.setNumberOfStars(3);
        gameInfoDto.setGameType(Game.Type.BOT_GAME);
        gameInfoDto.setPlayerId(0);
        gameInfoDto.setBotType(Bot.Types.EASY);

        Integer gameId = gameService.setGameInfo(gameInfoDto);

        CreatedGameInfoDto createdGameInfoDtoExpected = new CreatedGameInfoDto();
        createdGameInfoDtoExpected.setNumberOfCards(5);
        createdGameInfoDtoExpected.setNumber(1);
        createdGameInfoDtoExpected.setPlayers(new HashSet<String>(){{add("player");}});
        createdGameInfoDtoExpected.setGameName("Game");
        createdGameInfoDtoExpected.setId(gameId);

        CreatedGameInfoDto createdGameInfoDto = sseUtils.ToGameInfoDto(gameService.getGameInfo(gameId), 1);

        assertEquals(createdGameInfoDtoExpected,createdGameInfoDto);

        gameService.clearGameInfo(gameId);
    }

}
