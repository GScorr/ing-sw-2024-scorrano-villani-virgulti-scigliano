package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class CheckRmiResponse extends ResponseMessage {
    public boolean check;

    public CheckRmiResponse(boolean check) {
        this.check = check;
    }

    public void action(){
        super.client.check = this.check;
        super.client.flag_check = false;
    }

}
