package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Server {

    final ServerSocket listenSocket;
    final Controller controller;
    final List<ClientHandler> clients = new ArrayList<>();

    public HashMap<String, Giocatore> token_map = new HashMap<>();

    public Server(ServerSocket listenSocket, Controller controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
    }

    private void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            ClientHandler handler = new ClientHandler(this.controller, this, inputStream, new BufferedWriter(socketTx));

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

        new Server(listenSocket, new Controller()).runServer();
    }

}
