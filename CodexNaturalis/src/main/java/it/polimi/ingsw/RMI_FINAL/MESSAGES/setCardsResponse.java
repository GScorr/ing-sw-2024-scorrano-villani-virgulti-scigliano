package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.util.List;

public class setCardsResponse extends ResponseMessage{
    public List<PlayCard> cards;

    public setCardsResponse(List<PlayCard> cards) {
        this.cards = cards;
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
