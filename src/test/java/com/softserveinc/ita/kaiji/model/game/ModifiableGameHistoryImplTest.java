package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.PlayerFactory;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
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
import java.util.Set;

import static org.mockito.Mockito.times;

/**
 * Created by kbardtc on 8/21/2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= TestServiceConfiguration.class)
@WebAppConfiguration
public class ModifiableGameHistoryImplTest {
    @Autowired
    PlayerFactory playerFactory;
    @Mock
    TwoPlayersWinnerStrategyImpl winnerStrategy;
    @Mock
    GameInfo gameInfo;
    @InjectMocks
    ModifiableGameHistoryImpl modifiableGameHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void determineWinners() {
        Bot testBot = (Bot) playerFactory.makePlayer(Bot.Types.EASY, 3);
        Player player = new HumanPlayer(new User(), 3, 3);
        Set<Player> players = new HashSet<>();
        players.add(player);
        players.add(testBot);
        Mockito.when(gameInfo.getPlayers()).thenReturn(players);
        modifiableGameHistory.determineWinners();
        Mockito.verify(winnerStrategy,times(1)).getWinners(players);
    }
}
