package it.polimi.ingsw.SOCKET_FINAL.Message;


import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.Serializable;

public class LunchMessageMessage implements Message, Serializable {

    public Controller controller;
    public Server server;
    public String message, token;
    public LunchMessageMessage(String message, String token){
        this.message = message;
        this.token = token;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void action() {
        if(server.token_map.containsKey(token)){
            try{
                controller.lunchMessage(message);
                this.server.broadcastUpdate(this.controller.getMessage());
            }catch (ControllerException e){
                this.server.broadcastUpdate(e.getMessage());
            }
        }else{
            this.server.broadcastUpdate("Non hai le autorizzazioni per procedere, crea il tuo giocatore");
        }
    }
}
