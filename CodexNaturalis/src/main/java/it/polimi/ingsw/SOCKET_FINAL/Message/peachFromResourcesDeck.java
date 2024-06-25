package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A class representing a message sent from the client to the serverSocket
 * requesting a card to be drawn from the resource deck.
 *
 */
public class peachFromResourcesDeck implements Message, Serializable {

    public ServerSocket serverSocket;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;
    public int index;
    public int x;
    public int y;
    public boolean flipped;

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

    public peachFromResourcesDeck() {

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
     * Sends a request to the RMI controller to draw a card from the resource deck.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void action() throws IOException {
        SendFunction function = new SendDrawResource(token);
        rmi_controller.addQueue(function);
    }
}
