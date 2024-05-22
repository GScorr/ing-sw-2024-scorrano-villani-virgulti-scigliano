package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class checkGoalCardPresent extends ResponseMessage {
    public boolean isPresent;

    public checkGoalCardPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    @Override
    public void action(){
        super.client.GoalCardisPresent = this.isPresent;
        super.client.flag_check = false;
    }
}
