package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.Server;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CreateGame implements Message, Serializable {

    public Server server;
    public String token;
    public String name_p;
    ObjectOutputStream output;
    public String game_name;
    int num_players;

    public Common_Server common;
    public VirtualGameServer rmi_controller;

    public VirtualViewF clientHandler;

    public void setClientHandler(VirtualViewF clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
            this.common = common;
    }

    public void setToken(String token){
        this.token = token;
    }

    public CreateGame(String game_name, int num_players, String name_p ){
        this.name_p = name_p;
        this.num_players = num_players;
        this.game_name = game_name;
    }




    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
    public int actionCreateGameMessage() throws IOException, InterruptedException, ClassNotFoundException {
        int port;
        //port = common.createGameSocket(game_name,num_players,token,name_p, clientHandler);
        port = common.createGame(game_name,num_players,token,name_p,clientHandler);
        return port;
    }

    @Override
    public void action() {
        if( server == null){
            System.out.println("Qualcosa è andato storto nell'invio del messaggio, controller == null || server == null");
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
