package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.IOException;

public class FindRMIControllerMessage implements Message, Serializable {

    public Integer id;

    public String player_name;
    public  String token;


    public Controller controller;
    public Server server;

    ObjectOutputStream output;

    public Common_Server common;
    public VirtualGameServer rmi_controller;
    public VirtualViewF clientHandler;


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

    public void setClientHandler(VirtualViewF clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    public FindRMIControllerMessage(Integer id, String player_name) {
        this.id = id;

        this.player_name = player_name;
    }

    public boolean actionFindRmi() throws IOException {
        return common.findRmiController(id,token,player_name,clientHandler);
      // return common.findRmiControllerSocket(id,token,player_name, clientHandler);
    }

    @Override
    public void action() throws IOException {

    }

}
