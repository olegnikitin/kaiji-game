package com.softserveinc.ita.kaiji.rest;

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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/application-context-test.xml"})
@WebAppConfiguration
public class RestPlayBotGameTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GameService gameService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeSqlScript("file:src/test/resources/insert_h2.sql", false);
    }

    @Test
    public void makeTurnBotGame() throws Exception {

        mockMvc.perform(post("/rest/botgame/create?name=petya&gamename=MyGame&cards=2&bot=EASY"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rest/botgame/play/" + gameService.getGameId("MyGame") + "/PAPER"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.playerName", is("petya")))
                .andExpect(jsonPath("$.gameName", is("MyGame")))
                .andExpect(jsonPath("$.gameState", is("GAME_PLAYING")))
                .andExpect(jsonPath("$.cardPaperLeft", is(1)))
                .andExpect(jsonPath("$.cardScissorsLeft", is(2)))
                .andExpect(jsonPath("$.cardRockLeft", is(2)))
                .andExpect(jsonPath("$.starsLeft", is(0)))
                .andExpect(jsonPath("$.enemyChosenCard", is("PAPER")))
                .andExpect(jsonPath("$.yourCard", is("PAPER")))
                .andExpect(jsonPath("$.playerWin", is(0)))
                .andExpect(jsonPath("$.enemyWin", is(0)))
                .andExpect(jsonPath("$.draws", is(1)))
                .andExpect(jsonPath("$.roundWinner", is("DRAW")));
    }
}
