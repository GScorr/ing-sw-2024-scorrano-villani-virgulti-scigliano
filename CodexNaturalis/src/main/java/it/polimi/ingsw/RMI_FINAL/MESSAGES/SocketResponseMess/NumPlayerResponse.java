package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class NumPlayerResponse extends ResponseMessage {
    int num_player;
    public NumPlayerResponse(int num_player) {
        this.num_player = num_player;
    }

    @Override
    public void action() {
        super.client.miniModel.setNum_players(num_player);
    }
}
