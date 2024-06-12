package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.GameField;

/**
 * Response message for sending an update message to the client.
 *
 */
public class UpdateMessage extends ResponseMessage{

    private String message;

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
    public void action(){
        System.out.println(message);
    }

}
