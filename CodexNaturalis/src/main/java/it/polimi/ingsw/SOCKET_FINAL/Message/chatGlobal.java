package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a global chat message sent by a player. This message is received by the serverSocket
 * and forwarded to the game logic.
 */
public class chatGlobal implements Message, Serializable {

    public ServerSocket serverSocket;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;
    ChatMessage chatMessage;

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

    public chatGlobal(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
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
     * Sends the global chat message to the game logic through the RMI interface.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        rmi_controller.chattingGlobal(this.chatMessage);
    }
}
