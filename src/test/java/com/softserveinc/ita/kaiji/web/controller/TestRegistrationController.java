package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.dto.UserRegistrationDto;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 24.04.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-tests/web-context.xml" })
@WebAppConfiguration
public class TestRegistrationController {

    private static final Logger LOG = Logger.getLogger(TestRegistrationController.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void getRegistrationFormTest() {

        try {
            mockMvc.perform(get("/registration"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("registration-form"))
                    .andExpect(model().attribute("userDto", hasProperty("name", nullValue())))
                    .andExpect(model().attribute("userDto", hasProperty("nickname", isEmptyOrNullString())))
                    .andExpect(model().attribute("userDto", hasProperty("email", isEmptyOrNullString())))
                    .andExpect(model().attribute("userDto", hasProperty("password", isEmptyOrNullString())));
        } catch (Exception e) {
            LOG.error("Can't perform Registration Form Test. " + e.getMessage());
        }
    }

    @Test
    public void receiveModelTest() {

        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setName("user");
        userDto.setEmail("user@gmail.com");
        userDto.setNickname("userNickname");
        userDto.setPassword("12345");

        try {
            mockMvc.perform(post("/registration", userDto))
                    .andExpect(status().isOk())
                    .andExpect(view().name("login"))
                    .andExpect(model().attribute("notification", is("You have successfully registered, now log in")));
        } catch (Exception e) {
            LOG.error("Can't perform Receive Model Test. " + e.getMessage());
        }
    }
}
