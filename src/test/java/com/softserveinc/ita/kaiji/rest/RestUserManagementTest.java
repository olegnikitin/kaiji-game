package com.softserveinc.ita.kaiji.rest;

import org.hamcrest.Matchers;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/application-context-test.xml"})
@WebAppConfiguration
public class RestUserManagementTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeSqlScript("file:src/test/resources/insert_h2.sql", false);
    }

    @Test
    public void getUserByNickname() throws Exception {

        mockMvc.perform(get("/rest/management/user/vas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id", is(23)))
                .andExpect(jsonPath("$.name", is("vasya")))
                .andExpect(jsonPath("$.nickname", is("vas")))
                .andExpect(jsonPath("$.email", is("12122@1212")))
                .andExpect(jsonPath("$.roles[0]", is("USER_ROLE")))
                .andExpect(jsonPath("$.registrationDate", is("2014-07-30 17:54:18")));
    }

    @Test
    public void getAllUsers() throws Exception {

        mockMvc.perform(get("/rest/management/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id", is(15)))
                .andExpect(jsonPath("$[0].name", is("petya")))
                .andExpect(jsonPath("$[0].nickname", is("petya")))
                .andExpect(jsonPath("$[0].email", is("2@2")))
                .andExpect(jsonPath("$[0].roles", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].registrationDate", is("2014-07-18 16:00:37")))

                .andExpect(jsonPath("$[1].id", is(23)))
                .andExpect(jsonPath("$[1].name", is("vasya")))
                .andExpect(jsonPath("$[1].nickname", is("vas")))
                .andExpect(jsonPath("$[1].email", is("12122@1212")))
                .andExpect(jsonPath("$[1].roles[0]", is("USER_ROLE")))
                .andExpect(jsonPath("$[1].registrationDate", is("2014-07-30 17:54:18")))

                .andExpect(jsonPath("$[2].id", is(25)))
                .andExpect(jsonPath("$[2].name", is("kol")))
                .andExpect(jsonPath("$[2].nickname", is("kol")))
                .andExpect(jsonPath("$[2].email", is("kol@kol")))
                .andExpect(jsonPath("$[2].roles[0]", is("USER_ROLE")))
                .andExpect(jsonPath("$[2].registrationDate", is("2014-08-10 17:38:19")));
    }

    @Test
    public void addUser() throws Exception {

        mockMvc.perform(post("/rest/management/user/add")
                .param("name","sasha")
                .param("nickname","ssh")
                .param("email","1345@gmail.com")
                .param("password","$2a$10$b1zfl0hlzgLdH6G3mFgA9eJVh492pfJBAKOUys.90INubpb76VqZy"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name", is("sasha")))
                .andExpect(jsonPath("$.nickname", is("ssh")))
                .andExpect(jsonPath("$.email", is("1345@gmail.com")))
                .andExpect(jsonPath("$.roles[0]", is("USER_ROLE")));
    }

    @Test
    public void deleteUser() throws Exception {

        mockMvc.perform(delete("/rest/management/user/delete/vas"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllGameInfo() throws Exception {

        mockMvc.perform(get("/rest/management/gameinfos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id", is(16)))
                .andExpect(jsonPath("$[0].gameName", is("Duel")))
                .andExpect(jsonPath("$[0].numberOfCards", is(2)))
                .andExpect(jsonPath("$[0].gameType", is("BOT_GAME")))
                .andExpect(jsonPath("$[0].gameStartTime", is("2014-08-17 09:56:33")))
                .andExpect(jsonPath("$[0].gameFinishTime", is("2014-08-17 09:56:36")))
                .andExpect(jsonPath("$[0].users[0]", is("vas")))

                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[1].id", is(30)))
                .andExpect(jsonPath("$[1].gameName", is("Duel")))
                .andExpect(jsonPath("$[1].numberOfCards", is(2)))
                .andExpect(jsonPath("$[1].gameType", is("BOT_GAME")))
                .andExpect(jsonPath("$[1].gameStartTime", is("2014-08-20 09:19:35")))
                .andExpect(jsonPath("$[1].gameFinishTime", is("2014-08-20 09:19:40")))
                .andExpect(jsonPath("$[1].users[0]", is("kol")));
    }

    @Test
    public void getGameInfoByUserNickName() throws Exception {

        mockMvc.perform(get("/rest/management/gameinfo/vas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id", is(16)))
                .andExpect(jsonPath("$[0].gameName", is("Duel")))
                .andExpect(jsonPath("$[0].numberOfCards", is(2)))
                .andExpect(jsonPath("$[0].gameType", is("BOT_GAME")))
                .andExpect(jsonPath("$[0].gameStartTime", is("2014-08-17 09:56:33")))
                .andExpect(jsonPath("$[0].gameFinishTime", is("2014-08-17 09:56:36")))
                .andExpect(jsonPath("$[0].users[0]", is("vas")));

    }

    @Test
    public void deleteGameInfoById() throws Exception {

        mockMvc.perform(delete("/rest/management/gameinfo/delete/16"))
                .andExpect(status().isOk());

    }

    @Test
    public void getAllGameHistories() throws Exception {

        mockMvc.perform(get("/rest/management/gamehistories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id", is(16)))
                .andExpect(jsonPath("$[0].gameInfoRest.id", is(16)))
                .andExpect(jsonPath("$[0].gameInfoRest.gameName", is("Duel")))
                .andExpect(jsonPath("$[0].gameInfoRest.numberOfCards", is(2)))
                .andExpect(jsonPath("$[0].gameInfoRest.gameType", is("BOT_GAME")))
                .andExpect(jsonPath("$[0].gameInfoRest.gameStartTime", is("2014-08-17 09:56:33")))
                .andExpect(jsonPath("$[0].gameInfoRest.gameFinishTime", is("2014-08-17 09:56:36")))
                .andExpect(jsonPath("$[0].gameInfoRest.users[0]", is("vas")))
                .andExpect(jsonPath("$[0].roundResults", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].winners[0]", is("vas")))
                .andExpect(jsonPath("$[0].gameState", is("GAME_FINISHED")))

                .andExpect(jsonPath("$[1].id", is(30)))
                .andExpect(jsonPath("$[1].gameInfoRest.id", is(30)))
                .andExpect(jsonPath("$[1].gameInfoRest.gameName", is("Duel")))
                .andExpect(jsonPath("$[1].gameInfoRest.numberOfCards", is(2)))
                .andExpect(jsonPath("$[1].gameInfoRest.gameType", is("BOT_GAME")))
                .andExpect(jsonPath("$[1].gameInfoRest.gameStartTime", is("2014-08-20 09:19:35")))
                .andExpect(jsonPath("$[1].gameInfoRest.gameFinishTime", is("2014-08-20 09:19:40")))
                .andExpect(jsonPath("$[1].gameInfoRest.users[0]", is("kol")))
                .andExpect(jsonPath("$[1].roundResults", Matchers.hasSize(5)))
                .andExpect(jsonPath("$[1].winners", Matchers.hasSize(0)))
                .andExpect(jsonPath("$[1].gameState", is("GAME_FINISHED")));

    }

    @Test
    public void getGameHistoriesByNickname() throws Exception {

        mockMvc.perform(get("/rest/management/gamehistory/vas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id", is(16)))
                .andExpect(jsonPath("$[0].gameInfoRest.id", is(16)))
                .andExpect(jsonPath("$[0].gameInfoRest.gameName", is("Duel")))
                .andExpect(jsonPath("$[0].gameInfoRest.numberOfCards", is(2)))
                .andExpect(jsonPath("$[0].gameInfoRest.gameType", is("BOT_GAME")))
                .andExpect(jsonPath("$[0].gameInfoRest.gameStartTime", is("2014-08-17 09:56:33")))
                .andExpect(jsonPath("$[0].gameInfoRest.gameFinishTime", is("2014-08-17 09:56:36")))
                .andExpect(jsonPath("$[0].gameInfoRest.users[0]", is("vas")))
                .andExpect(jsonPath("$[0].roundResults", Matchers.hasSize(4)))
                .andExpect(jsonPath("$[0].winners[0]", is("vas")))
                .andExpect(jsonPath("$[0].gameState", is("GAME_FINISHED")));
    }

    @Test
    public void deleteGameHistoryById() throws Exception {

        mockMvc.perform(delete("/rest/management/gamehistory/delete/16"))
                .andExpect(status().isOk());

    }
}
