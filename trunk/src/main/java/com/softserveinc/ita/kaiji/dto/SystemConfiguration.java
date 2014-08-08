package com.softserveinc.ita.kaiji.dto;

import com.softserveinc.ita.kaiji.model.player.bot.Bot;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Vladyslav Shelest
 * @version 1.0
 * @since 29.03.14.
 */
@XmlRootElement
public class SystemConfiguration {
    @NotEmpty
    @Size(min = 2, max= 30)
    private String gameName;
    @NotEmpty
    @Size(min = 2, max= 30)
    private String userName;
    @NotNull
    @Range(min =  1, max = 5)
    private Integer numberOfCards;
    @NotNull
    @Range(min =  1, max = 10)
    private Integer numberOfStars;
    @NotNull
    private Bot.Types botType;
    @NotNull
    @Range(min =  1, max = 300)
    private Long gameConnectionTimeout;
    @NotNull
    @Range(min =  1, max = 100)
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

    public Integer getNumberOfStars() {
        return numberOfStars;
    }

    @XmlElement
    public void setNumberOfStars(Integer numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (obj instanceof SystemConfiguration) {
            SystemConfiguration configuration = (SystemConfiguration) obj;
            return this.gameName.equals(configuration.getGameName())
                    && this.userName.equals(configuration.getUserName())
                    && this.numberOfCards.equals(configuration.getNumberOfCards())
                    && this.botType.equals(configuration.botType)
                    && this.gameConnectionTimeout.equals(configuration.getGameConnectionTimeout())
                    && this.roundTimeout.equals(configuration.getRoundTimeout())
                    && this.numberOfStars.equals(configuration.getNumberOfStars());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = gameName != null ? gameName.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (numberOfCards != null ? numberOfCards.hashCode() : 0);
        result = 31 * result + (botType != null ? botType.toString().hashCode() : 0);
        result = 31 * result + (gameConnectionTimeout != null ? gameConnectionTimeout.hashCode() : 0);
        result = 31 * result + (roundTimeout != null ? roundTimeout.hashCode() : 0);
        result = 31 * result + (numberOfStars != null ? numberOfStars.hashCode() : 0);
        return result;
    }
}