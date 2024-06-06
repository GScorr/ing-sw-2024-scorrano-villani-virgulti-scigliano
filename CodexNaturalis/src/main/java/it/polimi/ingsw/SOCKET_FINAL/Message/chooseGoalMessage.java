package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class chooseGoalMessage implements Message, Serializable {

    public String token;
    public Integer intero;
    public int index;

    public Server server;

    ObjectOutputStream output;
    boolean check;
    public Common_Server common;
    public VirtualGameServer rmi_controller;

    public chooseGoalMessage(int index) {
        this.index = index;
    }

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }
    @Override
    public void setToken(String token) {
        this.token = token;
    }




    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public chooseGoalMessage( Integer intero){
        this.intero = intero;
    }


    @Override
    public void action() throws IOException, InterruptedException {
        rmi_controller.chooseGoal(token,index);

    }
}
