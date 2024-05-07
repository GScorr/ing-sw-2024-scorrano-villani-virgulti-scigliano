package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class chooseStartingCardMessage implements Message, Serializable {

    public String token;
    public boolean check;


    public Server server;

    ObjectOutputStream output;

    public VirtualServerF rmi_server;

    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public chooseStartingCardMessage(boolean check) {
        this.check = check;
    }



    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void action() throws IOException {

    }

}
