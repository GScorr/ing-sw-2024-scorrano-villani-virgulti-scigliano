package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Represents a message sent by the client to check if they have placed their first card.
 */
public class firstCardIsPlaced implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;
    public VirtualGameServer rmi_controller;


    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common ){
        this.common = common;
    }

    public firstCardIsPlaced(){}

    public void setToken(String token) {
        this.token = token;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    /**
     * Queries the RMI controller to check if the player associated with the token
     * has placed their first card. Returns true if the player has placed their first card,
     * false otherwise.
     *
     * @throws IOException If there is an IO error.
     */
    public boolean firstCardIsPlacedAction() throws IOException{
        return rmi_controller.getTtoP().get(token).isFirstPlaced();
    }

    /**
     * Checks if the player has placed their first card using firstCardIsPlacedAction()
     * and sends a corresponding message ("true" or "false") to the client through
     * the output stream.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        boolean is_places = rmi_controller.getTtoP().get(token).isFirstPlaced();
        MyMessageFinal message;
        if(is_places){
             message = new MyMessageFinal("true");
        }else{
             message = new MyMessageFinal("false");
        }
        output.writeObject(message);
        output.flush();
        output.reset();
    }

}
