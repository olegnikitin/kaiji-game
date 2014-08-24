package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.dto.game.GameInfoEntity;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.PlayerFactory;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.times;

/**
 * Created by Kyryll on 21.08.2014.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class)
@WebAppConfiguration
public class GameInfoEntityTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    PlayerFactory playerFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executeSqlScript("file:src/test/resources/insert_h2.sql", false);
    }

    @Test
    public void GameHistoryEntityCreation() {

        Bot testBot = (Bot) playerFactory.makePlayer(Bot.Types.EASY, 3);
        Player player = new HumanPlayer(new User(), 3, 3);
        Set<Player> players = new HashSet<>();
        players.add(player);
        players.add(testBot);
        GameInfo gameInfo = Mockito.mock(GameInfo.class);
        Mockito.when(gameInfo.getNumberOfCards()).thenReturn(100);
        Mockito.when(gameInfo.getPlayers()).thenReturn(players);
        Mockito.when(gameInfo.getGameType()).thenReturn(Game.Type.BOT_GAME);
        GameInfoEntity gameInfoEntity = new GameInfoEntity(gameInfo);
        Mockito.verify(gameInfo, times(1)).getNumberOfCards();
        Mockito.verify(gameInfo, times(1)).getGameType();
        Mockito.verify(gameInfo, times(1)).getGameName();

    }
}
