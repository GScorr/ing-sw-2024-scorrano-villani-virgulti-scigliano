package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;

import java.util.List;

public class CardsMessage {
    List<PlayCard> cards;

    public CardsMessage(List<PlayCard> cards) {
        this.cards = cards;
    }

    public List<PlayCard>  getField() {
        return cards;
    }

    public void setCards(List<PlayCard> cards) {
        this.cards = cards;
    }
}
