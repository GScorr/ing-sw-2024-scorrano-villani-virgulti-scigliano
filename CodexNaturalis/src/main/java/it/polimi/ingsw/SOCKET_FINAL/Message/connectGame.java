package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class connectGame implements Message, Serializable {

    public Server server;
    public String token;
    public String name_p;
    ObjectOutputStream output;
    public String game_name;
    int num_players;

    public Common_Server common;
    public VirtualGameServer rmi_controller;

    public VirtualViewF clientHandler;

    public void setClientHandler(VirtualViewF clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
            this.common = common;
    }

    public void setToken(String token){
        this.token = token;
    }

    public connectGame(){

    }




    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }
    public void actionConnectGame() throws IOException {
        this.rmi_controller.connectSocket(this.clientHandler);
    }

    @Override
    public void action() {


    }
}
