package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.Server;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to find a specific RMI game server controller.
 * This message likely contains an identifier for the desired controller and the player's information.
 */
public class FindRMIControllerMessage implements Message, Serializable {

    public Integer id;

    public String player_name;
    public  String token;
    public Server server;

    ObjectOutputStream output;

    public Common_Server common;
    public VirtualGameServer rmi_controller;
    public VirtualViewF clientHandler;


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

    public void setClientHandler(VirtualViewF clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public FindRMIControllerMessage(Integer id, String player_name) {
        this.id = id;

        this.player_name = player_name;
    }

    /**
     * Sends a request to the common server to find the RMI game server controller
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
    public void action() throws IOException {}

}
