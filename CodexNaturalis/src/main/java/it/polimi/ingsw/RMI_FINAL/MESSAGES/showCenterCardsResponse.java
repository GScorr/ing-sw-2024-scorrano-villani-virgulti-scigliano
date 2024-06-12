package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Card.PlayCard;

import java.io.IOException;

/**
 * Response message for showing a card from the center of the game board.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a single `PlayCard` object representing a card from the center of the game board.
 *
 */
public class showCenterCardsResponse extends ResponseMessage{

    public PlayCard card;

    public showCenterCardsResponse(  PlayCard card) {
        this.card = card;
    }

    /**
     * This method call the function showing card to the client.
     *
     * @throws IOException  if an I/O error occurs while communicating with the client
     */
    @Override
    public void action() throws IOException {
        super.client.showCard(this.card);
    }

}
