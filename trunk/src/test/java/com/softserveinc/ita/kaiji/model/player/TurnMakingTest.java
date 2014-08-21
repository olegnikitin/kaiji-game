package com.softserveinc.ita.kaiji.model.player;

import com.softserveinc.ita.kaiji.exception.MakeTurnException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.User;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 * Tests turn making logic for players
 * @author  Ievgen Sukhov
 * @since 23.03.14
 * @version 1.2
 */
@RunWith(JUnitParamsRunner.class)
@WebAppConfiguration
public class TurnMakingTest {

    private Player testPlayer;

    @Before
    public void setUp() {
        User testUser = new User("testName", "mail", "pass");
        testPlayer = new HumanPlayer(testUser, 1 ,1);
    }

    private static Object[] data() {
        return new Object[]{
                new Object[]{Card.ROCK, Card.ROCK},
                new Object[] {Card.PAPER, Card.PAPER},
                new Object[]{Card.SCISSORS, Card.SCISSORS}
        };
    }

    @Test
    @Parameters(method = "data")
    public void turnMakingTest(Card fInput, Card fExpected) {
        testPlayer.makeTurn(fInput);
        assertEquals("Chosen card must match", fExpected, testPlayer.getChosenCard());
        testPlayer.commitTurn(Card.DuelResult.WIN);
        assertThat("Must be in right state", testPlayer.getState(),
                is(anyOf(is (Player.PlayerStatus.TURN_READY), is(Player.PlayerStatus.FINISHED))));
    }

    @Test
    @Parameters(method = "data")
    public void turnMakingRightStateTest(Card fInput, Card fExpected) {
        testPlayer.makeTurn(fInput);
        assertEquals("Chosen card must match", fExpected, testPlayer.getChosenCard());
        testPlayer.commitTurn(Card.DuelResult.WIN);
        assertThat("Must be in right state", testPlayer.getState(),
                is(anyOf(is (Player.PlayerStatus.TURN_READY), is(Player.PlayerStatus.FINISHED))));
    }

    @Test(expected = MakeTurnException.class)
    @Parameters(method = "data")
    public void turnMakingNoCardTest(Card fInput, Card fExpected) {
        testPlayer.makeTurn(fInput);
        testPlayer.commitTurn(Card.DuelResult.WIN);
        testPlayer.makeTurn(fInput);
    }

    @Test(expected = IllegalStateException.class)
    @Parameters(method = "data")
    public void turnMakingWrongStateTest(Card fInput, Card fExpected) {
        testPlayer.makeTurn(fInput);
        testPlayer.makeTurn(fInput);
    }


    @Test(expected = RuntimeException.class)
    @Parameters(method = "data")
    public void commitTurnWrongStateTest(Card fInput, Card fExpected) {
        testPlayer.makeTurn(fInput);
        testPlayer.commitTurn(Card.DuelResult.WIN);
        testPlayer.commitTurn(Card.DuelResult.WIN);
    }

    @Test
    public void commitTurnNoCardsTest() {
      List<Card> testCards = new ArrayList<>();
        testCards.addAll(Arrays.asList(Card.ROCK, Card.PAPER, Card.SCISSORS));
       for (Card c : testCards) {
        testPlayer.makeTurn(c);
        testPlayer.commitTurn(Card.DuelResult.WIN);
       }
       assertEquals("Must be in finished state", Player.PlayerStatus.FINISHED, testPlayer.getState());
    }


    @Test
    @Parameters(method = "data")
    public void rollbackTest(Card fInput, Card fExpected) {
        testPlayer.makeTurn(fInput);
        testPlayer.rollbackTurn();

        Integer expectedSize=3;

        assertEquals("Card must return to deck", expectedSize, testPlayer.getCardCount());
       assertThat(testPlayer.getDeck().getCardsCopy(), containsInAnyOrder(Card.ROCK, Card.PAPER, Card.SCISSORS));
    }

}




