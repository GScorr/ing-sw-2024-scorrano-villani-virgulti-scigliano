package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.CheckGoldDeckSize;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to check if the gold deck in the game has any cards remaining.
 */
public class getGoldDeckSize implements Message, Serializable {

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

    public getGoldDeckSize(){}

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
     * Retrieves the size of the gold deck from the RMI controller and sends
     * a response message indicating whether the deck has any cards remaining.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        int size = rmi_controller.getController().getGame().getGold_deck().getNumber();
        boolean check;
        if(size > 0 ) check = true;
        else check = false;
        ResponseMessage s = new CheckGoldDeckSize(check);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

}
