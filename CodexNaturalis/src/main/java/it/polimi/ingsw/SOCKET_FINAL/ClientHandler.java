package it.polimi.ingsw.SOCKET_FINAL;


import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Message.Message;


import java.io.*;

public class ClientHandler implements VirtualView {

    final Controller controller;
    final Server server;
    final ObjectInputStream input;
    final ObjectOutputStream output;
    final VirtualView view;



    public ClientHandler(Controller controller, Server server, ObjectInputStream input, ObjectOutputStream output) {
        this.controller = controller;
        this.server = server;
        this.input = input;
        this.output = output;
        this.view = new ClientProxy(output);
    }

    public void runVirtualView() throws IOException, ClassNotFoundException {
        synchronized (this) {
            try {
                Message DP_message = null;
                // Read message type
                while ((DP_message = (Message) input.readObject()) != null) {
                    DP_message.setController(controller);
                    DP_message.setServer(server);
                    DP_message.setOutput(output);
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
         //   this.view.showValue(message);
        }
    }

    @Override
    public void reportError(String details) {
        synchronized (this) {
         //   this.view.reportError(details);
        }
    }


}
