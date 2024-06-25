package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to choose a goal during gameplay.
 * This message contains the player's token and the chosen goal index.
 */
public class chooseGoalMessage implements Message, Serializable {

    public String token;
    public Integer intero;
    public int index;

    public ServerSocket serverSocket;

    ObjectOutputStream output;
    boolean check;
    public Common_Server common;
    public VirtualGameServer rmi_controller;

    private ClientHandler clientHandler;
    @Override
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public chooseGoalMessage(int index) {
        this.index = index;
    }

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }
    @Override
    public void setToken(String token) {
        this.token = token;
    }




    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
/*
da eliminare
    public chooseGoalMessage( Integer intero){
        this.intero = intero;
    }

 */

    /**
     * Sends the chosen goal information (player token and index) to the game logic
     * through the RMI interface.
     *
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ClassNotFoundException If a class used in the RMI call cannot be found.
     */
    @Override
    public void action() throws IOException, InterruptedException, ClassNotFoundException {
        rmi_controller.chooseGoal(token,index);

    }
}
