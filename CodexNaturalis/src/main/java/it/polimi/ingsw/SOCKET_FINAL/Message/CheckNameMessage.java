package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.Serializable;

public class CheckNameMessage implements Message, Serializable {
    public Controller controller;
    public Server server;

    public String nome;
    public CheckNameMessage(String nome){
        this.nome = nome;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void action() {
        if(controller == null || server == null){
            server.broadcastUpdate("Qualcosa è andato storto nell'invio del messaggio, controller == null || server == null");
            return;
        }

        /*
        * CODICE DA ESEGUIRE IN CLIENT_HANDLER PER L'ESECUZIONE
        *
        *
        * */

    }
}
