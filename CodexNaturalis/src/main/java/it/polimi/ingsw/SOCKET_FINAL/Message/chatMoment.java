package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a private chat message sent by a player to another player during their turn
 * in the game. This message also includes the player's decision (related to gameplay)
 * and is received by the server and forwarded to the game logic
 */
public class chatMoment implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;
    int myIndex;
    int decision;
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

    public chatMoment(int index ,int  decision , ChatMessage chatMessage) {
        this.myIndex = index;
        this.decision = decision;
        this.chatMessage = chatMessage;
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
     *
     * @throws IOException
     */
    @Override
    public void action() throws IOException {
        rmi_controller.chattingMoment(myIndex, decision, chatMessage);
    }
}
