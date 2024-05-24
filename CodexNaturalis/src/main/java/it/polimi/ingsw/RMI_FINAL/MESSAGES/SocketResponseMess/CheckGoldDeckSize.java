package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class CheckGoldDeckSize extends ResponseMessage {
    public boolean checkSize;

    public CheckGoldDeckSize(boolean check) {
        this.checkSize = check;
    }


    public void action(){
        super.client.checkSizeGoldDeck = checkSize;
        super.client.flag_check = false;
    }


}
