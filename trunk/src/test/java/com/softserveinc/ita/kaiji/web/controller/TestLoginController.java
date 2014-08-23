package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.web.filter.AuthenticationSuccessFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by kbardtc on 8/22/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/application-context-test.xml"})
@WebAppConfiguration
public class TestLoginController extends AbstractTransactionalJUnit4SpringContextTests {
    private MockMvc mockMvc;

    @Mock
    AuthenticationSuccessFilter authenticationSuccessFilter;
    @InjectMocks
    LoginController loginController;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeSqlScript("file:src/test/resources/insert_h2.sql", false);
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void login() throws Exception {
        mockMvc.perform(post("/login")
        .param("username", "1petya")
        .param("password","1234"))
        .andExpect(view().name("start-page"));
      //  verify(authenticationSuccessFilter,times(1)).onAuthenticationSuccess(null,null,null);
    }
}
