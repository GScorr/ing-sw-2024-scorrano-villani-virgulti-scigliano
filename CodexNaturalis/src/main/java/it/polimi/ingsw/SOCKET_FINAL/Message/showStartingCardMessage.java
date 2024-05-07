package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.VirtualRmiController;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class showStartingCardMessage implements Message, Serializable {

    /**
     * ping temporizzato per vedere se il client è ancora collegato
     * anche via rmi in modo che tutti siano a conoscenza di eventuali crush
     */

    public String token;

    public Server server;

    ObjectOutputStream output;

    public VirtualServerF rmi_server;
    public VirtualRmiController rmi_controller;


    @Override
    public void setRmiController(VirtualRmiController rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public showStartingCardMessage() {

    }

    @Override
    public void action() throws IOException {

    }
}
