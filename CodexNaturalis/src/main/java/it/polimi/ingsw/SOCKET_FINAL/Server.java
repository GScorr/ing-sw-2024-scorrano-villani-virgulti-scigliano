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


public class Server extends UnicastRemoteObject  {

    final ServerSocket listenSocket;

    final List<ClientHandler> clients = new ArrayList<>();

    public ArrayList<String> names = new ArrayList<>();

    public TokenManager token_manager = new TokenManager();

    public Common_Server common;

    public Server(ServerSocket listenSocket, Common_Server common) throws IOException {
        this.common = common;
        this.listenSocket = listenSocket;
       // common.startHeartbeatChecker();
    }

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


    public void broadcastUpdate(String value) {
        synchronized (this.clients) {
            for (var client : this.clients) {
                client.showValue(value);
            }
        }
    }


    public static void main(String[] args) throws IOException, NotBoundException {
       /* // Server Socket
        String host = "127.0.0.1";
        int port = 12345;

        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("Common_Server is running...");

        //server Socket connect to ServerRmi as a Client
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1);
        VirtualServerF serverRmi = (VirtualServerF) registry.lookup("VirtualServer");


        new Server(listenSocket,serverRmi).runServer();*/
    }

    public MiniModel getMiniModel() throws IOException{
        return null;
    }
}
