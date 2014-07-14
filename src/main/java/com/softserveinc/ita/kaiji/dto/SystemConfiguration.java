package com.softserveinc.ita.kaiji.dto;

import com.softserveinc.ita.kaiji.model.player.bot.Bot;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 29.03.14.
 */
@XmlRootElement
public class SystemConfiguration {
    private String gameName;
    private String userName;
    private Integer numberOfCards;
    private Bot.Types botType;
    private Long gameConnectionTimeout;
    private Long roundTimeout;

    public Bot.Types getBotType() {
        return botType;
    }

    @XmlElement
    public void setBotType(Bot.Types botType) {
        this.botType = botType;
    }

    public String getGameName() {
        return gameName;
    }
    @XmlElement
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUserName() {
        return userName;
    }
    @XmlElement
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }
    @XmlElement
    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    public Long getGameConnectionTimeout() {
        return gameConnectionTimeout;
    }
    @XmlElement
    public void setGameConnectionTimeout(Long gameConnectionTimeout) {
        this.gameConnectionTimeout = gameConnectionTimeout;
    }

    public Long getRoundTimeout() {
        return roundTimeout;
    }
    @XmlElement
    public void setRoundTimeout(Long roundTimeout) {
        this.roundTimeout = roundTimeout;
    }
}