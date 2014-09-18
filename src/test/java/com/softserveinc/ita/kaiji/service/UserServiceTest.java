package com.softserveinc.ita.kaiji.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.dao.UserDAO;
import com.softserveinc.ita.kaiji.dto.GameInfoDto;
import com.softserveinc.ita.kaiji.dto.game.StatisticsDTO;
import com.softserveinc.ita.kaiji.model.User;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class UserServiceTest {

    @Mock
    private GameInfoDto gameInfoDto;
    
    @Mock
    private UserDAO userRepository;
    
    @InjectMocks
    private UserServiceImpl userService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testGetUserById() {
        User user = new User("john", "john@gmail.com", "pass");
        user.setId(1);
        when(userRepository.findOne(1)).thenReturn(user);
        assertEquals(user, userService.getUserById(1));
    }
    
    @Test
    public void testFindUser() {
        User user = new User("john", "john@gmail.com", "pass");
        user.setId(1);
        when(userRepository.findByNickname("john")).thenReturn(user);
        assertEquals(user, userService.findUser("john"));
    }
    
    @Test
    public void testUpdateUser() {
        User user = new User();
        userService.updateUser(user);
        verify(userRepository).save(user);
    }
    
    @Test
    public void testSaveUser() {
        User user = new User("john", "john@gmail.com", "pass");
        user.setId(1);
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(Integer.valueOf(1), userService.saveUser(user));
    }
    
    @Test
    public void testDeleteUser() {
        User user = new User();
        userService.deleteUser(user);
        verify(userRepository).delete(user);
    }
    
    @Test
    public void testGetStatsForUser() {
        User user = new User("john", "john@gmail.com", "pass");
        user.setId(1);
        when(userRepository.findByNickname(anyString())).thenReturn(user);
        StatisticsDTO stats = new StatisticsDTO();
        stats.setWin(1L);
        when(userRepository.findStatistics(any(User.class))).thenReturn(stats);
        assertEquals(stats.getWin(), userService.getStatsForUser("john").getWin());
    }
    
}
