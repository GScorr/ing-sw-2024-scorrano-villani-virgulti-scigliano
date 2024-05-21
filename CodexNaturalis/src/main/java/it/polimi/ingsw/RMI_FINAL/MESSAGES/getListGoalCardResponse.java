package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MODEL.Goal.Goal;

import java.util.List;

public class getListGoalCardResponse extends ResponseMessage{
    public List<Goal> goal_cards;

    public getListGoalCardResponse(List<Goal> goal_cards) {
       this.goal_cards =  goal_cards ;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
