package com.softserveinc.ita.kaiji.sse;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.service.GameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class SseReceivedCreatedGames extends AbstractTransactionalJUnit4SpringContextTests {

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeSqlScript("file:src/test/resources/insert_h2.sql", false);
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GameService gameService;

    private MockMvc mockMvc;

    @Test
    public void sentAllCreatedGames() throws Exception {

        GameInfoDto gameInfoDto = new GameInfoDto();
        gameInfoDto.setGameName("MyTestGame");
        gameInfoDto.setPlayerName("petya");
        gameInfoDto.setNumberOfCards(5);
        gameInfoDto.setPlayerId(0);
        gameInfoDto.setGameType(Game.Type.BOT_GAME);
        gameInfoDto.setBotType(Bot.Types.EASY);
        Integer gameId = gameService.setGameInfo(gameInfoDto);

        mockMvc.perform(get("/joingame/update"))
                .andExpect(content().string(containsString("\"id\":" + gameId)))
                .andExpect(content().string(containsString("\"numberOfCards\":5,\"gameName\":\"MyTestGame\"")));

        gameService.clearGameInfo(gameId);
    }
}
