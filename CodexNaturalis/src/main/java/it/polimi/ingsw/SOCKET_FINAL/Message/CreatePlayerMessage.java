package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CreatePlayerMessage implements Message, Serializable {

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
