package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to check if a username is available.
 * This message contains the proposed username and is received by the server.
 */
public class CheckNameMessage implements Message, Serializable {

    public Server server;
    public Common_Server common;
    ObjectOutputStream output;


    public String nome;
    public VirtualGameServer rmi_controller;


    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
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
/*
da eliminare
    public String checkNameMessageAction() throws IOException, InterruptedException {
        String isnew = common.checkName(nome,null);
        return isnew;
    }

 */

    /**
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
    }

}
