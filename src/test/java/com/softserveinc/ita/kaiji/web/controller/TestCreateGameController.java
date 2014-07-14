package com.softserveinc.ita.kaiji.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softserveinc.ita.kaiji.service.GameService;

public class TestCreateGameController {

    private MockMvc mockMvc;
    
	@Autowired
    private GameService gameService;
	
    @Before
    public void setup() {
 
    	this.mockMvc = MockMvcBuilders.standaloneSetup(new CreateGameController()).build();
    	
    }
 
    @Ignore("method under test changed!")
    @Test()
    public void testSendToForm() throws Exception {
        
        this.mockMvc.perform(get("/game/new"))
        		.andExpect(status().isOk())
        		.andExpect(view().name("create-game"))
                .andExpect(model().attributeExists("gameInfo"));
     
    }
    
}
