package com.softserveinc.ita.kaiji.rest;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class RestSystemConfigurationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private SystemConfiguration systemConfiguration;
    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        systemConfiguration = systemConfigurationService.getSystemConfiguration();
    }

    @After
    public void tearDown() {
        systemConfigurationService.saveSystemConfiguration(systemConfiguration);
    }

    @Test
    public void getSystemConfiguration() throws Exception {

        SystemConfiguration configuration = new SystemConfiguration();
        configuration.setGameName("BotGame");
        configuration.setUserName("Zoro");
        configuration.setBotType(Bot.Types.EASY);
        configuration.setNumberOfCards(3);
        configuration.setNumberOfStars(3);
        configuration.setRoundTimeout(50L);
        configuration.setGameConnectionTimeout(150L);
        systemConfigurationService.saveSystemConfiguration(configuration);

        mockMvc.perform(get("/rest/system-configuration"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.userName", is("Zoro")))
                .andExpect(jsonPath("$.gameName", is("BotGame")))
                .andExpect(jsonPath("$.numberOfCards", is(3)))
                .andExpect(jsonPath("$.numberOfStars", is(3)))
                .andExpect(jsonPath("$.botType", is("EASY")))
                .andExpect(jsonPath("$.gameConnectionTimeout", is(150)))
                .andExpect(jsonPath("$.roundTimeout", is(50)));
    }

    @Test
    public void setSystemConfiguration() throws Exception {

        mockMvc.perform(post("/rest/system-configuration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"gameName\":\"Duel\", " +
                        "\"userName\":\"Zoro\"," +
                        "\"numberOfCards\": 4," +
                        "\"numberOfStars\": 3," +
                        "\"botType\": \"EASY\"," +
                        "\"gameConnectionTimeout\": 20,"+
                        "\"roundTimeout\": 15}"))
                .andExpect(status().isOk());
    }
}
