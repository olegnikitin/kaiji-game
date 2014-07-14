package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.Identifiable;
import com.softserveinc.ita.kaiji.model.util.Statable;

/**
 * Round API
 * @see com.softserveinc.ita.kaiji.model.util.Identifiable
 * @see com.softserveinc.ita.kaiji.model.util.Statable
 * @author Paziy Evgeniy
 * @version 2.7
 * @since 20.03.14
 */
interface Round extends Identifiable, Statable<Round.State> {

    /**
     * All possible states of round.
     * Note. Different implementation of round may use different
     * graph schema and some states maybe not produced at all.
     * Using for <code>FiniteStateMachine</code>
     * @see com.softserveinc.ita.kaiji.model.util.FiniteStateMachine
     */
    enum State {

        /**
         * Round is initializing now
         */
        ROUND_INITIALIZATION,

        /**
         * Round is started. No one of players didn't make turn.
         */
        ROUND_STARTED,

        /**
         * Round is playing. Some of players made turn and some not.
         * Round is waiting for make turn of rest players.
         */
        ROUND_PLAYING,

        /**
         * Round is ready to finish. All players already made turns.
         */
        ROUND_READY_TO_FINISH,

        /**
         * Round finished normally. Round result is generated and can be got.
         * Cant continue playing in this round.
         */
        ROUND_FINISHED,

        /**
         * Round was interrupted. Players who already made turn got back heir cards.
         * Cant continue playing in this round.
         */
        ROUND_INTERRUPTED,

        /**
         * Round was broken for some reason.
         * Switching to this state may produce exceptions.
         * Cant continue playing in this round.
         */
        ROUND_BROKEN
    }

    /**
     * Add player to round and made turn for him
     * @throws com.softserveinc.ita.kaiji.exception.game.PlayerAlreadyMadeTurnInThisRoundException
     * @throws com.softserveinc.ita.kaiji.exception.game.TooManyPlayersInRoundException
     * @param card chosen by player
     * @param player who making turn
     */
    void makeTurn(Card card, Player player);

    /**
     * Returns round result if round finished and <code>null</code> in other cases.
     * @return round result
     */
    RoundResult getRoundResult();

    /**
     * Finish round if it possible
     * @return state after round finishing
     */
    State finishRound();

    /**
     * Finish round if possible, if not interrupt round.
     * @return state after found interrupting
     */
    State interruptRound();
}
