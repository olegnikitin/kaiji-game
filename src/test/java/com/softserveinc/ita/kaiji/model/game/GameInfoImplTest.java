package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.PlayerFactory;
import com.softserveinc.ita.kaiji.model.player.bot.Bot.Types;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class GameInfoImplTest {

    @Autowired
    private PlayerFactory playerFactory;

    private static String gameName;
    private static String ownerName;
    private static Integer numberOfCards;
    private static Integer numberOfStars;
    private static Types botType;
    private static Set<Player> players = new HashSet<Player>();


    @Before
    public void setUp() throws Exception {
        gameName = "game";
        ownerName = "owner";
        numberOfCards = 3;
        numberOfStars = 4;
        //isBotGame = true;
        botType = Types.EASY;
        User testUser1 = new User("player1", "mail", "pass");
        players.add(new HumanPlayer(testUser1, numberOfCards, numberOfStars));
        Player bot = playerFactory.makePlayer(botType, numberOfCards);

        players.add(bot);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createWithParamsTest() {
        GameInfoImpl gameInfo = new GameInfoImpl(
                gameName
                , ownerName
                , numberOfCards
                , numberOfStars
                , botType
                , players
                , 2);
        assertNotNull(gameInfo);
    }

}
