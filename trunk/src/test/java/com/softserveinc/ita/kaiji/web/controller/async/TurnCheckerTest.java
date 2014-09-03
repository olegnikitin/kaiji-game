package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.service.UserService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.AsyncContext;

import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Kyryll on 23.08.2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TurnChecker.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class TurnCheckerTest {
    @Mock
    UserService userService;
    @Mock
    AsyncContext context;
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    Player enemy;
    @Mock
    Player player;
    @InjectMocks
    TurnChecker turnChecker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Ignore
    @Test
    public void run() throws Exception {
        whenNew(TurnChecker.class).withArguments(context,null,null,null).thenReturn(turnChecker);
        when(userService.getPlayerById(null)).thenReturn(player);
        when(enemy.isBot()).thenReturn(false);
        when(player.getCardCount()).thenReturn(3);
        when(enemy.getCardCount()).thenReturn(3);
        turnChecker.run();
        verify(enemy,times(1)).isBot();
        verify(enemy,times(1)).getCardCount();
        verify(context,times(1)).dispatch(anyString());

    }
}
