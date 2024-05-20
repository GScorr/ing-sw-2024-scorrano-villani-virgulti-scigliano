package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;

public class CardResponse extends ResponseMessage{
    public PlayCard card;

    public CardResponse(PlayCard card) {
        this.card = card;
    }


    @Override
    public String getMessage() {
        return message;
    }

    public PlayCard CardResponseAction(){
        return this.card;
    }

    public void action(){

    }
}
