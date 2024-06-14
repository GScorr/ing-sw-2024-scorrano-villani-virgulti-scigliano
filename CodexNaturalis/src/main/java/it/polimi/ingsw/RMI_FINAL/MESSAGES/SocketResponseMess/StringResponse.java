package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for sending a generic string to the client.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a simple string message. The exact usage of this message might
 * depend on the specific implementation.
 */
public class StringResponse extends ResponseMessage {

    public String string;

    public StringResponse(String string) {
        this.string = string;
    }

    /**
     * Prints the string message to the console (likely for debugging purposes).
     *
     * This implementation currently prints the message to the server console.
     */
    public void action(){
        System.out.println(string);
    }

}
