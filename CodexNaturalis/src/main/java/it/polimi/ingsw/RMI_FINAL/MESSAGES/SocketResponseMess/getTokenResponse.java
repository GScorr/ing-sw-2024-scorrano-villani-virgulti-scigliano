package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for assigning a token to the client.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a unique token assigned to the client for identification purposes.
 *
 */
public class getTokenResponse extends ResponseMessage {

    private String token;

    public getTokenResponse(String token) {
        this.token = token;
    }

    public void setState(String token){
        this.token = token;
    }

    public String getState(){
        return token;
    }

    /**
     * Updates the client's state with the assigned token.
     *
     * This method sets the `token` field in the client object with the received information.
     * It also resets the `flag_check` flag
     */
    @Override
    public void action() {
        super.client.token = token;
        super.client.flag_check = false;
    }
}
