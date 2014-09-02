package com.softserveinc.ita.kaiji.web.controller.async;

import com.softserveinc.ita.kaiji.TestConfiguration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import java.io.IOException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Created by Kyryll on 23.08.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class TimeoutListenerTest {
    @Mock (answer = Answers.RETURNS_DEEP_STUBS)
    AsyncEvent event;
    @Mock
    AsyncContext context;
    @InjectMocks
    TimeoutListener timeoutListener;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    @Ignore
    @Test
    public void run() throws IOException {
        when(event.getAsyncContext().getRequest()).thenReturn(new MockHttpServletRequest());
        timeoutListener.onTimeout(event);
        Mockito.verify(event.getAsyncContext(), times(2));
    }
}
