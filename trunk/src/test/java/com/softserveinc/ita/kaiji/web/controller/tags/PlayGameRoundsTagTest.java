package com.softserveinc.ita.kaiji.web.controller.tags;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.RoundResult;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.web.tag.PlayGameRoundsTag;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.jsp.PageContext;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;


/**
 * Created by Kyryll on 23.08.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@WebAppConfiguration
public class PlayGameRoundsTagTest {
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    GameHistory gameHistory;
    @Mock
    PageContext pageContext;
    @Mock
    Player player;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    List<RoundResult> roundResults;
    @InjectMocks
    PlayGameRoundsTag playGameRoundsTag;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Ignore // problem with deep stubs
    @Test
    public void doTag() throws Exception {
        Mockito.when(roundResults.size()).thenReturn(1);
        when(gameHistory.getRoundResults().size()).thenReturn(1);
        when(gameHistory.getRoundResults()).thenReturn(roundResults);
        when(gameHistory.getRoundResults()).thenReturn(roundResults);
        whenNew(PageContext.class).withNoArguments().thenReturn(pageContext);
        when(roundResults.get(0).getDuelResult(player)).thenReturn(null);
        playGameRoundsTag.doTag();
        verify(pageContext, times(1)).setAttribute(eq("playerStatus"), anyObject());
    }
}
