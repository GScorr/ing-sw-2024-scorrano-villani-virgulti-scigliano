package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

/**
 * Response message for checking goal card presence.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * to a client's query about the presence of a specific goal card.
 *
 */
public class checkGoalCardPresent extends ResponseMessage {
    public boolean isPresent;

    public checkGoalCardPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    /**
     * Updates the client's state with the goal card presence information.
     *
     * This method sets the `GoalCardisPresent` flag in the client object
     * based on the received information and resets the `flag_check` flag.
     */
    @Override
    public void action(){
        super.client.GoalCardisPresent = this.isPresent;
        super.client.flag_check = false;
    }
}
