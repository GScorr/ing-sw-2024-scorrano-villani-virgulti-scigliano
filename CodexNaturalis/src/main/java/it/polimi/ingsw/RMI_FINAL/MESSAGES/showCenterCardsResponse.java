package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.State.PlaceCard;

import java.io.IOException;

public class showCenterCardsResponse extends ResponseMessage{
    public PlayCard card;
    public showCenterCardsResponse(  PlayCard card) {
        this.card = card;
    }

    @Override
    public void action() throws IOException {
        super.client.showCard(this.card);
    }
}
