package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualRmiController;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

public class CheckNameMessage implements Message, Serializable {

    public Server server;
    public Common_Server common;
    ObjectOutputStream output;


    public String nome;
    public VirtualRmiController rmi_controller;


    @Override
    public void setRmiController(VirtualRmiController rmi_controller) {
        this.rmi_controller = rmi_controller;
    }


    boolean check;
    public CheckNameMessage(String nome){
        this.nome = nome;
    }


    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    @Override
    public  void setCommonServer(Common_Server common){
        this.common = common;
    }

    @Override
    public void setToken(String token) {

    }

    public String checkNameMessageAction() throws IOException {
        String isnew = common.checkName(nome,null);
        return isnew;
    }

    @Override
    public void action() throws IOException {
    }

}
