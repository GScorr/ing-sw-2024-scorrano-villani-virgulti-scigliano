package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.getListGoalCardResponse;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a message sent by the client to request the list of initial goal cards for the game.
 */
public class getListGoalCard implements Message, Serializable {

    public ServerSocket serverSocket;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;


    private ClientHandler clientHandler;
    @Override
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }

    public getListGoalCard(){}

    public void setToken(String token) {
        this.token = token;
    }

    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public List<Goal> actionGetListGoalCard() throws  IOException{
        return rmi_controller.getTtoP().get(token).getInitial_goal_cards();
    }

    @Override
    public void action() throws IOException {
        List<Goal> list_goal_card = this.actionGetListGoalCard();
        ResponseMessage s = new getListGoalCardResponse(list_goal_card);
        clientHandler.sendMessage(s);
    }
}
