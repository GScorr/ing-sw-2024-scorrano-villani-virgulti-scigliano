package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class receiveHeartbeatMessage implements Message, Serializable {

    public String token;


    public Controller controller;
    public Server server;

    ObjectOutputStream output;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public receiveHeartbeatMessage(String token) {
        this.token = token;
    }

    @Override
    public void action() throws IOException {

    }

}
