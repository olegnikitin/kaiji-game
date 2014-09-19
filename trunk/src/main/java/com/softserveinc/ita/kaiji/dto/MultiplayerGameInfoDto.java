package com.softserveinc.ita.kaiji.dto;

import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents Data Transfer Object which is used
 * to pass information about the multiplayer game via a form
 *
 * @author Konstantin Shevchuk
 * @version 1.6
 * @since 14.07.14.
 */

@Component
public class MultiplayerGameInfoDto {

    @Size(min = 1, max = 10 )
    private String gameName;

    @NotNull
    @Range(min = 1, max = 10 )
    private Integer numberOfPlayers;

    @NotNull
    @Range(min = 1, max = 5 )
    private Integer numberOfCards;

    @NotNull
    @Range(min = 1, max = 10)
    private Integer numberOfStars;

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Integer getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(Integer numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public String getGameName() {
        return gameName;
    }

    public Integer getNumberOfCards() {
        return numberOfCards;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setNumberOfCards(Integer numberOfCards) {
        this.numberOfCards = numberOfCards;
    }

    @Override
    public String toString() {
        return "MultiplayerGameInfoDto{" +
                "gameName='" + gameName + '\'' +
                ", numberOfPlayers=" + numberOfPlayers +
                ", numberOfCards=" + numberOfCards +
                ", numberOfStars=" + numberOfStars +
                '}';
    }
}
