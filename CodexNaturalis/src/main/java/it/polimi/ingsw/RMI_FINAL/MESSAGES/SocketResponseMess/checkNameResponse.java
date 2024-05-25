package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class checkNameResponse extends ResponseMessage {
    public int check;

    public checkNameResponse(int check) {
        this.check = check;
    }


    @Override
    public void action() {
        super.client.checkName = this.check;
        super.client.flag_check = false;
    }
}
