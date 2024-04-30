package it.polimi.ingsw.SOCKET;
/*
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class ServerS {

    final ServerSocket listenSocket;
    final Controller controller;
    final List<ClientHandlerS> clients = new ArrayList<>();

    public HashMap<String, Giocatore> token_map = new HashMap<>();

    public ServerS(ServerSocket listenSocket, Controller controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
    }

    public void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            ClientHandlerS handler = new ClientHandlerS(this.controller, this, inputStream, new BufferedWriter(socketTx));

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


    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = Integer.parseInt("4567");

        ServerSocket listenSocket = new ServerSocket(port);

        new ServerS(listenSocket, new Controller()).runServer();
    }

}
*/