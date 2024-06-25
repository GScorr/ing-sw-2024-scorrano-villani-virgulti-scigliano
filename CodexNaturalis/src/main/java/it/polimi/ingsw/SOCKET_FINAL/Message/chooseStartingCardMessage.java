package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to choose whether to see their starting
 * card or keep it hidden during gameplay.
 */
public class chooseStartingCardMessage implements Message, Serializable {

    public String token;
    public boolean flipped;


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

    public chooseStartingCardMessage(boolean flipped) {
        this.flipped = flipped;
    }

    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    /**
     * Sends the player's choice (see or hide starting card) to the game logic through
     * the RMI interface.
     *
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ClassNotFoundException If a class used in the RMI call cannot be found.
     */
    @Override
    public void action() throws IOException, InterruptedException, ClassNotFoundException {
        rmi_controller.chooseStartingCard(token,flipped);
    }

}
