package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Goal.Goal;

import java.util.List;

public class GoalCardListMessage extends ResponseMessage{
    public List<Goal> list_goal_card;

    public GoalCardListMessage(List<Goal> list_goal_card) {
        this.list_goal_card = list_goal_card;
    }


}
