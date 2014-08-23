package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.service.GameServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.AsyncContext;

import static org.mockito.Mockito.*;

/**
 * Created by Kyryll on 23.08.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/application-context-test.xml"})
@WebAppConfiguration
public class GameCheckerTest {
    @Mock
    GameServiceImpl gameService;
    @Mock
    AsyncContext asyncContext;
    @InjectMocks
    GameChecker gameChecker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void run() {
        when(gameService.getGameId(null)).thenReturn(1);
        gameChecker.run();
        verify(gameService,times(1)).getGameId(null);
        verify(asyncContext,times(1)).dispatch("/game/1/");

    }

}
