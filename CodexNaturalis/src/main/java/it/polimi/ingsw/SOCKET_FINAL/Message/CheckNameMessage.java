package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.checkNameResponse;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Represents a message sent by the client to check if a username is available.
 * This message contains the proposed username and is received by the serverSocket.
 */
public class CheckNameMessage implements Message, Serializable {

    public ServerSocket serverSocket;
    public Common_Server common;
    ObjectOutputStream output;


    public String nome;
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


    boolean check;
    public CheckNameMessage(String nome){
        this.nome = nome;
    }


    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public  void setCommonServer(Common_Server common){
        this.common = common;
    }

    @Override
    public void setToken(String token) {

    }
/*
da eliminare
    public String checkNameMessageAction() throws IOException, InterruptedException {
        String isnew = common.checkName(nome,null);
        return isnew;
    }

 */

    /**
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException, InterruptedException, NotBoundException {

        String mayToken =  common.checkName(nome,clientHandler);
        if (mayToken.equals("true")) {
            clientHandler.name = this.nome;
            clientHandler.token = common.createToken(clientHandler);
            ResponseMessage s = new checkNameResponse(1);
            clientHandler.sendMessage(s);
        }
        else if (mayToken.equals("false")) {
            ResponseMessage s = new checkNameResponse(0);
            clientHandler.sendMessage(s);
        }
        else {

            clientHandler.token = mayToken;
            clientHandler.startCheckingMessages();
            int port = common.getPort(clientHandler.token);
            Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
            clientHandler.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
            clientHandler.client_is_connected = true;
            clientHandler.startSendingHeartbeats();
            ResponseMessage s = new checkNameResponse(2);
            clientHandler.sendMessage(s);
        }

    }

}
