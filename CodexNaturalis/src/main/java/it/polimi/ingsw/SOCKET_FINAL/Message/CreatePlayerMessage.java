package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.RMI_FINAL.VirtualRmiController;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CreatePlayerMessage implements Message, Serializable {

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

    public String nome,token;
    public CreatePlayerMessage(String nome){
        this.nome = nome;

    }



    public void setServer(Server server) {
        this.server = server;
    }
    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public void action() {

    }
}
