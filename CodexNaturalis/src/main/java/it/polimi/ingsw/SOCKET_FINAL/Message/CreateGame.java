package it.polimi.ingsw.SOCKET_FINAL.Message;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.ClientHandler;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Represents a message sent by the client to create a new game.
 * This message contains the desired game name, number of players, and player's name.
 */
public class CreateGame implements Message, Serializable {

    public ServerSocket serverSocket;
    public String token;
    public String name_p;
    ObjectOutputStream output;
    public String game_name;
    int num_players;

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

    public void setToken(String token){
        this.token = token;
    }

    public CreateGame(String game_name, int num_players, String name_p ){
        this.name_p = name_p;
        this.num_players = num_players;
        this.game_name = game_name;
    }

    public void setServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void setOutput(ObjectOutputStream output) {
        this.output = output;
    }

    /**
     * Sends the create game request to the common serverSocket through RMI and retrieves
     * the assigned port for the game.
     *
     * @throws IOException If there is an IO error.
     * @throws InterruptedException If the thread is interrupted while waiting.
     * @throws ClassNotFoundException If a class used in the RMI call cannot be found.
     * @return The assigned port for the game, or -1 if an error occurs.
     */
    public int actionCreateGameMessage() throws IOException, InterruptedException, ClassNotFoundException {
        int port;
        port = common.createGame(game_name,num_players,token,name_p,clientHandler);
        return port;
    }

    /**
     *
     */
    @Override
    public void action() throws IOException, InterruptedException, ClassNotFoundException, NotBoundException {
        clientHandler.startCheckingMessages();
        int port = this.actionCreateGameMessage();
        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
        clientHandler.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
        clientHandler.startSendingHeartbeats();
    }

}
