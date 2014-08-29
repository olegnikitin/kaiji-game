package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.dto.game.GameHistoryEntity;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.dto.game.RoundResultEntity;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.PlayerFactory;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kyryll on 20.08.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestServiceConfiguration.class)
@WebAppConfiguration
public class GameHistoryEntityTest {
    @Autowired
    PlayerFactory playerFactory;
    @Mock
    private GameInfoEntity gameInfoEntity;
    @Mock
    private Set<RoundResultEntity> roundResults;
    @Mock
    private Set<User> winners;
    @Mock
    GameHistory gameHistory;
    @InjectMocks
    GameHistoryEntity gameHistoryEntity;


    @Before
    public void setUp() {
        Bot testBot = (Bot)playerFactory.makePlayer(Bot.Types.EASY,3);
        Player player = new HumanPlayer(new User(), 3, 3);
        Set<Player> players = new HashSet<>();
        players.add(player);
        players.add(testBot);
        gameHistory = new ModifiableGameHistoryImpl(
                new GameInfoImpl("gameName", "ownerGame", 3, 3, Bot.Types.EASY, players),
                new GameFiniteStateMachine(Game.State.GAME_STARTED, null), TwoPlayersWinnerStrategyImpl.getInstance());
    }
    @Ignore
    @Test
    public void GameHistoryEntityCreation() {
        GameHistoryEntity gameHistoryEntity = new GameHistoryEntity(gameHistory);
    }
}
