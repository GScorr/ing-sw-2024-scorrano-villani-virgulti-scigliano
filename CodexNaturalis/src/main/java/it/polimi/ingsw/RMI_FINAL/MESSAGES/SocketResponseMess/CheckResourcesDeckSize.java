package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.IOException;

/**
 * Response message for checking the resource deck size.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * to a client's query about the size of the resource deck.
 *
 */
public class CheckResourcesDeckSize extends ResponseMessage {
    public boolean checkSize;

    public CheckResourcesDeckSize(boolean check) {
        this.checkSize = check;
    }

    /**
     * Updates the client's state with the resource deck size information.
     *
     * This method sets the `checkSizeResourcesDeck` flag in the client object
     * based on the received information and resets the `flag_check` flag.
     *
     * @throws IOException This exception might be thrown if there's an issue
     * communicating with the client.
     */
    @Override
    public void action() throws IOException {
        super.client.checkSizeResourcesDeck = this.checkSize;
        super.client.flag_check = false;
    }

}
