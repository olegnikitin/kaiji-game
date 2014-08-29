package com.softserveinc.ita.kaiji.model.player;

import com.softserveinc.ita.kaiji.TestConfiguration;
import com.softserveinc.ita.kaiji.TestServiceConfiguration;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameFactory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.GameInfoImpl;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Tests hard bot strategy
 *
 * @author Ievgen Sukhov
 * @version 1.1
 * @since 23.03.14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
@WebAppConfiguration
public class HardBotTest {
    Bot testBot;
    Player testPlayer;
    Game testGame;
    GameInfo gameInfo;
    Set<Player> playersSet;
    //Reflection
    Class<? extends Bot> botClass;
    Method initMethod;
    Method executeStrategyMethod;
    Field initedField;
    Field roundNumberField;
    Field opponentField;
    Field roundResultsField;
    @Autowired
    private GameFactory gameFactory;
    @Autowired
    private PlayerFactory playerFactory;

    @Before
    public void setUp() {

        playersSet = new HashSet<>();
        User testUser = new User("testName", "mail", "pass");
        testPlayer = new HumanPlayer(testUser, 1, 1);
        testPlayer.setId(1);
        testBot = (Bot) playerFactory.makePlayer(Bot.Types.HARD, 1);
        botClass = testBot.getClass();

        playersSet.add(testPlayer);
        playersSet.add(testBot);

        gameInfo = new GameInfoImpl("Game1", "Human1", 1, 1, Bot.Types.HARD, playersSet);
        gameInfo.setGameType(Game.Type.BOT_GAME);
        testGame = gameFactory.makeGame(gameInfo);

        //Reflect on private properties


        try {
            initMethod = botClass.getDeclaredMethod("initAI");
            executeStrategyMethod = botClass.getDeclaredMethod("executeStrategy");
            initedField = botClass.getDeclaredField("inited");
            roundNumberField = botClass.getDeclaredField("roundNumber");
            opponentField = botClass.getDeclaredField("opponent");
            roundResultsField = botClass.getDeclaredField("roundResults");

        } catch (NoSuchMethodException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        initMethod.setAccessible(true);
        executeStrategyMethod.setAccessible(true);
        initedField.setAccessible(true);
        roundNumberField.setAccessible(true);
        opponentField.setAccessible(true);
        roundResultsField.setAccessible(true);

    }

    @Test(expected = NullPointerException.class)
    public void noGameHistoryError() {
        testBot.makeTurn(null);
    }

    @Test(expected = InvocationTargetException.class)
    public void initExceptionTest() throws IllegalAccessException, InvocationTargetException {

        initMethod.invoke(testBot);
    }

    @Test
    public void initTest() throws IllegalAccessException {
        testBot.setGameData(testGame.getGameHistory());

        try {
            initMethod.invoke(testBot);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        assertTrue("Must be inited", (Boolean) initedField.get(testBot));

    }

    @Test
    public void setDataTest() throws IllegalAccessException {
        testBot.setGameData(testGame.getGameHistory());
        try {
            executeStrategyMethod.invoke(testBot);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        assertNotNull("Must be inited", (Integer) roundNumberField.get(testBot));
        assertNotNull("Must be inited", (Player) opponentField.get(testBot));

    }

}
