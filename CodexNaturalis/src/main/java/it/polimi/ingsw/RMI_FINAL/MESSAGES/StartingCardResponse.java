package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;

public class StartingCardResponse extends ResponseMessage{

    public PlayCard starting_card;
    public StartingCardResponse(PlayCard starting_card) {
        this.starting_card = starting_card;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
