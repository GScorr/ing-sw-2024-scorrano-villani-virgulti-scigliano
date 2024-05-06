package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class GetFreeGames implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public VirtualServerF rmi_server;

    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }


    public GetFreeGames(){

    }

    public void setToken(String token) {
        this.token = token;
    }



    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void action() throws IOException {

        List<SocketRmiControllerObject> games = rmi_server.getFreeGamesSocket();

        if( server == null){
            System.out.println("error");
            return;
        }


        output.writeObject(games);
        output.flush();
    }
}
