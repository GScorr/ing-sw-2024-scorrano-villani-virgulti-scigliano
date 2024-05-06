package it.polimi.ingsw.SOCKET_FINAL.Message;

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


    public Controller controller;
    public Server server;

    ObjectOutputStream output;

    public VirtualServerF rmi_server;

    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public showStartingCardMessage(String token) {
        this.token = token;
    }

    @Override
    public void action() throws IOException {

    }
}
