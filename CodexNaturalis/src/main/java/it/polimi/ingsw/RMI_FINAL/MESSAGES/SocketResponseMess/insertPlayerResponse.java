package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

public class insertPlayerResponse extends ResponseMessage {
    Player player;


    public insertPlayerResponse(Player player) {
       this.player = player;
    }


    @Override
    public void action() {
        super.client.miniModel.setMy_player(player);
    }
}
