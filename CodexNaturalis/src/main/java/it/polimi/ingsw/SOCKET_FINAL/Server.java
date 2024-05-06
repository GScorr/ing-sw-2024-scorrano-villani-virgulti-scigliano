package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.RMI.TokenManagerImplement;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;
import it.polimi.ingsw.SOCKET_FINAL.TokenManager.TokenManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Server extends UnicastRemoteObject implements VirtualViewF {

    final ServerSocket listenSocket;
    VirtualServerF serverRmi;

    final List<ClientHandler> clients = new ArrayList<>();

    public HashMap<String, Giocatore> token_map = new HashMap<>();

    public ArrayList<String> names = new ArrayList<>();

    public TokenManager token_manager = new TokenManager();

    public Server(ServerSocket listenSocket, VirtualServerF serverRmi) throws RemoteException {
        this.listenSocket = listenSocket;
        this.serverRmi = serverRmi;
    }

    public void runServer() throws IOException, NotBoundException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            System.out.println("Common_Client connected: " + clientSocket.getInetAddress());

            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());


            ClientHandler handler = new ClientHandler(this, inputStream, outputStream, serverRmi);

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
        // Server Socket
        String host = "127.0.0.1";
        int port = 12345;

        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("Common_Server is running...");

        //server Socket connect to ServerRmi as a Client
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1);
        VirtualServerF serverRmi = (VirtualServerF) registry.lookup("VirtualServer");


        new Server(listenSocket,serverRmi).runServer();
    }

    @Override
    public void showUpdate(GameField game_field) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {

    }

    @Override
    public void reportMessage(String details) throws RemoteException {

    }

    @Override
    public void showCard(PlayCard card) throws RemoteException {

    }

    @Override
    public void showField(GameField field) throws RemoteException {

    }

    @Override
    public void printString(String s) throws RemoteException {

    }
}
