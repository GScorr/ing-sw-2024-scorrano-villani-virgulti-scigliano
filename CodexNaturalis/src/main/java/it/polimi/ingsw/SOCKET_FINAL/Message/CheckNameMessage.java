package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;

public class CheckNameMessage implements Message, Serializable {

    public Server server;
    VirtualServerF rmi_server;
    ObjectOutputStream output;


    public String nome;

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
    public void setRmiServer(VirtualServerF rmi_server) {
        this.rmi_server = rmi_server;
    }

    @Override
    public void setToken(String token) {

    }

    public String checkNameMessageAction() throws IOException {
        String isnew = rmi_server.checkName(nome);
        return isnew;
    }

    @Override
    public void action() throws IOException {
    }

}
