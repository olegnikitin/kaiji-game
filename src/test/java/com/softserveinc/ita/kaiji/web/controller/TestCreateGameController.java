package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestCreateGameController {

    private MockMvc mockMvc;

    @InjectMocks
    private CreateGameController gameController;

    @Mock
    private SystemConfigurationService systemConfigurationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    	this.mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    	
    }

    @Test()
    public void testSendToForm() throws Exception {

        Mockito.when(systemConfigurationService.getSystemConfiguration()).thenReturn(new SystemConfiguration());

        this.mockMvc.perform(get("/game/new"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("create-game"))
                .andExpect(model().attributeExists("gameInfo"));
     
    }
    
}
