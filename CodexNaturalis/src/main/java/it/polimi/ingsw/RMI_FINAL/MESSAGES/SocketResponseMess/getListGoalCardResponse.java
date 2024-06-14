package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.List;

/**
 * Response message for getting the list of goal cards.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a list of goal cards that the client can choose from during setup.
 *
 */
public class getListGoalCardResponse extends ResponseMessage {

    public List<Goal> goal_cards;

    public getListGoalCardResponse(List<Goal> goal_cards) {
       this.goal_cards =  goal_cards ;
    }

    /**
     * Updates the client's state with the list of goal cards.
     *
     * This method sets the `goalsCard` list in the client object with the received information
     * about the available goal cards. It also resets the `flag_check` flag.
     */
    @Override
    public void action(){
        super.client.goalsCard = this.goal_cards;
        super.client.flag_check = false;
    }

}
