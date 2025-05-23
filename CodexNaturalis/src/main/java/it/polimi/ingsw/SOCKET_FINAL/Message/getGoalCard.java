package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.checkGoalCardPresent;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to check if they have received a goal card.
 */
public class getGoalCard implements Message, Serializable {

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

    public getGoalCard(){

    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public boolean getGoalCardAction() throws IOException{
        Goal goal_card = rmi_controller.getTtoP().get(token).getGoalCard();
        if(goal_card == null){
            return false;
        }else{
            return true;
        }

    }

    /**
     * The action() method is currently empty.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        boolean isPresent = this.getGoalCardAction();
        ResponseMessage s = new checkGoalCardPresent(isPresent);
        clientHandler.sendMessage(s);
    }

}
