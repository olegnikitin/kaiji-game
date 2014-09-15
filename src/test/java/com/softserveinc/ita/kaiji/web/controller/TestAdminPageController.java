package com.softserveinc.ita.kaiji.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.dao.GameHistoryEntityDAO;
import com.softserveinc.ita.kaiji.dao.GameInfoEntityDAO;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.SystemConfiguration;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.util.email.MailSender;
import com.softserveinc.ita.kaiji.service.SystemConfigurationService;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class TestAdminPageController {

    private MockMvc mockMvc;
    
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    
    @Mock
    private UserDAO userDAO;
    
    @Mock
    private GameInfoEntityDAO gameInfoEntityDAO;
    
    @Mock
    private GameHistoryEntityDAO gameHistoryEntityDAO;
    
    @Mock
    private MailSender mailSender;
    
    @Mock
    private SystemConfigurationService systemConfigurationService;
    
    @InjectMocks
    private AdminPageController adminPageController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminPageController).build();
    }

    @Test
    public void testAdminAPI() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk()).andExpect(view().name("admin-panel"));
    }
    
    @Test
    public void testUserPage() throws Exception {
        mockMvc.perform(get("/admin/users")).andExpect(status().isOk())
                .andExpect(model().attributeExists("newUser", "usersList"))
                .andExpect(view().name("admin-user"));
    }
    
    @Test
    public void testSaveUser() throws Exception {
        when(passwordEncoder.encode(anyString())).thenReturn("pass");
        mockMvc.perform(post("/admin/users/save").param("name", "John").param("nickname", "john")
                            .param("email", "test@test.com")).andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/admin/users"));
    }
    
    @Test
    public void testGetUser() throws Exception {
        User user = new User();
        user.setId(111);
        when(userDAO.findByEmail(anyString())).thenReturn(user);
        when(userDAO.findByNickname(anyString())).thenReturn(user);
        mockMvc.perform(post("/admin/users").param("nickname", "john").param("email", "test@test.com"))
                            .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/users"));
    }
    
    @Test
    public void testRemoveUser() throws Exception {
        when(userDAO.findOne(111)).thenReturn(new User());
        mockMvc.perform(post("/admin/users/remove").param("id", "111")).andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/admin/users"));
    }
    
    @Test
    public void testGameInfoPage() throws Exception {
        when(userDAO.findAll()).thenReturn(new ArrayList<User>());
        when(gameInfoEntityDAO.findAll()).thenReturn(new ArrayList<GameInfoEntity>());
        when(systemConfigurationService.getSystemConfiguration()).thenReturn(new SystemConfiguration());
        mockMvc.perform(get("/admin/gameinfo")).andExpect(status().isOk())
                            .andExpect(model().attributeExists("usersList", "gamesList", "multiplayerGameInfo"))
                            .andExpect(view().name("admin-gameinfo"));
    }

    @Test
    public void testGetGameInfoEntity() throws Exception {
        List<GameInfoEntity> gameInfoEntityList = new ArrayList<GameInfoEntity>();
        when(gameInfoEntityDAO.findByUser(111)).thenReturn(gameInfoEntityList);
        mockMvc.perform(post("/admin/gameinfo").param("userId", "111")).andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/admin/gameinfo"));
    }
    
    @Test
    public void testRemoveGameInfoEntity() throws Exception {
        mockMvc.perform(post("/admin/gameinfo/remove").param("id", "111")).andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/admin/gameinfo"));
    }
    
    @Test
    public void testGetGameHistoryPage() throws Exception {
        mockMvc.perform(get("/admin/gamehistory")).andExpect(status().isOk())
                            .andExpect(model().attributeExists("usersList", "gameHistoryList"))
                            .andExpect(view().name("admin-gamehistory"));
    }
    
    @Test
    public void testGetGameHistoryEntity() throws Exception {
        GameHistoryEntity gameHistoryEntity = new GameHistoryEntity();
        List<GameHistoryEntity> gameHistoryEntityList = new ArrayList<GameHistoryEntity>();
        when(gameHistoryEntityDAO.findOne(111)).thenReturn(gameHistoryEntity);
        when(gameHistoryEntityDAO.findByWinner(222)).thenReturn(gameHistoryEntityList);
        mockMvc.perform(post("/admin/gamehistory").param("id", "111").param("userId", "222")).andExpect(status().is3xxRedirection())
                            .andExpect(flash().attributeExists("searchGameHistory")).andExpect(redirectedUrl("/admin/gamehistory"));
    }
    
    @Test
    public void testRemoveGameHistoryEntity() throws Exception {
        mockMvc.perform(post("/admin/gamehistory/remove").param("id", "111")).andExpect(status().is3xxRedirection())
                            .andExpect(redirectedUrl("/admin/gamehistory"));
    }
    
}