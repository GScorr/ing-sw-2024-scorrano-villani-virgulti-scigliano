package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CreateGame implements Message, Serializable {
    public Controller controller;
    public Server server;
    public String token;
    public String nome;
    ObjectOutputStream output;
    public String game_name;
    int num_players;

    public VirtualServerF rmi_server;

    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }


    public CreateGame(String game_name, int num_players,String token,String nome ){
        this.nome = nome;
        this.token = token;
        this.num_players = num_players;
        this.game_name = game_name;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void setOutput(ObjectOutputStream output) {
        this.output = output;
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
