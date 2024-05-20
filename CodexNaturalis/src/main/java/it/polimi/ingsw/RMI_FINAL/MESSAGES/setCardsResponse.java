package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.util.ArrayList;
import java.util.List;

public class setCardsResponse extends ResponseMessage{
    public List<PlayCard> cards = new ArrayList<>();

    public setCardsResponse(List<PlayCard> cards) {
        this.cards.addAll(cards);
    }

    @Override
    public String getMessage() {
        return message;
    }
    @Override
    public void action(){
        super.miniModel.setCards(this.cards);
    };
}
