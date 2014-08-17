package com.softserveinc.ita.kaiji.dto.game;

import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameHistory;
import com.softserveinc.ita.kaiji.model.game.RoundResult;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.Identifiable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 12.04.14
 */
@Entity
@Table(name = "game_history")
public class GameHistoryEntity implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id", nullable = false, unique = true)
    private GameInfoEntity gameInfoEntity;

    @OneToMany(mappedBy = "gameHistory", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<RoundResultEntity> roundResults;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_winners",
            joinColumns = {@JoinColumn(name = "game_history_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> winners;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state",
            nullable = false,
            columnDefinition = "enum('GAME_INITIALIZATION','GAME_STARTED', 'GAME_PLAYING', 'GAME_FINISHED'," +
                    "'GAME_INTERRUPTED', 'GAME_BROKEN')")
    private Game.State gameState;

    public GameHistoryEntity() {

    }

    public GameHistoryEntity(GameHistory gameHistory) {
        gameInfoEntity = new GameInfoEntity(gameHistory.getGameInfo());
        Integer dbId =  gameHistory.getGameInfo().getDatabaseId();
        gameInfoEntity.setId(dbId);


        List <RoundResult> roundResultList = gameHistory.getRoundResults();
        roundResults = new HashSet<>(roundResultList.size());
        for (int i = 0; i < gameHistory.getRoundResults().size(); ++i) {
            RoundResultEntity roundResultEntity = new RoundResultEntity(roundResultList.get(i));
            roundResultEntity.setNumber(i);
            roundResultEntity.setGameHistory(this);
            roundResults.add(roundResultEntity);
        }

        gameState = gameHistory.getGameStatus();

        winners = new HashSet<>();
        for (Player p : gameHistory.getWinners()) {
            winners.add(p.getUser());
        }
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public GameInfoEntity getGameInfo() {
        return gameInfoEntity;
    }

    public void setGameInfo(GameInfoEntity gameInfo) {
        this.gameInfoEntity = gameInfo;
    }

    public Set<RoundResultEntity> getRoundResults() {
        return roundResults;
    }

    public void setRoundResults(Set<RoundResultEntity> roundResults) {
        this.roundResults = roundResults;
    }

    public Set<User> getWinners() {
        return winners;
    }

    public void setWinners(Set<User> winners) {
        this.winners = winners;
    }

    public Game.State getGameState() {
        return gameState;
    }

    public void setGameState(Game.State gameState) {
        this.gameState = gameState;
    }

    @Override
    public String toString() {
        return "GameHistoryEntity{" +
                "id=" + id +
                ", gameInfoEntity=" + gameInfoEntity +
                ", roundResults=" + roundResults +
                ", winners=" + winners +
                ", gameState=" + gameState +
                '}';
    }
}
