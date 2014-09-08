package com.softserveinc.ita.kaiji.dto.game;

import com.softserveinc.ita.kaiji.model.User;
import com.softserveinc.ita.kaiji.model.game.Game;
import com.softserveinc.ita.kaiji.model.game.GameInfo;
import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.util.Identifiable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Paziy Evgeniy
 * @version 1.2
 * @since 07.04.14
 */
@Entity
@Table(name = "game")
public class GameInfoEntity implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 80)
    private String gameName = null;

    @Column(name = "number_of_cards")
    private Integer numberOfCards = 0;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "game_type",
            nullable = false,
            columnDefinition = "enum('BOT_GAME','TWO_PLAYER_GAME', 'KAIJI_GAME')")
    private Game.Type gameType;

    @Column(name = "number_of_stars")
    private Integer numberOfStars;

    @Column(name = "number_of_players")
    private Integer numberOfPlayers;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date gameStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "finish_time")
    private Date gameFinishTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "link_game_to_user",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users;

    @OneToOne(mappedBy = "gameInfoEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private GameHistoryEntity gameHistoryEntity;

    public GameInfoEntity() {
    }

    public GameInfoEntity(GameInfo gameInfo) {
        gameName = gameInfo.getGameName();
        numberOfCards = gameInfo.getNumberOfCards();
        gameType = gameInfo.getGameType();
        numberOfStars = gameInfo.getNumberOfStars();
        gameStartTime = gameInfo.getGameStartTime();
        gameFinishTime = gameInfo.getGameFinishTime();

        users = new HashSet<>(gameInfo.getPlayers().size());
        for (Player p : gameInfo.getPlayers()) {
            users.add(p.getUser());
        }
        numberOfPlayers = users.size();
    }

    public GameHistoryEntity getGameHistoryEntity() {
        return gameHistoryEntity;
    }

    public void setGameHistoryEntity(GameHistoryEntity gameHistoryEntity) {
        this.gameHistoryEntity = gameHistoryEntity;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public void setGameType(Game.Type gameType) {
        this.gameType = gameType;
    }

    public Game.Type getGameType() {
        return gameType;
    }

    public Integer getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(Integer numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Date getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(Date gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    public Date getGameFinishTime() {
        return gameFinishTime;
    }

    public void setGameFinishTime(Date gameFinishTime) {
        this.gameFinishTime = gameFinishTime;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GameInfoEntity{" +
                "id=" + id +
                ", gameName='" + gameName + '\'' +
                ", numberOfCards=" + numberOfCards +
                ", gameType=" + gameType +
                ", users=" + users +
                '}';
    }
}
