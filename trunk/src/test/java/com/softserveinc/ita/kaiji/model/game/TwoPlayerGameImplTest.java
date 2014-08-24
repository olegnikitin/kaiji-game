package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.model.Card;
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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


/**
 * Created by Kyryll on 21.08.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
@WebAppConfiguration
public class TwoPlayerGameImplTest {
    @Autowired
    PlayerFactory playerFactory;
    @Mock
    GameInfoImpl gameInfo;
    @Mock
    StateRoundImpl currentRound;
    @Mock
    GameFiniteStateMachine state;
    @InjectMocks
    TwoPlayerGameImpl twoPlayerGame;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void canContinueGame() throws Exception {
        Bot testBot = (Bot) playerFactory.makePlayer(Bot.Types.EASY, 3);
        Player player = new HumanPlayer(new User("a", "a", "a"), 3, 3);
        Set<Player> players = new HashSet<>();
        players.add(player);
        players.add(testBot);
        when(gameInfo.getPlayers()).thenReturn(players);
        // PowerMock.expectNew(GameInfoImpl.class).andReturn(gameInfo);
        twoPlayerGame.makeTurn(Card.PAPER, player);
        Mockito.verify(gameInfo, atLeastOnce()).getPlayers();
    }

    @Test
    public void interruptGame(){
        twoPlayerGame.interruptGame();
        Mockito.verify(gameInfo, times(1)).setGameFinishTime((Date) any());
    }
}
