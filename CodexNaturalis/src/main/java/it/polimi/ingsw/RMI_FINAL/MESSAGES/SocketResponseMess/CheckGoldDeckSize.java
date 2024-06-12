package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for checking the gold deck size.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * to a client's query about the size of the gold deck.
 *
 */
public class CheckGoldDeckSize extends ResponseMessage {
    public boolean checkSize;

    public CheckGoldDeckSize(boolean check) {
        this.checkSize = check;
    }

    /**
     * Updates the client's state with the gold deck size information.
     *
     * This method sets the `checkSizeGoldDeck` flag in the client object
     * based on the received information and resets the `flag_check` flag.
     */
    public void action(){
        super.client.checkSizeGoldDeck = checkSize;
        super.client.flag_check = false;
    }

}
