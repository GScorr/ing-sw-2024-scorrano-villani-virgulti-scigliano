package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CheckNameMessage implements Message, Serializable {
    public Controller controller;
    public Server server;
    ObjectOutputStream output;


    public String nome;

    boolean check;
    public CheckNameMessage(String nome){
        this.nome = nome;
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
            return;
        }


        if(!server.names.contains(nome)){
            server.names.add(nome);
            MyMessageFinal response = new MyMessageFinal("TRUE");
            output.writeObject(response);
            output.flush();
        }else{
            MyMessageFinal response = new MyMessageFinal("FALSE");
            output.writeObject(response);
            output.flush();
        }

    }
}
