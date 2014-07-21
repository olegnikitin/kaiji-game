package com.softserveinc.ita.kaiji.dto.game;

import com.softserveinc.ita.kaiji.model.Card;
import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.RoundResult;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.Identifiable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @see com.softserveinc.ita.kaiji.model.game.RoundResult
 * @author Paziy Evgeniy
 * @version 1.1
 * @since 12.04.14
 */
@Entity
@Table(name = "round")
public class RoundResultEntity implements Identifiable {

    @Embeddable
    public static class Entry {

        @Enumerated(EnumType.STRING)
        @Column(name = "card", nullable = false, columnDefinition = "enum('ROCK','PAPER', 'SCISSORS')")
        private Card card;

        @Enumerated(EnumType.STRING)
        @Column(name = "duel_result", nullable = false, columnDefinition = "enum('WIN','LOSE', 'DRAW')")
        private Card.DuelResult duelResult;

        public Entry() {
        }

        public Entry(Card card, Card.DuelResult duelResult) {
            this.card = card;
            this.duelResult = duelResult;
        }

        public Card getCard() {
            return card;
        }

        public Card.DuelResult getDuelResult() {
            return duelResult;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Transient
    @Column(name = "number")
    private Integer number;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_history_id", nullable = false)
    private GameHistoryEntity gameHistory;

    @ElementCollection(fetch = FetchType.EAGER)
    //@OneToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})

    @JoinTable(
            name = "round_detail",
            joinColumns = {@JoinColumn(name = "round_id")})
    @MapKeyJoinColumn(name = "user_id", nullable = false)
    private Map<User, Entry> round;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    public RoundResultEntity() {
    }

    public RoundResultEntity(RoundResult roundResult) {
        round = new HashMap<>(roundResult.getPlayers().size());
        for (Player p : roundResult.getPlayers()) {
            round.put(p.getUser(), new Entry(roundResult.getCard(p), roundResult.getDuelResult(p)));
        }

        endTime = roundResult.getEndTime();
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<User,Entry> map : round.entrySet()){
            builder.append(map.getKey().getName()).append("->");
            builder.append(map.getValue().getCard()).append("->");
            builder.append(map.getValue().getDuelResult()).append(";");
        }
        return builder.toString();
    }

    public Set<User> getUsers() {
        return round.keySet();
    }

    public Map<User, Entry> getRound() {
        return round;
    }

    public void setRound(Map<User, Entry> round) {
        this.round = round;
    }

    public GameHistoryEntity getGameHistory() {
        return gameHistory;
    }

    public void setGameHistory(GameHistoryEntity gameHistory) {
        this.gameHistory = gameHistory;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
