package com.softserveinc.ita.kaiji.ajax;


import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class SearchCreatedGamesTest extends AbstractTransactionalJUnit4SpringContextTests {

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
    public void findCreatedGamesByFirstLetters() throws Exception{

        GameInfoDto gameInfoDto = new GameInfoDto();
        gameInfoDto.setGameName("Game");
        gameInfoDto.setPlayerName("petya");
        gameInfoDto.setNumberOfCards(5);
        gameInfoDto.setBotType(Bot.Types.EASY);
        Integer gameId = gameService.setGameInfo(gameInfoDto);

        mockMvc.perform(get("/game/createdgames")
                .param("term","G"))
                .andExpect(content().string("[\"Game\"]"));

        gameService.clearGameInfo(gameId);
    }
}
