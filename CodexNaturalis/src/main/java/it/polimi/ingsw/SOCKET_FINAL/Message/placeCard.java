package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendInsertCard;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.placeCardResponse;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class placeCard implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;
    public int index;
    public int x;
    public int y;
    public boolean flipped;

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }

    public placeCard(int index, int x, int y, boolean flipped) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.flipped = flipped;
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
    public void action() throws IOException, ControllerException {
        SendFunction function = new SendInsertCard(token, index-1, x,y,flipped);
        rmi_controller.addQueue(function);
        ResponseMessage message = new placeCardResponse();
        output.writeObject(message);
        output.flush();
        output.reset();
    }

}
