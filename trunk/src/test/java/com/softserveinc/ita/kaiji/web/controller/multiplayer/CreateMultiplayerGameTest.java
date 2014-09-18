package com.softserveinc.ita.kaiji.web.controller.multiplayer;

import javax.servlet.AsyncContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.Game.Type;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.GameInfoImpl;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.util.multiplayer.ConvertMultiplayerDto;
import com.softserveinc.ita.kaiji.service.GameService;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;
import com.softserveinc.ita.kaiji.sse.ServerEventsSyncro;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class CreateMultiplayerGameTest {

    private MockMvc mockMvc;
    
    @Mock
    AsyncContext asyncContext;
    
    @Mock
    Authentication auth;
    
    @Mock
    SecurityContext securityContext;
    
    @Mock
    private ConvertMultiplayerDto convertMultiplayerDto;

    @Mock
    private GameService gameService;

    @Mock
    private SystemConfigurationService systemConfigurationService;

    @Mock
    private ServerEventsSyncro serverEventsSyncro;
    
    @InjectMocks
    private CreateMultiplayerGame createMultiplayerGame;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(createMultiplayerGame).build();
    }
    
    @Test
    public void testCreateGame() throws Exception {
        when(serverEventsSyncro.getCreatedGames()).thenReturn(new Object());
        mockMvc.perform(post("/game/multiplayer/new").param("gameName", "mygame").param("numberOfPlayers", "5")
                        .param("numberOfCards", "4").param("numberOfStars", "3"))
                        .andExpect(status().is3xxRedirection()).andExpect(flash().attributeExists("notification"))
                        .andExpect(redirectedUrl("/admin/gameinfo"));
    }
    
    @Test
    public void testJoinGame() throws Exception {
        GameInfo info = new GameInfoImpl();
        info.setId(1);
        info.setDatabaseId(1);
        info.setNumberOfPlayers(5);
        info.setGameType(Type.KAIJI_GAME);
        when(gameService.getGameInfo(anyInt())).thenReturn(info);
        
        SystemConfiguration systemConfiguration = new SystemConfiguration();
        systemConfiguration.setGameConnectionTimeout(1L);
        when(systemConfigurationService.getSystemConfiguration()).thenReturn(systemConfiguration);
        
        when(auth.getName()).thenReturn("john");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        
        when(gameService.addPlayer(anyString(), anyString())).thenReturn(1);
        
        mockMvc.perform(get("/game/multiplayer/new/join").param("gameName", "mygame").param("infoId", "1"))
                        .andExpect(status().isOk());
    }
    
    @Test
    public void testJoinGameMaximumPlayers() throws Exception {
        GameInfo info = new GameInfoImpl();
        info.setId(1);
        info.setDatabaseId(1);
        info.setNumberOfPlayers(1);
        info.setGameType(Type.KAIJI_GAME);
        info.getPlayers().add(new HumanPlayer(new User("john", "john@gmail.com", "pass"), 4, 3));
        when(gameService.getGameInfo(anyInt())).thenReturn(info);
        
        SystemConfiguration systemConfiguration = new SystemConfiguration();
        systemConfiguration.setGameConnectionTimeout(1L);
        when(systemConfigurationService.getSystemConfiguration()).thenReturn(systemConfiguration);
        
        when(auth.getName()).thenReturn("nick");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        
        mockMvc.perform(get("/game/multiplayer/new/join").param("gameName", "mygame").param("infoId", "1"))
                        .andExpect(status().isOk()).andExpect(request().attribute("manyPlayers", Boolean.TRUE))
                        .andExpect(forwardedUrl("/game/multiplayer/join/1"));
    }
    
}
