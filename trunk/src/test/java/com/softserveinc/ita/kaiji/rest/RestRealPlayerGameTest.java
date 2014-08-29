package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.TestServiceConfiguration;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class RestRealPlayerGameTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void playerMakeTurnWithWrongParameters() throws Exception {

        mockMvc.perform(get("/rest/playergame/play")
                .param("playerId", "0")
                .param("gameId","0")
                .param("chosenCard","CARD"))
                .andExpect(status().isBadRequest());
    }
}
