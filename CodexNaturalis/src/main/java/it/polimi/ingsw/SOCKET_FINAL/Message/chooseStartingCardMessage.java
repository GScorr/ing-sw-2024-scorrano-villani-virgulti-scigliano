package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class chooseStartingCardMessage implements Message, Serializable {

    public String token;
    public boolean flipped;


    public Server server;

    ObjectOutputStream output;

    public Common_Server common;

    public VirtualGameServer rmi_controller;


    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public chooseStartingCardMessage(boolean flipped) {
        this.flipped = flipped;
    }



    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void action() throws IOException {
        rmi_controller.chooseStartingCard(token,flipped);
    }

}
