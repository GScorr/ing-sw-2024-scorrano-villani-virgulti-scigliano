package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A class representing a message sent from the client to the server
 * requesting a card to be drawn from the center decks.
 *
 */
public class peachFromCardsInCenter implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;
    public int index;

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

    public peachFromCardsInCenter(int index) {
        this.index = index;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    /**
     * Sends a request to the RMI controller to draw a card from the center decks
     * based on the specified index.
     *
     * @throws IOException if an I/O error occurs while sending the message
     */
    @Override
    public void action() throws IOException {
        SendFunction function = new SendDrawCenter(token, index);
        rmi_controller.addQueue(function);
    }
}
