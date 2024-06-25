package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.SOCKET_FINAL.TokenManager.TokenManager;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * A class representing the serverSocket application in the SOCKET_FINAL package.
 *
 */
public class ServerSocket extends UnicastRemoteObject  {

    final java.net.ServerSocket listenSocket;

    final List<ClientHandler> clients = new ArrayList<>();

    //da eliminare
    public ArrayList<String> names = new ArrayList<>();

    //da eliminare
    public TokenManager token_manager = new TokenManager();

    public Common_Server common;

    public ServerSocket(java.net.ServerSocket listenSocket, Common_Server common) throws IOException {
        this.common = common;
        this.listenSocket = listenSocket;
        common.startHeartbeatChecker();
    }

    /**
     * Starts the serverSocket, listens for incoming client connections, and creates separate threads to handle each client.
     *
     * @throws IOException if an I/O error occurs while accepting client connections
     * @throws NotBoundException if the RMI registry cannot be found
     */
    public void runServer() throws IOException, NotBoundException {

       Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Common_Client connected: " + clientSocket.getInetAddress());

            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ClientHandler handler = new ClientHandler(this, inputStream, outputStream,  common, clientSocket );

            clients.add(handler);
            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
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
     * Gets the serverSocket's MiniModel object.
     *
     * @return the MiniModel object (may throw IOException)
     * @throws IOException if an I/O error occurs
     */
    public MiniModel getMiniModel() throws IOException{
        return null;
    }

}
