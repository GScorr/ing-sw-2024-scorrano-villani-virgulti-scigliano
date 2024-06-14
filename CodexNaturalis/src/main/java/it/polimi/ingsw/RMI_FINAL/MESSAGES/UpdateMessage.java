package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.GameField;

import java.io.IOException;
import java.rmi.NotBoundException;

/**
 * Response message for sending an update message to the client.
 *
 */
public class UpdateMessage extends ResponseMessage{

    private String message;

    public boolean isAlone;

    public boolean win = false;

    private GameField gamefield;

    public UpdateMessage(String string) {
        this.message = string;
    }

   /*
   da eliminare
    public UpdateMessage(GameField gamefield) {
        this.gamefield = gamefield;
    }

    */

    /**
     * Prints the update message to the console
     *
     */
    @Override
    public void action() throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {
        virtual_view.getTerminal_interface().printUpdateMessage(this.message);
        virtual_view.getTerminal_interface().startCountdown(message, isAlone, this.win);
    }

}
