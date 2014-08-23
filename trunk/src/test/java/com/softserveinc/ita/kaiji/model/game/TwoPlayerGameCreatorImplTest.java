package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.game.creator.TwoPlayerGameCreatorImpl;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.PlayerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;

import static org.mockito.Mockito.*;

/**
 * Created by Kyryll on 22.08.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/application-context-test.xml"})
@WebAppConfiguration
public class TwoPlayerGameCreatorImplTest {
    @Autowired
    PlayerFactory playerFactory;
    @Mock
    GameInfoImpl gameInfo;
    @Mock
    StateRoundImpl currentRound;
    @Mock
    GameFiniteStateMachine state;
    @InjectMocks
    TwoPlayerGameCreatorImpl twoPlayerGameCreator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isValid() {
        gameInfo = mock(GameInfoImpl.class, RETURNS_DEEP_STUBS);
        Mockito.when(gameInfo.getGameType()).thenReturn(Game.Type.TWO_PLAYER_GAME);
        Mockito.when(gameInfo.getPlayers().size()).thenReturn(2);
        Mockito.when(gameInfo.getPlayers()).thenReturn(new HashSet<Player>());
        twoPlayerGameCreator.isValid(gameInfo);
        verify(gameInfo, times(1)).getGameType();
        verify(gameInfo, times(2)).getPlayers();      // check why 2 times,strange..
    }

}
