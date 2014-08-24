package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.TestConfiguration;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class RestCreateBotGameTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeSqlScript("file:src/test/resources/insert_h2.sql", false);
    }

    @Test
    public void createBotGame() throws Exception {

        mockMvc.perform(post("/rest/botgame/create")
                .param("name", "petya")
                .param("gamename","Game")
                .param("cards","2")
                .param("bot","EASY"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.gameName", is("Game")))
                .andExpect(jsonPath("$.playerName", is("petya")))
                .andExpect(jsonPath("$.botGame", is(true)))
                .andExpect(jsonPath("$.numberOfCards", is(2)))
                .andExpect(jsonPath("$.numberOfStars", is(0)))
                .andExpect(jsonPath("$.botType", is("EASY")))
                .andExpect(jsonPath("$.gameType", is("BOT_GAME")))
                .andExpect(jsonPath("$.playerId", is(0)));
    }

    @Test
    public void createBotGameWithWrongParameters() throws Exception {

        mockMvc.perform(post("/rest/botgame/create")
                .param("name", "yuti")
                .param("gamename","Game")
                .param("cards","2")
                .param("bot","EASY_GAME"))
                .andExpect(status().isBadRequest());
    }

}
