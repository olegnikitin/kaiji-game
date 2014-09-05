package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.model.game.GameInfoImpl;
import com.softserveinc.ita.kaiji.service.GameServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.AsyncContext;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;

/**
 * Created by Kyryll on 23.08.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class SecondPlayerCheckerTest {
    @Mock
    GameServiceImpl gameService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    GameInfoImpl gameInfo;
    @Mock
    AsyncContext asyncContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void run() {

        SecondPlayerChecker playerChecker = new SecondPlayerChecker(asyncContext, gameService,
                0, new CountDownLatch(0), 1L);
        when(gameService.getGameInfo(0)).thenReturn(gameInfo);
        when(gameService.createGame(gameInfo)).thenReturn(3);
        playerChecker.run();
        verify(asyncContext, times(1)).dispatch("/game/3/");
    }
}
