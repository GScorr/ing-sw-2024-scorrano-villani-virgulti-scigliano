package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FindRMIControllerMessage implements Message, Serializable {

    public Integer id;
    public String p_token;
    public String player_name;


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

    public FindRMIControllerMessage(Integer id, String p_token, String player_name) {
        this.id = id;
        this.p_token = p_token;
        this.player_name = player_name;
    }

    @Override
    public void action() throws IOException {

    }

}
