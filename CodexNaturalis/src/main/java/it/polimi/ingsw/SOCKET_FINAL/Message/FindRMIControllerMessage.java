package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.VirtualRmiController;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

public class FindRMIControllerMessage implements Message, Serializable {

    public Integer id;

    public String player_name;
    public  String token;


    public Controller controller;
    public Server server;

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
    public void setToken(String token) {
        this.token = token;
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

    public boolean actionFindRmi() throws RemoteException {
       return rmi_server.findRmiControllerSocket(id,token,player_name);
    }

    @Override
    public void action() throws IOException {

    }

}
