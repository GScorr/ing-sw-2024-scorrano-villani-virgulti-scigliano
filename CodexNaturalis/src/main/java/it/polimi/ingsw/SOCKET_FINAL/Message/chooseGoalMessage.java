package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class chooseGoalMessage implements Message, Serializable {

    public String token;
    public Integer intero;

    public Controller controller;
    public Server server;

    ObjectOutputStream output;
    boolean check;


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
    public chooseGoalMessage(String token, Integer intero){
        this.token = token;
        this.intero = intero;
    }


    @Override
    public void action() throws IOException {


    }
}
