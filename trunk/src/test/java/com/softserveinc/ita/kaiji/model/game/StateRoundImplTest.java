package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.exception.MakeTurnException;
import com.softserveinc.ita.kaiji.exception.game.CanNotFinishRoundException;
import com.softserveinc.ita.kaiji.exception.game.PlayerAlreadyMadeTurnInThisRoundException;
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
public class StateRoundImplTest {

    StateRoundImpl round = null;
    Player firstPlayer = null;
    Player secondPlayer = null;

    @Before
    public void setup() {
        round = new StateRoundImpl();

        User testUser1 = new User("player1", "mail", "pass");
        firstPlayer = new HumanPlayer(testUser1, 1 , 1);
        User testUser2 = new User("player2", "mail", "pass");
        secondPlayer = new HumanPlayer(testUser2, 1, 1);

    }

    @After
    public void cleanup() {
        round = null;
        firstPlayer = null;
        secondPlayer = null;
    }

    @Test
    public void mustBeRoundStartedStateAfterCreatingRound() {
        Round.State expectedState = Round.State.ROUND_STARTED;
        Round.State actualState = round.getState();
        assertEquals("", expectedState, actualState);
    }

    @Test
    public void roundResultMustBeNullWhenRoundInProgress() {
        RoundResult roundResult = round.getRoundResult();
        assertNull("Round result not null", roundResult);
    }

    @Test(expected = CanNotFinishRoundException.class)
    public void mustThrowCanNotFinishRoundExceptionWhenTryingFinishJustStartedRound() {
        round.finishRound();
    }

    @Test
    public void mustReturnRoundInterruptedStateWhenTryInterruptJustStartedRound() {
        Round.State expectedState = Round.State.ROUND_INTERRUPTED;
        Round.State actualState = round.interruptRound();
        assertEquals("Wrong state after interrupting round", expectedState, actualState);
    }

    @Test(expected = CanNotFinishRoundException.class)
        public void mustThrowCanNotFinishRoundExceptionWhenTryingFinishRoundAfterFirstTurn() {
        round.makeTurn(Card.ROCK, firstPlayer);
        round.finishRound();
    }

    @Test
    public void mustInterruptRoundAndRetrieveCardBackToPlayerWhenWasMadeOneTurn() {
        int expectedCardsCount = firstPlayer.getDeck().getDeckSize();
        Round.State expectedState = Round.State.ROUND_INTERRUPTED;
        round.makeTurn(Card.ROCK, firstPlayer);

        Round.State actualState = round.interruptRound();
        int actualCardsCount = firstPlayer.getDeck().getDeckSize();

        assertEquals("Wrong state after round interrupting", expectedState, actualState);
        assertEquals("Card was not retrieved back to player", expectedCardsCount, actualCardsCount);
    }

    @Test(expected = PlayerAlreadyMadeTurnInThisRoundException.class)
    public void mustThrowPlayerAlreadyMadeTurnInThisRoundExceptionWhenTryingMadeTurnTwiceForOnePlayer() {
        round.makeTurn(Card.ROCK, firstPlayer);
        round.makeTurn(Card.PAPER, firstPlayer);
    }

    @Test
    public void mustBeReadyToFinishStateWhenTwoPlayersMadeTurn() {
        Round.State expectedState = Round.State.ROUND_READY_TO_FINISH;
        round.makeTurn(Card.ROCK, firstPlayer);
        round.makeTurn(Card.PAPER, secondPlayer);

        Round.State actualState = round.getState();

        assertEquals("Wrong state after two turns", expectedState, actualState);
    }

    @Test
    public void mustBeFinishedStateWhenTryingToFinishRoundAfterTwoPlayersMadeTurn() {
        Round.State expectedState = Round.State.ROUND_FINISHED;
        round.makeTurn(Card.ROCK, firstPlayer);
        round.makeTurn(Card.PAPER, secondPlayer);
        round.finishRound();

        Round.State actualState = round.getState();
        RoundResult roundResult = round.getRoundResult();

        assertEquals("Wrong state when finishing round after two turns", expectedState, actualState);
        assertNotNull("RoundResult was not built", roundResult);
    }

    @Test
    public void mustBeFinishedStateWhenTryingToInterruptRoundAfterTwoPlayersMadeTurn() {
        Round.State expectedState = Round.State.ROUND_FINISHED;
        round.makeTurn(Card.ROCK, firstPlayer);
        round.makeTurn(Card.PAPER, secondPlayer);
        round.interruptRound();

        Round.State actualState = round.getState();
        RoundResult roundResult = round.getRoundResult();

        assertEquals("Wrong state when interrupt round after two turns", expectedState, actualState);
        assertNotNull("RoundResult was not built", roundResult);
    }

    @Test(expected = MakeTurnException.class)
    public void mustThrowMakeTurnExceptionWhenTryingToMakeThirdTurnInRound() {
        round.makeTurn(Card.ROCK, firstPlayer);
        round.makeTurn(Card.PAPER, secondPlayer);
        User testUser1 = new User("player1", "mail", "pass");
        round.makeTurn(Card.SCISSORS, new HumanPlayer(testUser1, 1, 1));
    }

}
