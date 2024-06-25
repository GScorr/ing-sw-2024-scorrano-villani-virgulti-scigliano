package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
forse da eliminare
 */

/**
 * This message might be suitable for removal depending on the game's design
 * and how the game field information is supposed to be retrieved and presented
 * to the client.
 */
public class getGameField implements Message, Serializable {

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

    public getGameField(){

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

    /**
     * Retrieves the game field object from the RMI controller and sends it
     * to the client.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        GameField game_field = rmi_controller.getTtoP().get(token).getGameField();
        output.writeObject(game_field);
        output.flush();
        output.reset();
    }

}
