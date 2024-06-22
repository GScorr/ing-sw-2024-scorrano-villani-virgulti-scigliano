package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Represents a message sent by the client to join a game.
 * This message contains the player's name, desired game name, and number of players.
 */
public class connectGame implements Message, Serializable {

    public Server server;
    public String token;
    public String name_p;
    ObjectOutputStream output;
    public String game_name;
    int num_players;

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

    public void setToken(String token){
        this.token = token;
    }

    public connectGame(){

    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
    /*
    da eliminare
    public void actionConnectGame() throws IOException {
        this.rmi_controller.connectSocket(this.clientHandler);
    }

     */

    @Override
    public void action() throws IOException, NotBoundException {
        clientHandler.startCheckingMessages();
        int port = clientHandler.common.getPort(token);
        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
        clientHandler.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
        clientHandler.startSendingHeartbeats();
    }

}
