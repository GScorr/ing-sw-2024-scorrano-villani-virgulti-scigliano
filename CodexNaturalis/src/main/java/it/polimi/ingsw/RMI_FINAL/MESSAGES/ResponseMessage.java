package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public abstract class ResponseMessage implements Serializable {
    public clientSocket client;
    List<SocketRmiControllerObject> free_games_Socket;

    public void setClient(clientSocket client){
        this.client = client;
    }

    public void setFree_games(List<SocketRmiControllerObject> free_games) {
        this.free_games_Socket = free_games;
    }

    public void action() throws IOException {

    };

}
