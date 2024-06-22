package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.freeGamesResponse;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.Server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a message sent by the client to request a list of available (free to join) game lobbies.
 */
public class GetFreeGames implements Message, Serializable {

    public Server server;
    public String token;
    ObjectOutputStream output;
    public Common_Server common;

    public VirtualGameServer rmi_controller;

    private ClientHandler clientHandler;
    @Override
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void setRmiController(VirtualGameServer rmi_controller) {
        this.rmi_controller = rmi_controller;
    }

    public void setCommonServer(Common_Server common){
        this.common = common;
    }

    public GetFreeGames(){

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

    /**
     * Retrieves the list of available game lobbies using the getFreeGamesSocket() method
     * of the common server and sends a response message containing the list to the client
     * through the output stream.
     *
     * @throws IOException If there is an IO error.
     */
    @Override
    public void action() throws IOException {
        List<SocketRmiControllerObject> games = common.getFreeGamesSocket();
        if( server == null){
            System.out.println("error");
            return;
        }
        ResponseMessage s = new freeGamesResponse(games);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

}
