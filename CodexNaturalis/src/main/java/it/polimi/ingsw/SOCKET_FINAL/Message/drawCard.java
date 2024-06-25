package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.placeCardResponse;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to request drawing a card.
 * This message includes a flag indicating whether the player wants to see
 * the card (flipped) or keep it hidden.
 */
public class drawCard implements Message, Serializable {

    public ServerSocket serverSocket;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;
    SendFunction function;

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

    public drawCard(SendFunction function) {
        this.function = function;
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
     * Adds the draw card function object
     * to the RMI queue, triggering the card drawing logic on the serverSocket-side.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        rmi_controller.addQueue(function);
        ResponseMessage message = new placeCardResponse();
        output.writeObject(message);
        output.flush();
        output.reset();
    }

}
