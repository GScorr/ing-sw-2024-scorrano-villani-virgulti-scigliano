package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for checking username validity.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * to a client's query about the validity of their chosen username.
 *
 */
public class checkNameResponse extends ResponseMessage {
    public int check;

    public checkNameResponse(int check) {
        this.check = check;
    }

    /**
     * Updates the client's state with the username validity information.
     *
     * This method sets the `checkName` flag in the client object based on the received information
     * It also resets the `flag_check` flag.
     */
    @Override
    public void action() {
        super.client.checkName = this.check;
        super.client.flag_check = false;
    }

}
