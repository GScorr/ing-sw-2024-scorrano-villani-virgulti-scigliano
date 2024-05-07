package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.RMI_FINAL.VirtualRmiController;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class getListGoalCard implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public VirtualServerF rmi_server;
    public VirtualRmiController rmi_controller;


    @Override
    public void setRmiController(VirtualRmiController rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }


    public getListGoalCard(){

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
        List<Goal> list_goal_card = rmi_controller.getTtoP().get(token).getInitial_goal_cards();
        output.writeObject(list_goal_card);
        output.flush();

    }
}
