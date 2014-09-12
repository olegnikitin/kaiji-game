package com.softserveinc.ita.kaiji.model.game;


import com.softserveinc.ita.kaiji.model.*;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.Identifiable;
import com.softserveinc.ita.kaiji.model.util.Statable;
import com.softserveinc.ita.kaiji.model.util.pool.Poolable;

/**
 * Game API. To get an instance of game use <code>GameFactory</code>.
 * Every game has states. You can get current state by using <code>getState()</code> method.
 * And then decide what can you do with game next.
 * Avery game have id. Game doesn't control it, just hold.
 * @see com.softserveinc.ita.kaiji.model.game.GameFactory
 * @see com.softserveinc.ita.kaiji.model.game.Game.State
 * @see com.softserveinc.ita.kaiji.model.util.Statable
 * @see com.softserveinc.ita.kaiji.model.util.Identifiable
 *
 * @author Paziy Evgeniy
 * @version 3.1
 * @since 17.03.2014
 */
public interface Game extends Identifiable, Statable<Game.State>, Poolable<Integer> {

    /**
     * Type of created game
     * @see com.softserveinc.ita.kaiji.model.game.GameFactory
     */
    enum Type {
        /**
         * Game between the player and the bot
         * One round per one moment of time.
         * <code>makeTurn()</code> automatically make turn for bot player
         * and finish round.
         * @see com.softserveinc.ita.kaiji.model.game.Round
         * @see com.softserveinc.ita.kaiji.model.game.BotGameImpl
         */
        BOT_GAME,

        /**
         *
         * Game between two players.
         * One round per one moment of time.
         * @see com.softserveinc.ita.kaiji.model.game.Round
         * @see com.softserveinc.ita.kaiji.model.game.TwoPlayerGameImpl
         */
        TWO_PLAYER_GAME,

        /**
         * !!! Not supported yet !!!
         *
         * Game between four or more player. The real Kaiji game.
         * Few round at one moment of time.
         * For rounds used pool.
         * @see com.softserveinc.ita.kaiji.model.game.Round
         */
        KAIJI_GAME
    }

    /**
     * All possible states of game.
     * Note. Different implementation of game may use different
     * graph schema and some states maybe not produced at all.
     * Using for <code>FiniteStateMachine</code>
     * @see com.softserveinc.ita.kaiji.model.util.FiniteStateMachine
     */
    enum State {
        /**
         * Game is initializing and can't be played now.
         */
        GAME_INITIALIZATION,

        /**
         * Game was just started. No one made turn in this game
         */
        GAME_STARTED,

        /**
         * Game in process. Some players already made turn int this game.
         */
        GAME_PLAYING,

        /**
         * Game was normally finished, players can't continue game.
         */
        GAME_FINISHED,

        /**
         * Gama was interrupted for some reason, players can't continue game.
         */
        GAME_INTERRUPTED,

        /**
         * Game was broken, players can't continue playing.
         * When game goes into this status may throw some exception.
         */
        GAME_BROKEN
    }

    /**
     * Returns information about game.
     * @return information about game.
     */
    GameInfo getGameInfo();

    /**
     * Makes turn with chosen card for chosen player
     *
     * @param card for making turn
     * @param player who makes turn
     */
    void makeTurn(Card card, Player player);

    void makeTurn(Card card, Player player, Round round);

    /**
     * Returns true if game can be normally finished.
     *
     * @return true if game can be successfully finished
     */
    boolean canFinishGame();

    /**
     * Finishes game if it can be finished
     * @return current game status
     */
    State finishGame();

    /**
     * Returns true if game can be Interrupted.
     * @return true if game successfully interrupted
     */
    boolean canInterruptGame();

    /**
     * Interrupt game. If game can be finished
     * it will be finished and not interrupted.
     * @return current game status
     */
    State interruptGame();

    /**
     * Returns history of game for current moment.
     * If game is finished then history is immutable.
     * If game is still playing it will be changed
     * and need to get again updated history.
     * @return history of game for current moment.
     */
    GameHistory getGameHistory();
}