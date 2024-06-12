package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;

/**
 * Response message for sending a card to the client.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a single `PlayCard` object.
 *
 */
public class CardResponse extends ResponseMessage{

    public PlayCard card;

    public CardResponse(PlayCard card) {
        this.card = card;
    }

    /* da eliminare
    public PlayCard CardResponseAction(){
        return this.card;
    }

     */

    public void action(){}
}
