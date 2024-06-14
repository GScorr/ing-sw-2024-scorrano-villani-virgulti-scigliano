package it.polimi.ingsw.RMI_FINAL.MESSAGES;

import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.clientSocket;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.util.List;

/**
 * Abstract base class for response messages sent from the server to the client.
 *
 * This abstract class defines the foundation for messages sent by the server
 * in response to client actions. Subclasses represent specific types of responses
 * with their own data and functionalities.

 */
public abstract class ResponseMessage implements Serializable {

    public clientSocket client;
    public VirtualViewF virtual_view;
    List<SocketRmiControllerObject> free_games_Socket;

    public void setClient(clientSocket client){
        this.client = client;
    }

    public void setVirtual_view(VirtualViewF virtual_view) {
        this.virtual_view = virtual_view;
    }
    /*
da eliminare
    public void setFree_games(List<SocketRmiControllerObject> free_games) {
        this.free_games_Socket = free_games;
    }

 */
    /**
     * Abstract method for defining the action to be performed by the client
     * upon receiving the message.
     *
     * @throws IOException  if an I/O error occurs while performing the action
     */
    public void action() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException {}

}
