package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to register a new player.
 * This message likely contains the player's chosen name.
 */
public class CreatePlayerMessage implements Message, Serializable {

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

    public String nome,token;
    /*
    da eliminare
    public CreatePlayerMessage(String nome){
        this.nome = nome;
    }

     */

    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void action() {}
}
