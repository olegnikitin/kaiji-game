package com.softserveinc.ita.kaiji.model.player;

import com.softserveinc.ita.kaiji.exception.MakeTurnException;
import com.softserveinc.ita.kaiji.exception.game.NoBotPlayerInGameException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.BotGameImpl;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.game.GameInfoImpl;
import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests basic bot player
 *
 * @author Ievgen Sukhov
 * @version 2.0
 * @since 23.03.14
 */
@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/application-context-test.xml"})
public class BotTest {

    Bot testBot;
    Player player;

    @Autowired
    PlayerFactory playerFactory;
    private TestContextManager testContextManager;

    @Before
    public void setUp() throws Exception {
        this.testContextManager = new TestContextManager(getClass());
        this.testContextManager.prepareTestInstance(this);

        testBot = (Bot) playerFactory.makePlayer(Bot.Types.EASY, 1);
        player=new HumanPlayer(new User(), 3, 3);
    }

    private static Object[] data() {
        return new Object[]{
                new Object[]{Card.ROCK, Card.ROCK},
                new Object[]{Card.PAPER, Card.PAPER},
                new Object[]{Card.SCISSORS, Card.SCISSORS}
        };
    }

    @Test
    public void inheritanceTest() {

        assertTrue("Not assignable from Player interface", Player.class.isAssignableFrom(testBot.getClass()));
    }

    @Test
    public void construtorTest() {
        Player easyBot = playerFactory.makePlayer(Bot.Types.EASY, 1);
        assertNotNull("Bot was not initialized", easyBot);
        Player medBot = playerFactory.makePlayer(Bot.Types.MEDIUM, 1);
        assertNotNull("Bot was not initialized", medBot);
        Player hardBot = playerFactory.makePlayer(Bot.Types.HARD, 1);
        assertNotNull("Bot was not initialized", hardBot);

    }


    @Test
    public void isBotTest() {
        assertTrue("Must be bot", testBot.isBot());
    }

    @Test
    public void botTypeTest() {
        Bot.Types expectedType = Bot.Types.EASY;
        assertEquals("Must be same type", expectedType, testBot.getType());
    }

    @Test
    public void gameDataTest() {
        GameHistory testData = mock(GameHistory.class);
        testBot.setGameData(testData);
        assertNotNull("Must have data", testBot.getGameData());
    }


    @Test
    @Parameters(method = "data")
    public void turnMakingTestSpecifiedCard(Card fInput, Card fExpected) {
        testBot.makeTurn(fInput);
        assertEquals("Chosen card must match", fExpected, testBot.getChosenCard());
        testBot.commitTurn(Card.DuelResult.DRAW);
        assertThat("Must be in right state", testBot.getState(),
                is(anyOf(is(Player.PlayerStatus.TURN_READY), is(Player.PlayerStatus.FINISHED))));
    }

    @Test
    public void turnMakingTestforEasyBot() {
        Player easyBot = playerFactory.makePlayer(Bot.Types.EASY, 1);
        easyBot.makeTurn(null);
        assertThat("Chosen card must be ROCK for easy bot", easyBot.getChosenCard(), is(Card.PAPER));

    }

    @Test
    public void turnMakingTestforMediumBot() {
        Player medBot = playerFactory.makePlayer(Bot.Types.MEDIUM, 1);
        medBot.makeTurn(null);
        assertThat("Chosen card can be any for medium bot", medBot.getChosenCard(),
                is(anyOf(is(Card.ROCK), is(Card.PAPER), is(Card.SCISSORS))));

    }

    @Test
    public void turnMakingTestforHardBot() {
        Player hardBot = playerFactory.makePlayer(Bot.Types.HARD, 1);
        hardBot.makeTurn(Card.ROCK);
        assertThat("First chosen card can be any for hard bot", hardBot.getChosenCard(),
                is(anyOf(is(Card.ROCK), is(Card.PAPER), is(Card.SCISSORS))));

    }

    @Test(expected = MakeTurnException.class)
    @Parameters(method = "data")
    public void turnMakingNoCardTest(Card fInput, Card fExpected) {
        testBot.makeTurn(fInput);
        testBot.commitTurn(Card.DuelResult.WIN);
        testBot.makeTurn(fInput);
    }

    @Test(expected = MakeTurnException.class)
    @Parameters(method = "data")
    public void turnMakingWrongStateTest(Card fInput, Card fExpected) {
        testBot.makeTurn(fInput);
        testBot.makeTurn(fInput);
    }

    @Test(expected = NoBotPlayerInGameException.class)
    public void mustThrowExceptionThatGameIsBrokenCauseInDuelHumanvsBotThereIsNoBot() {
        //Arrange
        Set<Player> players = new HashSet<>();
        players.add(new HumanPlayer(new User(), 3, 3));
        players.add(new HumanPlayer(new User(), 3, 3));
        GameInfo gameInfo = new GameInfoImpl("test", "testOwner", 3, 3, Bot.Types.EASY, players);
        //Act //Assert
        BotGameImpl botGame = new BotGameImpl(gameInfo);
    }

    @Test
    public void makeTurn(){
        //Arrange
        Card card = Card.PAPER;
        Set<Player> players = new HashSet<>();
        players.add(player);
        players.add(testBot);
        GameInfo gameInfo = new GameInfoImpl("test", "testOwner", 3, 3, Bot.Types.EASY, players);
        //Act
        BotGameImpl botGame = new BotGameImpl(gameInfo);
        //Assert
        botGame.makeTurn(card,player);
    }

}
