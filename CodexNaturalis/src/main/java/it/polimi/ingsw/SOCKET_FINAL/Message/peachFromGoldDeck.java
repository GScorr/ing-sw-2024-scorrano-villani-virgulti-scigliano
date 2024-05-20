package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class peachFromGoldDeck implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;
    public int index;
    public int x;
    public int y;
    public boolean flipped;

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }

    public peachFromGoldDeck() {

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
        SendFunction function = new SendDrawGold(token);
        rmi_controller.addQueue(function);
    }
}
