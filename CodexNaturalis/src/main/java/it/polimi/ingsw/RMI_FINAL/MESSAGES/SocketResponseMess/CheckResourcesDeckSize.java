package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import java.io.IOException;

public class CheckResourcesDeckSize extends ResponseMessage {
    public boolean checkSize;

    public CheckResourcesDeckSize(boolean check) {
        this.checkSize = check;
    }


    @Override
    public void action() throws IOException {
        super.client.checkSizeResourcesDeck = this.checkSize;
        super.client.flag_check = false;
    }
}
