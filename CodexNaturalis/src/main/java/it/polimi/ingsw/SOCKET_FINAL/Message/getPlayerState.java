package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to request the current player state (e.g., planning phase, action phase).
 */
public class getPlayerState implements Message, Serializable {

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
     * Retrieves the current player state name from the RMI controller and sends a
     * custom message (MyMessageFinal) containing the state name to the client.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        String state = rmi_controller.getTtoP().get(token).getActual_state().getNameState();
        MyMessageFinal message = new MyMessageFinal(state);
        output.writeObject(message);
        output.flush();
        output.reset();
    }
}
