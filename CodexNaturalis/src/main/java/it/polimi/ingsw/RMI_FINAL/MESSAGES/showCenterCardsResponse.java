package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.State.PlaceCard;

public class showCenterCardsResponse extends ResponseMessage{
    public PlayCard card;
    public showCenterCardsResponse(  PlayCard card) {
        this.card = card;
    }

}
