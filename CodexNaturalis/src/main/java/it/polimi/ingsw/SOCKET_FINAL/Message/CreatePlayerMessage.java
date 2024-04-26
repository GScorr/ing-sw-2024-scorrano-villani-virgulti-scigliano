package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;
import it.polimi.ingsw.SOCKET.ServerS;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.Serializable;

public class CreatePlayerMessage implements Message, Serializable {
    public Controller controller;
    public Server server;

    public String nome,token;
    public CreatePlayerMessage(String nome, String token){
        this.nome = nome;
        this.token = token;
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
        if(server.token_map.containsKey(token)){
            server.broadcastUpdate("Gicatore già creato impossibile crearne un altro");
        }else{

        try {
            Giocatore tmp = controller.inserisciGiocatore(nome);
            server.token_map.put(token, tmp);
            server.broadcastUpdate("Giocatore " + tmp.getName() + " " + " entrato nella Lobby");
        }catch (ControllerException e){
            server.broadcastUpdate(e.getMessage());
        }
        }

    }
}
