package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GetTokenMessage implements Message, Serializable {
    public Controller controller;
    public Server server;
    public String name;
    ObjectOutputStream output;

    public GetTokenMessage(String name){
        this.name = name;
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

    @Override
    public void action() throws IOException {
        if(controller == null || server == null){
            server.broadcastUpdate("Qualcosa è andato storto nell'invio del messaggio, controller == null || server == null");

        }

        String token = server.token_manager.generateToken(name);

        MyMessageFinal response = new MyMessageFinal(token);

        output.writeObject(response);
        output.flush();
    }
}
