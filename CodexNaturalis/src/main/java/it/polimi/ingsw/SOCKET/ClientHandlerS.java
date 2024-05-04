package it.polimi.ingsw.SOCKET;
/*
import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET.GiocoProva.Giocatore;
import it.polimi.ingsw.SOCKET.Message.CreatePlayerMessage;
import it.polimi.ingsw.SOCKET.Message.Message;

import java.io.*;
import java.net.ConnectException;

public class ClientHandlerS implements VirtualViewS{

    final Controller controller;
    final ServerS server;
    final ObjectInputStream input;
    final VirtualViewS view;


    public ClientHandlerS(Controller controller, ServerS server, ObjectInputStream input, BufferedWriter output) {
        this.controller = controller;
        this.server = server;
        this.input = input;
        this.view = new ClientProxyS(output);
    }

    public void runVirtualView() throws IOException, ClassNotFoundException {
        synchronized (this) {
            try {
                Message DP_message = null;
                // Read message type
                while ((DP_message = (Message) input.readObject()) != null) {
                    DP_message.setController(controller);
                    DP_message.setServer(server);
                    DP_message.action();
                }
            } catch (EOFException e) {
                // EOFException viene sollevata quando si raggiunge la fine del flusso di input
                //System.out.println("Fine del flusso di input");
            } catch (ClassNotFoundException | IOException e) {
                // Gestione generica delle eccezioni durante la deserializzazione
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showValue(String message) {
        synchronized (this) {
            this.view.showValue(message);
        }
    }

    @Override
    public void reportError(String details) {
        synchronized (this) {
            this.view.reportError(details);
        }
    }


}

 */
