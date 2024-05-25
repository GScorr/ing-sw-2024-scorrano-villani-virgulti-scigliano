package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.util.List;

public class getListGoalCardResponse extends ResponseMessage {
    public List<Goal> goal_cards;

    public getListGoalCardResponse(List<Goal> goal_cards) {
       this.goal_cards =  goal_cards ;
    }

    @Override
    public void action(){
        super.client.goalsCard = this.goal_cards;
        super.client.flag_check = false;
    }
}
