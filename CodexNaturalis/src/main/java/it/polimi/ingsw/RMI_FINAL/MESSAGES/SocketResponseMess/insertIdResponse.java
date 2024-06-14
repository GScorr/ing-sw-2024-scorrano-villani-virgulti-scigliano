package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for assigning a player ID.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a unique player ID assigned to the client.
 *
 */
public class insertIdResponse extends ResponseMessage {

    int id;

    public insertIdResponse(int id) {
        this.id = id;
    }

    /**
     * Updates the client's model with the assigned player ID.
     *
     * This method delegates the ID setting to the client's miniModel object
     */
    @Override
    public void action() {
        super.client.miniModel.setMy_index(id);
    }
}
