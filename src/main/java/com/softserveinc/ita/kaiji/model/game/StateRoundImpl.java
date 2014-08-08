package com.softserveinc.ita.kaiji.model.game;

import com.softserveinc.ita.kaiji.exception.MakeTurnException;
import com.softserveinc.ita.kaiji.exception.game.CanNotFinishRoundException;
import com.softserveinc.ita.kaiji.exception.game.PlayerAlreadyMadeTurnInThisRoundException;

import com.softserveinc.ita.kaiji.exception.util.SwitchStateException;
import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.Star;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.FiniteStateMachine;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * @author Paziy Evgeniy
 * @version 1.4
 * @since 20.03.14
 */
class StateRoundImpl implements Round {

    private static final Logger LOGGER = Logger.getLogger(StateRoundImpl.class);
    private static final Map<State, Set<State>> STATUS_SCHEMA = initStatusSchema();
    private static final int MAX_COUNT_OF_PLAYERS_WHO_MADE_TURN = 2;

    private Integer id;

    private FiniteStateMachine<State> statusChanger =
            new FiniteStateMachine<>(State.ROUND_INITIALIZATION, STATUS_SCHEMA);

    //todo check concurrent collections use
    private Set<Player> players = new HashSet<>();

    private volatile RoundResult roundResult = null;

    StateRoundImpl() {
        try {
            statusChanger.switchState(State.ROUND_STARTED);
        } catch (SwitchStateException e) {
            throw new IllegalStateException(e);
        }
    }

    private void buildRoundResult() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("building round result...");
        }

        if (statusChanger.getPreviousState() == State.ROUND_READY_TO_FINISH) {
            List<Player> playerList = new ArrayList<>(players);
            roundResult = new RoundResult(playerList.get(0), playerList.get(1));
        }
    }

    private void checkPlayer(Player player) {
        boolean playerAlreadyInRound = players.contains(player);
        if (playerAlreadyInRound) {
            String errorMassage = "Trying to make turn for player who already made turn";
            PlayerAlreadyMadeTurnInThisRoundException ex = new PlayerAlreadyMadeTurnInThisRoundException(errorMassage);
            LOGGER.error("Making turn error: ", ex);
            throw ex;
        }

        if (player.getState() != Player.PlayerStatus.TURN_READY) {
            throw new IllegalStateException("Player_temp not ready to make turn");
        }
    }

    @Override
    public synchronized void makeTurn(Card card, Player player) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("<in> Round.makeTurn()");
        }

        try {
            statusChanger.switchState(State.ROUND_PLAYING);
        } catch (SwitchStateException ex) {
            String message = "Can't make turn. Wrong state!";
            MakeTurnException makeTurnException = new MakeTurnException(message, ex);
            LOGGER.warn("Finishing round error", makeTurnException);
            throw makeTurnException;
        }

        checkPlayer(player);

        player.makeTurn(card);
        players.add(player);

        if (players.size() == MAX_COUNT_OF_PLAYERS_WHO_MADE_TURN) {
            try {
                statusChanger.switchState(State.ROUND_READY_TO_FINISH);
            } catch (SwitchStateException e) {
                throw new IllegalStateException(e);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("<out> Round.makeTurn()");
        }
    }

    @Override
    public State getState() {
        return statusChanger.getState();
    }

    @Override
    public RoundResult getRoundResult() {
        return roundResult;
    }

    @Override
    public synchronized State finishRound() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("<in> finishRound(); Finishing round");
        }

        try {
            statusChanger.switchState(State.ROUND_FINISHED);
        } catch (SwitchStateException ex) {
            String message = "Can't finish round. Wrong state!";
            CanNotFinishRoundException canNotFinishRound = new CanNotFinishRoundException(message, ex);
            LOGGER.warn("Finishing round error", canNotFinishRound);
            throw canNotFinishRound;
        }

        buildRoundResult();

        Integer quantity;
        for (Player p : players) {
            if (p.isGameWithStars()) {
                quantity = p.getStar().getQuantity();
                if (roundResult.getDuelResult(p) == Card.DuelResult.WIN) {
                    p.getStar().setQuantity(++quantity);
                } else if (roundResult.getDuelResult(p) == Card.DuelResult.LOSE) {
                    p.getStar().setQuantity(--quantity);
                }
            }
            p.commitTurn(roundResult.getDuelResult(p));
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("<in> finishRound(); round finished");
        }

        return getState();
    }

    @Override
    public synchronized State interruptRound() {
        if (statusChanger.canSwitchTo(State.ROUND_FINISHED)) {
            finishRound();
        } else {
            try {
                statusChanger.switchState(State.ROUND_INTERRUPTED);
            } catch (SwitchStateException e) {
                throw new IllegalStateException(e);
            }

            for (Player p : players) {
                p.rollbackTurn();
            }
        }

        return getState();
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }


    /**
     * Making schema for Finite-state machine possible state switches
     *
     * @return schema ror Finite-state machine
     * @see com.softserveinc.ita.kaiji.model.util.FiniteStateMachine
     */
    private static Map<State, Set<State>> initStatusSchema() {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("initialization statuses schema");
        }

        Map<State, Set<State>> statusMap = new HashMap<>();

        Set<State> roundInitialization = new HashSet<>();
        roundInitialization.add(State.ROUND_STARTED);
        roundInitialization.add(State.ROUND_BROKEN);

        Set<State> roundStarted = new HashSet<>();
        roundStarted.add(State.ROUND_PLAYING);
        roundStarted.add(State.ROUND_INTERRUPTED);
        roundStarted.add(State.ROUND_BROKEN);

        Set<State> roundPlaying = new HashSet<>();
        roundPlaying.add(State.ROUND_PLAYING);
        roundPlaying.add(State.ROUND_READY_TO_FINISH);
        roundPlaying.add(State.ROUND_INTERRUPTED);
        roundPlaying.add(State.ROUND_BROKEN);

        Set<State> roundReadyToFinish = new HashSet<>();
        roundReadyToFinish.add(State.ROUND_FINISHED);

        Set<State> roundFinished = new HashSet<>();

        Set<State> roundInterrupted = new HashSet<>();

        Set<State> roundBroken = new HashSet<>();

        statusMap.put(State.ROUND_INITIALIZATION, roundInitialization);
        statusMap.put(State.ROUND_STARTED, roundStarted);
        statusMap.put(State.ROUND_PLAYING, roundPlaying);
        statusMap.put(State.ROUND_READY_TO_FINISH, roundReadyToFinish);
        statusMap.put(State.ROUND_FINISHED, roundFinished);
        statusMap.put(State.ROUND_INTERRUPTED, roundInterrupted);
        statusMap.put(State.ROUND_BROKEN, roundBroken);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("statuses schema initialized");
        }
        return statusMap;
    }
}
