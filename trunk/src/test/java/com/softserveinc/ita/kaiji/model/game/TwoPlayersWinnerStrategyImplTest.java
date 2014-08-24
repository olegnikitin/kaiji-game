package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Created by Kyryll on 21.08.2014.
 */
@RunWith(Parameterized.class)
@ContextConfiguration(classes=TestConfiguration.class)
@WebAppConfiguration
public class TwoPlayersWinnerStrategyImplTest {
    int player1Vicrories;
    int player2Vicrories;

    public TwoPlayersWinnerStrategyImplTest(int player1Vicrories, int player2Vicrories) {
        this.player1Vicrories = player1Vicrories;
        this.player2Vicrories = player2Vicrories;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { 4,5 }, { 5,4 },{4,4}};
        return Arrays.asList(data);
    }

    @InjectMocks
    TwoPlayersWinnerStrategyImpl twoPlayerGame;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getWinners(){
        HumanPlayer player1=mock(HumanPlayer.class,RETURNS_DEEP_STUBS);
        HumanPlayer player2=mock(HumanPlayer.class,RETURNS_DEEP_STUBS);
        when(player1.getStatistic().getSpecificStat(Card.DuelResult.WIN)).thenReturn(player1Vicrories);
        when(player2.getStatistic().getSpecificStat(Card.DuelResult.WIN)).thenReturn(player2Vicrories);
        Set<Player> players = new HashSet<>();
        players.add(player1);
        players.add(player2);
        if (player1Vicrories>player2Vicrories)
        Assert.assertTrue(twoPlayerGame.getWinners(players).iterator().next()==player1);
        else if (player2Vicrories>player1Vicrories)
            Assert.assertTrue(twoPlayerGame.getWinners(players).iterator().next()==player2);
        else Assert.assertTrue(twoPlayerGame.getWinners(players).isEmpty());
    }
}
