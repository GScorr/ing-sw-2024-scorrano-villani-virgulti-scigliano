package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class insertIdResponse extends ResponseMessage {
    int id;

    public insertIdResponse(int id) {
        this.id = id;
    }


    @Override
    public void action() {
        super.client.miniModel.setMy_index(id);
    }
}
