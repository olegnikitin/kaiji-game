package com.softserveinc.ita.kaiji.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softserveinc.ita.kaiji.TestServiceConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class TestStartPageController {
    
    private MockMvc mockMvc;
    
    @Autowired
    private StartPageController startPageController;
    
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(startPageController).build();
    }

    @Test
    public void testGetStartPage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("start-page"));
    }
    
    @Test
    public void testGetStatisticPage() throws Exception {
        mockMvc.perform(get("/statistic/user")).andExpect(status().isOk()).andExpect(view().name("statistics"));
    }
    
    @Test
    public void testGetChatPage() throws Exception {
        mockMvc.perform(get("/gamechat/john")).andExpect(status().isOk()).andExpect(model().attributeExists("messages"))
                            .andExpect(view().name("chat"));
    }
    
}
