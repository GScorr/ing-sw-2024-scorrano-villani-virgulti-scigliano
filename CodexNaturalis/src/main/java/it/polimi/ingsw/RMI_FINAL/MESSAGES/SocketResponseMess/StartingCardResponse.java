package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class StartingCardResponse extends ResponseMessage {

    public PlayCard starting_card;
    public StartingCardResponse(PlayCard starting_card) {
        this.starting_card = starting_card;
    }

    @Override
    public void  action() {
        super.client.startingCard = this.starting_card;
        super.client.flag_check = false;
    }
}
