package com.softserveinc.ita.kaiji.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.player.bot.Bot.Types;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class TestSystemConfigurationController {
    
    private MockMvc mockMvc;
    
    @Mock
    private SystemConfigurationService systemConfigurationService;
    
    @InjectMocks
    private SystemConfigurationController systemConfigurationController;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(systemConfigurationService.getSystemConfiguration()).thenReturn(new SystemConfiguration());
        mockMvc = MockMvcBuilders.standaloneSetup(systemConfigurationController).build();
    }
    
    @Test
    public void testInitModelForBinding() throws Exception {
        mockMvc.perform(get("/config")).andExpect(status().isOk()).andExpect(model().attributeExists("systemConfiguration"))
                            .andExpect(view().name("system-configuration"));
    }

    
    @Test
    public void testHandlePost() throws Exception {
        mockMvc.perform(post("/config/handler").param("botType", "EASY").param("gameConnectionTimeout", "100")
                            .param("gameName", "mygame").param("multiplayerGameDuration", "100")
                            .param("numberOfCards", "3").param("numberOfPlayers", "2")
                            .param("numberOfStars", "3").param("roundTimeout", "100")
                            .param("userName", "john").param("action", "refresh"))
                            .andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/config/"));
    }
    
}
