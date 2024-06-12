package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.getCardsInCenterResponse;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a message sent by the client to request the list of cards currently in the center of the game.
 */
public class getCardsInCenter implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }

    public getCardsInCenter(){

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
     * Sends a request to the RMI controller to retrieve the list of cards in the center
     * using the showCardsInCenter() method.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        rmi_controller.showCardsInCenter(token);
        ResponseMessage message = new getCardsInCenterResponse();
        output.writeObject(message);
        output.flush();
        output.reset();
    }
}
