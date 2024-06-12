package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for getting cards in the center.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * to a client's query about the cards currently available in the center of the game board.
 *
 */
public class getCardsInCenterResponse extends ResponseMessage {

    public getCardsInCenterResponse() {}

    /**
     * Resets the client's check flag.
     *
     * This method does not directly update the client's model as the card information
     * might be included in a separate message. It resets the `flag_check` flag
     */
    @Override
    public void action() {
        super.client.flag_check = false;
    }

}
