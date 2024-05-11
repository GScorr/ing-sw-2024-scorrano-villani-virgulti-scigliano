package it.polimi.ingsw.SOCKET_FINAL;


import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.RMI_FINAL.VirtualRmiController;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET.GiocoProva.Controller;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;
import it.polimi.ingsw.SOCKET_FINAL.TokenManager.TokenManager;


import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ClientHandler  implements VirtualView {

    final Server server;
    final ObjectInputStream input;
    final ObjectOutputStream output;
    //final VirtualView view;

    public Common_Server common;
    public String token;

    private VirtualRmiController rmi_controller;
    public boolean client_is_connected = true;


    public ClientHandler(Server server, ObjectInputStream input, ObjectOutputStream output, Common_Server common ) throws RemoteException, NotBoundException {
        this.server = server;
        this.input = input;
        this.output = output;
       // this.view = new ClientProxy(output);
       this.common = common;

    }

    private void startSendingHeartbeats() {
        new Thread(() -> {
            while (client_is_connected) {
                try {
                    Thread.sleep(1500);
                    common.receiveHeartbeat(token);
                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void runVirtualView() throws IOException, ClassNotFoundException {
        synchronized (this) {
            try {
                Message DP_message = null;
                // Read message type
                while ((DP_message = (Message) input.readObject()) != null) {
                    if(token != null){
                        DP_message.setToken(token);
                    }
                    if(rmi_controller != null){
                        DP_message.setRmiController(this.rmi_controller);
                    }
                    DP_message.setServer(server);
                    DP_message.setOutput(output);
                    DP_message.setCommonServer(this.common);

                    if((DP_message instanceof CheckNameMessage)){
                        String mayToken = ((CheckNameMessage) DP_message).checkNameMessageAction();

                        if(mayToken.equals("true")){
                            this.token = common.createTokenSocket(((CheckNameMessage) DP_message).nome);

                        } else if (mayToken.equals("false")) {

                        } else{
                            this.token = mayToken;
                            int port = common.getPort(token);
                            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                            this.rmi_controller = (VirtualRmiController) registry.lookup(String.valueOf(port));
                            client_is_connected = true;
                            startSendingHeartbeats();
                        }


                        MyMessageFinal message = new MyMessageFinal(mayToken);
                        output.writeObject(message);
                        output.flush();

                    }else
                    if((DP_message instanceof CreateGame)){
                       int port =  ((CreateGame) DP_message).actionCreateGameMessage();
                        Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                        this.rmi_controller = (VirtualRmiController) registry.lookup(String.valueOf(port));
                        MyMessageFinal message = new MyMessageFinal("Creazione Player e Game andati a buon fine");
                        output.writeObject(message);
                        output.flush();
                        startSendingHeartbeats();
                    }
                    else if(DP_message instanceof FindRMIControllerMessage){
                       if( ((FindRMIControllerMessage)DP_message).actionFindRmi()){
                           System.out.println(token);
                           int port = common.getPort(token);
                           Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                           this.rmi_controller = (VirtualRmiController) registry.lookup(String.valueOf(port));
                           MyMessageFinal message = new MyMessageFinal("true");
                           output.writeObject(message);
                           output.flush();
                           startSendingHeartbeats();
                       }else{
                           MyMessageFinal message = new MyMessageFinal("false");
                           output.writeObject(message);
                           output.flush();
                       }
                    }
                    else{
                        DP_message.action();
                    }


                }
            } catch (EOFException e) {
                client_is_connected = false;
            } catch (ClassNotFoundException | IOException e) {
                // Gestione generica delle eccezioni durante la deserializzazione
                e.printStackTrace();
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
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

    }

}
