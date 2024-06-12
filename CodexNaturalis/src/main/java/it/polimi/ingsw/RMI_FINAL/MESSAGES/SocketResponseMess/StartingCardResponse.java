package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for receiving the starting card.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a `PlayCard` object representing the card the player receives at the beginning of the game.
 */
public class StartingCardResponse extends ResponseMessage {

    public PlayCard starting_card;

    public StartingCardResponse(PlayCard starting_card) {
        this.starting_card = starting_card;
    }

    /**
     * Updates the client's model with the starting card and resets the check flag.
     *
     * This method sets the `startingCard` field in the client object with the received information
     * about the player's starting card. It also resets the `flag_check` flag.
     */
    @Override
    public void  action() {
        super.client.startingCard = this.starting_card;
        super.client.flag_check = false;
    }
}
