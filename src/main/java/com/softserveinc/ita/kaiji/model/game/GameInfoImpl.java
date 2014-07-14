package com.softserveinc.ita.kaiji.model.game;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.softserveinc.ita.kaiji.model.player.Player;
import com.softserveinc.ita.kaiji.model.player.bot.Bot.Types;

/**
 * @author Bohdan Shaposhnik
 * @author Eugene Semenkov
 * @author Paziy Evgeniy
 * @version 2.6
 * @since 17.03.2014
 */
//todo clear class
public class GameInfoImpl implements GameInfo {

    private static final Logger LOG = Logger.getLogger(GameInfoImpl.class);

    private Integer id;

    private Integer databaseId;

    private String gameName = null;

    private Integer numberOfCards = 0;

    private Game.Type gameType;

    private Date gameStartTime;

    private Date gameFinishTime;

    private boolean botGame = false;

    private Set<Player> players = new HashSet<Player>();

    public GameInfoImpl() {
    }

    public GameInfoImpl(
            String gameName
            , String ownerName
            , Integer numberOfCards
            , boolean botGame
            , Types botType
            , Set<Player> players) {
        this.gameName = gameName;
        this.numberOfCards = numberOfCards;
        this.botGame = botGame;
        this.players = players;
        if (this.botGame && LOG.isDebugEnabled()) {
            LOG.trace("GameInfoImpl: the mod is: bot VS player");
        }
    }


    @Override
    public boolean isBotGame() {
        return botGame;
    }

    public void setBotGame(boolean botGame) {
        this.botGame = botGame;
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    @Override
    public void setGameType(Game.Type gameType) {
        this.gameType = gameType;
    }

    @Override
    public Game.Type getGameType() {
        return gameType;
    }

    @Override
    public Date getGameStartTime() {
        return gameStartTime;
    }

    @Override
    public void setGameStartTime(Date gameStartTime) {
        this.gameStartTime = gameStartTime;
    }

    @Override
    public Date getGameFinishTime() {
        return gameFinishTime;
    }

    @Override
    public void setGameFinishTime(Date gameFinishTime) {
        this.gameFinishTime = gameFinishTime;
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
    public Integer getPoolKey() {
        return this.id;
    }

    @Override
    public Set<Player> getPlayers() {
        return players;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }


}
