package com.softserveinc.ita.kaiji.web.controller;

import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.PlayerFactory;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/application-context-test.xml"})
@WebAppConfiguration
public class TestCreateGameController {

    private MockMvc mockMvc;

    @InjectMocks
    private CreateGameController gameController = new CreateGameController();
    @Autowired
    PlayerFactory playerFactory;

    @Mock
    private SystemConfigurationService systemConfigurationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();

    }

    @Test
    public void createGame() throws Exception {

        Mockito.when(systemConfigurationService.getSystemConfiguration()).thenReturn(new SystemConfiguration());

        this.mockMvc.perform(get("/game/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-game"))
                .andExpect(model().attributeExists("gameInfo"));
    }

    @Test
    public void createGameWithInvalidData() throws Exception {

        Mockito.when(systemConfigurationService.getSystemConfiguration()).thenReturn(new SystemConfiguration());
        Bot testBot = (Bot) playerFactory.makePlayer(Bot.Types.EASY, 3);
        Player player = new HumanPlayer(new User(), 3, 3);
        Set<Player> players = new HashSet<>();
        players.add(player);
        players.add(testBot);
        GameInfoDto gameInfo = new GameInfoDto();
        gameInfo.setPlayerName("ownerName");
        gameInfo.setNumberOfCards(Integer.valueOf("4"));
        // else must enter owner name and bot type to make gameInfo valid
        this.mockMvc.perform(post("/game/new")
                .sessionAttr("gameInfo", gameInfo))
                .andExpect(view().name("create-game"))
                .andExpect(model().hasErrors());
    }


}
