package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.exception.game.NoSuchPlayerInRoundException;
import com.softserveinc.ita.kaiji.exception.game.PlayerNotMadeTurnException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.player.HumanPlayer;
import com.softserveinc.ita.kaiji.model.player.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Paziy Evgeniy
 * @version 1.0
 * @since 23.03.14
 */
public class RoundResultTest {

    private Player first = null;
    private Player second = null;

    @Before
    public void setup() {
        User testUser1 = new User("player1", "mail", "pass");
        first = new HumanPlayer(testUser1, 1);
        User testUser2 = new User("player2", "mail", "pass");
        second = new HumanPlayer(testUser2, 1);
    }

    @After
    public void cleanup() {
        first = null;
        second = null;
    }

    @Test(expected = NullPointerException.class)
    public void mustThrowNullPointerException() {
        Player nullPlayer = null;

        new RoundResult(nullPlayer, nullPlayer);
    }

    @Test(expected = PlayerNotMadeTurnException.class)
    public void mustThrowPlayerNotMadeTurnExceptionIfPlayerDidNotMakeTurn() {
        first.makeTurn(Card.PAPER);

        new RoundResult(first, second);
    }

    @Test(expected = NoSuchPlayerInRoundException.class)
    public void mustThrowNoSuchPlayerInRoundIfTryingToGetInfoAboutWrongPlayerOrNull() {
        first.makeTurn(Card.PAPER);
        second.makeTurn(Card.PAPER);
        RoundResult rr = new RoundResult(first, second);

        rr.getCard(null);
    }

    @Test
    public void mustReturnCorrectCardForGetCard() {
        Card firstPlayerExpectedCard = Card.PAPER;
        Card secondPlayerExpectedCard = Card.ROCK;
        first.makeTurn(firstPlayerExpectedCard);
        second.makeTurn(secondPlayerExpectedCard);
        RoundResult rr = new RoundResult(first, second);

        Card firstPlayerActualCard = rr.getCard(first);
        Card secondPlayerActualCard = rr.getCard(second);

        assertEquals("Wrong card for first player", firstPlayerExpectedCard, firstPlayerActualCard);
        assertEquals("Wrong card for second player", secondPlayerExpectedCard, secondPlayerActualCard);
    }

    @Test
    public void mustReturnCorrectDuelResultForGetDuelResult() {
        Card firstPlayerCard = Card.PAPER;
        Card secondPlayerCard = Card.ROCK;
        first.makeTurn(firstPlayerCard);
        second.makeTurn(secondPlayerCard);
        RoundResult rr = new RoundResult(first, second);
        Card.DuelResult forFirstPlayerExpectedResult = Card.DuelResult.WIN;
        Card.DuelResult forSecondPlayerExpectedResult = Card.DuelResult.LOSE;

        Card.DuelResult forFirstPlayerActualResult = rr.getDuelResult(first);
        Card.DuelResult forSecondPlayerActualResult = rr.getDuelResult(second);

        assertEquals("Wrong duel result for first player", forFirstPlayerExpectedResult, forFirstPlayerActualResult);
        assertEquals("Wrong duel result for second player", forSecondPlayerExpectedResult, forSecondPlayerActualResult);
    }
}
