package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.TestServiceConfiguration;
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
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Kyryll on 23.08.2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TurnChecker.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class TurnCheckerTest {

    @Mock
    AsyncContext context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void run() throws Exception {

        TurnChecker turnChecker = new TurnChecker(context, 0, new CountDownLatch(0), 1L,"/game/");
        turnChecker.run();
        verify(context, times(1)).dispatch(eq("/game/0/"));
    }
}
