package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.Serializable;

public class GetTokenMessage implements Message, Serializable {
    public Controller controller;
    public Server server;


    public VirtualView client;
    public GetTokenMessage(VirtualView client){
        this.client = client;
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
        * CODICE DA ESEGUIRE IN SERVER
        *
        *
        * */

    }
}
