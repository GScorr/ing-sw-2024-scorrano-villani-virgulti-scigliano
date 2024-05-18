package it.polimi.ingsw.SOCKET_FINAL;


import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ErrorMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.GameFieldMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.setStateMessage;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;


import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientHandler  implements VirtualView {
    private MiniModel miniModel =  new MiniModel();
    final Server server;
    final ObjectInputStream input;
    final ObjectOutputStream output;

    //final VirtualView view;

    public Common_Server common;
    public String token;

    private VirtualGameServer rmi_controller;
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
                    Thread.sleep(100);
                    common.receiveHeartbeat(token);
                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void pushBack(ResponseMessage message){
        miniModel.pushBack(message);
    }

    public void setGameField(List<GameField> games){
        miniModel.setGameField(games);
    }

    public void setCards(List<PlayCard> cards){
        miniModel.setCards(cards);
    }

    public void setState(String state) throws IOException {
        ResponseMessage s = new setStateMessage(state);
        output.writeObject(s);
        output.flush();
    }

    private void startCheckingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    ResponseMessage s = miniModel.popOut();
                    if(s!=null){
                       output.writeObject(s);
                       output.flush();
                    }
                } catch (InterruptedException e) {
                    System.err.println("impossible to pop out");
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
                            this.token = common.createTokenSocket(this);

                        } else if (mayToken.equals("false")) {

                        } else{
                            this.token = mayToken;
                            int port = common.getPort(token);
                            Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                            this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                            this.rmi_controller.connectSocket(this);
                            client_is_connected = true;
                            startSendingHeartbeats();
                        }


                        MyMessageFinal message = new MyMessageFinal(mayToken);
                        output.writeObject(message);
                        output.flush();

                    }else
                    if((DP_message instanceof CreateGame)){
                        ((CreateGame) DP_message).setClientHandler(this);
                       int port =  ((CreateGame) DP_message).actionCreateGameMessage();
                        Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                        MyMessageFinal message = new MyMessageFinal("Creazione Player e Game andati a buon fine");
                        output.writeObject(message);
                        output.flush();
                        startSendingHeartbeats();
                        startCheckingMessages();
                    }
                    else if(DP_message instanceof FindRMIControllerMessage){
                        ((FindRMIControllerMessage) DP_message).setClientHandler(this);
                       if( ((FindRMIControllerMessage)DP_message).actionFindRmi()){
                           System.out.println(token);
                           int port = common.getPort(token);
                           Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                           this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                           MyMessageFinal message = new MyMessageFinal("true");
                           output.writeObject(message);
                           output.flush();
                           startSendingHeartbeats();
                           startCheckingMessages();
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
                client_is_connected = false;
                //e.printStackTrace();
            } catch (NotBoundException e) {
                client_is_connected = false;
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
