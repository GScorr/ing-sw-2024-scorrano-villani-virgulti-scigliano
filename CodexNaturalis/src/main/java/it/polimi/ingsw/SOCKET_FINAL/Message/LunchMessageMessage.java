package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
da eliminare probabilmente
 */
public class LunchMessageMessage implements Message, Serializable {

    public ServerSocket serverSocket;
    public String message, token;
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

    public LunchMessageMessage(String message){
        this.message = message;

    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void action() {

    }
}
