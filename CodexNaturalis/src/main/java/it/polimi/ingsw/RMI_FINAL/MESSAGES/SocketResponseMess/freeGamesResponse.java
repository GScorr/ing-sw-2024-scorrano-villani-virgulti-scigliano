package it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;

import java.util.List;

public class freeGamesResponse extends ResponseMessage {
    List<SocketRmiControllerObject> free_games;

    public freeGamesResponse(List<SocketRmiControllerObject> free_games) {
        this.free_games = free_games;
    }


    @Override
    public void action() {
        super.client.free_games = this.free_games;
        super.client.flag_check = false;
    }
}
