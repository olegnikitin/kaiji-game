package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.PlayerFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kbardtc on 8/21/2014.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class ModifiableGameHistoryImplTest {
    @Autowired
    PlayerFactory playerFactory;
    @Mock
    MultiPlayerWinnerStrategyImpl winnerStrategy;
    @Mock
    GameInfo gameInfo;
    @InjectMocks
    ModifiableGameHistoryImpl modifiableGameHistory;

    Set<Player> players = new HashSet<>();
    int winnersQuantity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    public ModifiableGameHistoryImplTest(Object[] size, Object[] humanPlayers) {
        winnersQuantity = (Integer) size[0];
        for (Object humanPlayer : humanPlayers) {
            players.add((HumanPlayer) humanPlayer);
        }
    }

    @After
    public void refresh() {
        players.clear();
    }


    @Parameterized.Parameters
    public static Collection<Object[][]> data() {
        Object[][][] data = new Object[][][]{
                {{1}, {new HumanPlayer(new User(), 3, 0), new HumanPlayer(new User(), 3, 3)}},
                {{2}, {new HumanPlayer(new User(), 3, 3), new HumanPlayer(new User(), 3, 3)}},
                {{0}, {new HumanPlayer(new User(), 3, 0), new HumanPlayer(new User(), 3, 0)}},
                {{3}, {new HumanPlayer(new User(), 3, 3), new HumanPlayer(new User(), 3, 3),
                        new HumanPlayer(new User(), 3, 3)}}};
        return Arrays.asList(data);
    }

    @Test
    public void determineWinners() {
        Mockito.when(gameInfo.getPlayers()).thenReturn(players);
        modifiableGameHistory.determineWinners(MultiPlayerWinnerStrategyImpl.getInstance());
        Assert.assertTrue(modifiableGameHistory.getWinners().size() == winnersQuantity);
    }
}
