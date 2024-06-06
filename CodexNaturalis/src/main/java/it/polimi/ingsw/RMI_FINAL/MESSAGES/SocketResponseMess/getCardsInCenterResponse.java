package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class getCardsInCenterResponse extends ResponseMessage {


    public getCardsInCenterResponse() {

    }

    @Override
    public void action() {
        super.client.flag_check = false;
    }
}
