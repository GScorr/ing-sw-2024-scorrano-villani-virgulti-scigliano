package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

public class setCardsResponse extends ResponseMessage {
    public List<PlayCard> cards = new ArrayList<>();

    public setCardsResponse(List<PlayCard> cards) {
        this.cards.addAll(cards);
    }


    @Override
    public void action(){
        super.client.miniModel.setCards(this.cards);
    };
}
