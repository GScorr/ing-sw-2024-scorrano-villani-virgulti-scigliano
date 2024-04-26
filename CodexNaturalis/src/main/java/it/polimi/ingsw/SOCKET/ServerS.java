package it.polimi.ingsw.SOCKET;

import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * errore in client non leggo da input il messaggio o il nome giocatore
 */

public class ServerS {

    final ServerSocket listenSocket;
    final Controller controller;
    final List<ClientHandlerS> clients = new ArrayList<>();

    public HashMap<String, Giocatore> token_map = new HashMap<>();

    public ServerS(ServerSocket listenSocket, Controller controller) {
        this.listenSocket = listenSocket;
        this.controller = controller;
    }

    private void runServer() throws IOException {
        Socket clientSocket = null;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            ClientHandlerS handler = new ClientHandlerS(this.controller, this, new BufferedReader(socketRx), new BufferedWriter(socketTx));

            clients.add(handler);
            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    throw new RuntimeException(e); //possibile errore que
                }
            }).start();
        }
    }

    /**
     * volendo si può gestire per aggiornare tutti i client che un player entra
     */
 /*   public void messagggio(){ //prende il messaggio dal controller e lo invio ai client
        broadcastUpdate(controller.getMessage());
        synchronized (this.clients){
            for(var client : this.clients){
                client.showValue(value);
            }
        }
    }

  */

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
