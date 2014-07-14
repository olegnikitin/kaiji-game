package com.softserveinc.ita.kaiji.model.player;

import com.softserveinc.ita.kaiji.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.softserveinc.ita.kaiji.model.player.Player.PlayerStatus;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;
/**
 * Tests basic real player
 * @author  Ievgen Sukhov
 * @since 22.03.14
 * @version 1.3
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
        "file:src/main/webapp/WEB-INF/spring-tests/model-context.xml"})
public class HumanPlayerTest {

    private Player testPlayer;
    @Autowired
    private PlayerFactory playerFactory;

    @Before
    public void setUp() {
        User testUser = new User("testName", "mail", "pass");
       testPlayer = new HumanPlayer(testUser, 1);

    }



    @Test
    public void stateCheckTest() {
        assertThat(testPlayer.canPlay(), is(true));
        assertThat("State and boolean not in synch", testPlayer.getState(),
                not( anyOf( is(PlayerStatus.FINISHED),is(PlayerStatus.PLAYER_BROKEN))));
    }

    @Test(expected = IllegalStateException.class)
    public void wrongStateTest() {
        testPlayer.getChosenCard();
    }

    @Test
    public void finishTest() {

        testPlayer.finish();
        assertThat("Wrong state after finish", testPlayer.getState(), is(PlayerStatus.FINISHED));
        assertFalse("Can not continue game", testPlayer.canPlay());
    }

    @Test(expected = RuntimeException.class)
    public void cantFinishTest() {
    try {
        testPlayer.commitTurn(Card.DuelResult.WIN);
    }catch (IllegalStateException ex)
    {
        testPlayer.finish();
    }

    }

    @Test
    public void inheritanceTest() {

        assertTrue("Not assignable from Player interface", Player.class.isAssignableFrom(testPlayer.getClass()));
    }

    @Test
    public void construtorTest() {

       assertNotNull("Player was not initialized", testPlayer);
    }

    @Test
    public void idTest() {
        testPlayer.setId(1);
       Integer expectedId = 1;
        assertEquals("Id must be the same", expectedId, testPlayer.getId());
    }

    @Test
    public void nameTest() {
          String expectedName = "testName";
          assertEquals("Name must be the same", expectedName, testPlayer.getName());
    }

    @Test
    public void deckTest() {

        assertNotNull("Must have deck", testPlayer.getDeck());
    }

    @Test
    public void isBotTest() {
        assertFalse("Must be not bot", testPlayer.isBot());
    }

    @Test
    public void stateTest() {
        assertEquals("Must be in correct state", PlayerStatus.TURN_READY, testPlayer.getState());
    }

    @Test
    public void cardCountTest() {

        Integer expectedCount = 3;
        assertEquals("Must be 3 cards", expectedCount, testPlayer.getCardCount());
    }

    @Test
    public void setDeckTest() {
        HumanPlayer hPlayer = (HumanPlayer) testPlayer;
        hPlayer.setDeck(3);
        Deck expectedDeck = new DeckImpl(3);

        assertThat("Must have new deck with same order", hPlayer.getDeck().getCardsCopy(),
                contains(expectedDeck.getCardsCopy().toArray()));
    }

    @Test
    public void setNameTest() {
        HumanPlayer hPlayer = (HumanPlayer) testPlayer;
        String expectedName = "testName";

        hPlayer.setName(expectedName);

        assertEquals("Must have same name", expectedName, hPlayer.getName());
    }

    @Test
    public void toStringTest() {
        String expectedString = "testName";
        assertEquals("Must return player's name", expectedString, testPlayer.toString());
    }

    @Test
    public void compareTest() {
        User testUser = new User("testName", "mail", "pass");
        Player anotherPlayer = new HumanPlayer(testUser, 2);
        testPlayer.setId(1);
        anotherPlayer.setId(2);
        assertTrue("Compare not working", testPlayer.compareTo(anotherPlayer) == -1);
    }




}
