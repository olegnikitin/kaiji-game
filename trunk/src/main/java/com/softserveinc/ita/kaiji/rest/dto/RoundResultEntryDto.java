package com.softserveinc.ita.kaiji.rest.dto;
import com.softserveinc.ita.kaiji.model.Card;

public class RoundResultEntryDto {

    private String userName;
    private Card card;
    private Card.DuelResult duelResult;

    public RoundResultEntryDto(String userName, Card card, Card.DuelResult duelResult) {
        this.userName = userName;
        this.card = card;
        this.duelResult = duelResult;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card.DuelResult getDuelResult() {
        return duelResult;
    }

    public void setDuelResult(Card.DuelResult duelResult) {
        this.duelResult = duelResult;
    }
}