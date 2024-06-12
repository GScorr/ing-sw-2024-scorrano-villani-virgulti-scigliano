package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.TokenManager.TokenManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class representing the server application in the SOCKET_FINAL package.
 *
 */
public class Server extends UnicastRemoteObject  {

    final ServerSocket listenSocket;

    final List<ClientHandler> clients = new ArrayList<>();

    //da eliminare
    public ArrayList<String> names = new ArrayList<>();

    //da eliminare
    public TokenManager token_manager = new TokenManager();

    public Common_Server common;

    public Server(ServerSocket listenSocket, Common_Server common) throws IOException {
        this.common = common;
        this.listenSocket = listenSocket;
        common.startHeartbeatChecker();
    }

    /**
     * Starts the server, listens for incoming client connections, and creates separate threads to handle each client.
     *
     * @throws IOException if an I/O error occurs while accepting client connections
     * @throws NotBoundException if the RMI registry cannot be found
     */
    public void runServer() throws IOException, NotBoundException {

       Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Common_Client connected: " + clientSocket.getInetAddress());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            ClientHandler handler = new ClientHandler(this, inputStream, outputStream,  common );

            clients.add(handler);
            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param value the message to broadcast
     */
    public void broadcastUpdate(String value) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showValue(value);
            }
        }
    }

    /**
     * Gets the server's MiniModel object.
     *
     * @return the MiniModel object (may throw IOException)
     * @throws IOException if an I/O error occurs
     */
    public MiniModel getMiniModel() throws IOException{
        return null;
    }

}
