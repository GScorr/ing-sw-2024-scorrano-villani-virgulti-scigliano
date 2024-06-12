package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Response message for setting the player's hand cards.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a list of `PlayCard` objects representing the cards in the player's hand.
 */
public class setCardsResponse extends ResponseMessage {

    public List<PlayCard> cards = new ArrayList<>();

    public setCardsResponse(List<PlayCard> cards) {
        this.cards.addAll(cards);
    }

    /**
     * Updates the client's model with the hand cards.
     *
     * This method sets the `cards` list in the client's miniModel object with the received information
     * about the player's hand cards.
     */
    @Override
    public void action(){
        super.client.miniModel.setCards(this.cards);
    };
}
