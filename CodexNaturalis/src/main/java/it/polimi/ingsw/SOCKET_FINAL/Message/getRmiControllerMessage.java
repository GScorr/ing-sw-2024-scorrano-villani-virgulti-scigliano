package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class getRmiControllerMessage implements Message, Serializable {

    public String token;



    public Server server;

    public VirtualServerF rmi_server;
    public VirtualGameServer rmi_controller;


    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }


    ObjectOutputStream output;

    public void setToken(String token) {
        this.token = token;
    }



    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
    public getRmiControllerMessage() {

    }

    @Override
    public void action() throws IOException {

    }

}
