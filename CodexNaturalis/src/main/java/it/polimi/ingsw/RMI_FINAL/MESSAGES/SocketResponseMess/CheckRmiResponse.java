package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for RMI connection check.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * to a client's query about the status of the RMI connection.
 *
 */
public class CheckRmiResponse extends ResponseMessage {
    public boolean check;

    public CheckRmiResponse(boolean check) {
        this.check = check;
    }

    /**
     * Updates the client's state with the RMI connection status.
     *
     * This method sets the `check` flag in the client object based on the received information
     * It also resets the `flag_check` flag.
     */
    public void action(){
        super.client.check = this.check;
        super.client.flag_check = false;
    }

}
