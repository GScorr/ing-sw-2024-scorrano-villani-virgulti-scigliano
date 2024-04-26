package it.polimi.ingsw.SOCKET;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ConnectException;

public class ClientHandlerS implements VirtualViewS{

    final Controller controller;
    final ServerS server;
    final BufferedReader input;
    final VirtualViewS view;


    public ClientHandlerS(Controller controller, ServerS server, BufferedReader input, BufferedWriter output) {
        this.controller = controller;
        this.server = server;
        this.input = input;
        this.view = new ClientProxyS(output);
    }

    public void runVirtualView() throws IOException {
        String line;
        // Read message type
        while ((line = input.readLine()) != null) { //errore nella riga che viene letta, tutte le riche che vengono lette vengono passate al server come riga di controllo
            System.out.println("riga letta:"+line);
            // Read message and perform action
            switch (line) {
                case "Inserisci_Giocatore" ->{
                    try{
                        String nome = input.readLine();
                        String token = input.readLine();
                        if(server.token_map.containsKey(token)){ //se il giocatore esiste gia
                            return;
                        }
                        else{
                            Giocatore tmp = controller.inserisciGiocatore(nome);
                            server.token_map.put(token, tmp);
                            /**
                             * non chiamo nessuna funzione dal server
                             */
                        }

                    }
                    catch (ControllerException e){
                        System.out.println("errore");
                    }
                }
                case "inserisci messaggio" ->{
                    String message = input.readLine();
                    String token = input.readLine();
                    /**
                     * nel nostro caso
                     * Giocatore tmp = server.token_map.get(token);
                     * nel nostro al posto del token deve arrivre il player
                     */
                    controller.lunchMessage(message);
                    this.server.broadcastUpdate(this.controller.getMessage()); //aggiorno tutti i client
                }
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    @Override
    public void showValue(String message) {
        synchronized (this.view) {
            this.view.showValue(message);
        }
    }

    @Override
    public void reportError(String details) {
        synchronized (this.view) {
            this.view.reportError(details);
        }
    }


}
