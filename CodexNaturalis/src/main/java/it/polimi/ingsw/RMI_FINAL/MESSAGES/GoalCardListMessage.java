package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Goal.Goal;

import java.util.List;

/**
 * Response message for sending a list of goal cards to the client.
 *
 * This class extends the `ResponseMessage` class and represents a response
 * containing a list of `Goal` objects representing the player's goal cards
 * for the current game.
 *
 */
public class GoalCardListMessage extends ResponseMessage{

    public List<Goal> list_goal_card;

    public GoalCardListMessage(List<Goal> list_goal_card) {
        this.list_goal_card = list_goal_card;
    }

}
