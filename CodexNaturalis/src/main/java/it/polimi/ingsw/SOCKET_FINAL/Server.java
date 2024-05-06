package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.RMI.TokenManagerImplement;
import it.polimi.ingsw.RMI_FINAL.RmiServerF;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;
import it.polimi.ingsw.SOCKET_FINAL.TokenManager.TokenManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Server {

    final ServerSocket listenSocket;
    final List<ClientHandler> clients = new ArrayList<>();

    public HashMap<String, Giocatore> token_map = new HashMap<>();

    public ArrayList<String> names = new ArrayList<>();

    public TokenManager token_manager = new TokenManager();

    final VirtualServerF rmi_server;

    public Server(ServerSocket listenSocket, VirtualServerF rmi_server) {
        this.listenSocket = listenSocket;
        this.rmi_server = rmi_server;
    }

    public void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Client connected: " + clientSocket.getInetAddress());

            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            //OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            ClientHandler handler = new ClientHandler(this, inputStream, outputStream, rmi_server);

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


    public void broadcastUpdate(String value) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showValue(value);
            }
        }
    }


    public static void main(String[] args) throws IOException, NotBoundException {
        String host = "127.0.0.1";
        int port = 12345;


        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("Server is running...");
        //connection RMI SERVER (LOBBY)
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        VirtualServerF rmi_server = (VirtualServerF) registry.lookup("VirtualServer");
        //creation of SERVER SOCKET
        new Server(listenSocket,rmi_server).runServer();
    }

}
