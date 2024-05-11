package it.polimi.ingsw.SOCKET_FINAL;


import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualServerF;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;


import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientHandler  implements VirtualView {

    final Server server;
    final ObjectInputStream input;
    final ObjectOutputStream output;
    //final VirtualView view;

    public VirtualServerF rmi_server;
    public String token;

    private VirtualGameServer rmi_controller;
    public boolean client_is_connected = true;


    public ClientHandler(Server server, ObjectInputStream input, ObjectOutputStream output,VirtualServerF rmi_server ) throws RemoteException, NotBoundException {
        this.server = server;
        this.input = input;
        this.output = output;
       // this.view = new ClientProxy(output);
        this.rmi_server = rmi_server;

    }

    private void startSendingHeartbeats() {
        new Thread(() -> {
            while (client_is_connected) {
                try {
                    Thread.sleep(1500);
                    rmi_server.receiveHeartbeat(token);
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
                    DP_message.setRmiServer(this.rmi_server);

                    if((DP_message instanceof CheckNameMessage)){
                        String mayToken = ((CheckNameMessage) DP_message).checkNameMessageAction();

                        if(mayToken.equals("true")){
                            this.token = rmi_server.createTokenSocket(((CheckNameMessage) DP_message).nome);

                        } else if (mayToken.equals("false")) {

                        } else{
                            this.token = mayToken;
                            int port = rmi_server.getPort(token);
                            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                            this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
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
                        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                        MyMessageFinal message = new MyMessageFinal("Creazione Player e Game andati a buon fine");
                        output.writeObject(message);
                        output.flush();
                        startSendingHeartbeats();
                    }
                    else if(DP_message instanceof FindRMIControllerMessage){
                       if( ((FindRMIControllerMessage)DP_message).actionFindRmi()){
                           System.out.println(token);
                           int port = rmi_server.getPort(token);
                           Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                           this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
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
