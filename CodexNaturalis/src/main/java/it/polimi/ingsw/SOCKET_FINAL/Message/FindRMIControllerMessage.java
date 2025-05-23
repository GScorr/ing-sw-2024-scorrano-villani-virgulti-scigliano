package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.CheckRmiResponse;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to find a specific RMI game serverSocket controller.
 * This message likely contains an identifier for the desired controller and the player's information.
 */
public class FindRMIControllerMessage implements Message, Serializable {

    public Integer id;

    public String player_name;
    public  String token;
    public ServerSocket serverSocket;

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
    public void setToken(String token) {
        this.token = token;
    }



    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public FindRMIControllerMessage(Integer id, String player_name) {
        this.id = id;

        this.player_name = player_name;
    }

    /**
     * Sends a request to the common serverSocket to find the RMI game serverSocket controller
     * with the specified identifier. It also provides the player's token and name
     * for authentication and association. Returns true if the controller is found,
     * false otherwise.
     *
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public boolean actionFindRmi() throws IOException, InterruptedException {
        return common.findRmiController(id,token,player_name,clientHandler);
    }

    @Override
    public void action() throws IOException, InterruptedException {
        if (this.actionFindRmi()) {
            ResponseMessage s = new CheckRmiResponse(true);
            clientHandler.sendMessage(s);
        }
        else {
            ResponseMessage s = new CheckRmiResponse(false);
            clientHandler.sendMessage(s);
        }
    }

}
