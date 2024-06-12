package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for setting the game state.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a string message that likely describes the current state of the game
 */
public class setStateMessage extends ResponseMessage {

    private String state;

    public setStateMessage(String state) {
        this.state = state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }

    /**
     * Updates the client's model with the state message.
     *
     * This method delegates setting the state message to the client's miniModel object,
     */
    @Override
    public void action() {
        super.client.miniModel.setState(state);
    }
}
