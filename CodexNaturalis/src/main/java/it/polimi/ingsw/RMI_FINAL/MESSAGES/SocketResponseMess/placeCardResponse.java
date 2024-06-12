package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for placing a card.
 *
 * This class extends the `ResponseMessage` class
 *
 */
public class placeCardResponse extends ResponseMessage {

    public placeCardResponse() {

    }

    /**
     * Resets the client's check flag.
     */
    @Override
    public void action() {
        super.client.flag_check = false;
    }
}
