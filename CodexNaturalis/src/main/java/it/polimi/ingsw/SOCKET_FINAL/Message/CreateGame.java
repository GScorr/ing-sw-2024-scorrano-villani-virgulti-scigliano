package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

public class CreateGame implements Message, Serializable {

    public Server server;
    public String token;
    public String name_p;
    ObjectOutputStream output;
    public String game_name;
    int num_players;

    public VirtualServerF rmi_server;


    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
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
    public int actionCreateGameMessage() throws RemoteException {
        int port;
        port = rmi_server.createGameSocket(game_name,num_players,token,name_p);
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
