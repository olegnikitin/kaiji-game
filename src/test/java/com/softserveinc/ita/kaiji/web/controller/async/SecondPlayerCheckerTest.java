package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.model.game.GameInfoImpl;
import com.softserveinc.ita.kaiji.service.GameServiceImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
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
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class SecondPlayerCheckerTest {
    @Mock
    GameServiceImpl gameService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    GameInfoImpl gameInfo;
    @Mock
    AsyncContext asyncContext;
    @InjectMocks
    SecondPlayerChecker secondPlayerChecker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void run() {
        when(gameService.getGameInfo(null)).thenReturn(gameInfo);
        when(gameInfo.getPlayers().size()).thenReturn(2);
        when(gameService.createGame(gameInfo)).thenReturn(3);
        secondPlayerChecker.run();
        verify(gameService,times(1)).getGameInfo(null);
        verify(gameService,times(1)).createGame(gameInfo);
        verify(asyncContext,times(1)).dispatch("/game/3/");

    }
}
