package it.polimi.ingsw.SOCKET_FINAL;


import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.StartingCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.*;

import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;


import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientHandler  implements VirtualViewF {
    private MiniModel miniModel =  new MiniModel();
    final Server server;
    final ObjectInputStream input;
    final ObjectOutputStream output;


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

    public void showValue(String message) {

    }

    @Override
    public void showUpdate(GameField game_field) throws IOException {

    }

    @Override
    public void reportError(String details) throws IOException {

    }

    @Override
    public void reportMessage(String details) throws IOException {

    }

    @Override
    public void showCard(PlayCard card) throws IOException {
        ResponseMessage s = new showCenterCardsResponse(card);
        output.writeObject(s);
        output.flush();
        output.reset();
    }
    @Override
    public void pushBack(ResponseMessage message) throws IOException {
        miniModel.pushBack(message);
    }

    @Override
    public void showField(GameField field) throws IOException {

    }

    @Override
    public void printString(String string) throws IOException {
        ResponseMessage s = new StringResponse(string);
        output.writeObject(s);
        output.flush();
    }

    @Override
    public void setGameField(List<GameField> games) throws IOException {
        List<GameField> fields = new ArrayList<>(games);
        System.out.println("questi i campi che mi arrivano");
        for (GameField f : fields){
            CopyshowField(f);
        }
        System.out.println("end");
        ResponseMessage s = new setGameFieldResponse(fields);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

    private void CopyshowField(GameField field) throws IOException {
        boolean[] nonEmptyRows = new boolean[Constants.MATRIXDIM];
        boolean[] nonEmptyCols = new boolean[Constants.MATRIXDIM];


        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            for (int j = 0; j < Constants.MATRIXDIM; j++) {
                if (field.getCell(i, j, Constants.MATRIXDIM).isFilled()) {
                    nonEmptyRows[i] = true;
                    nonEmptyCols[j] = true;


                    if (i > 0) nonEmptyRows[i - 1] = true;
                    if (i < Constants.MATRIXDIM - 1) nonEmptyRows[i + 1] = true;
                    if (j > 0) nonEmptyCols[j - 1] = true;
                    if (j < Constants.MATRIXDIM - 1) nonEmptyCols[j + 1] = true;
                }
            }
        }


        System.out.print("   ");
        for (int k = 0; k < Constants.MATRIXDIM; k++) {
            if (nonEmptyCols[k]) {
                System.out.print(k + " ");
            }
        }
        System.out.print("\n");


        for (int i = 0; i < Constants.MATRIXDIM; i++) {
            if (nonEmptyRows[i]) {
                System.out.print(i + " ");
                for (int j = 0; j < Constants.MATRIXDIM; j++) {
                    if (nonEmptyCols[j]) {
                        if (field.getCell(i, j, Constants.MATRIXDIM).isFilled()) {
                            System.out.print(field.getCell(i, j, Constants.MATRIXDIM).getShort_value() + " ");
                        } else {
                            System.out.print("   ");
                        }
                    }
                }
                System.out.print("\n");
            }
        }
    }

    @Override
    public MiniModel getMiniModel() throws IOException {
        return null;
    }

    @Override
    public void setCards(List<PlayCard> cards)throws IOException {
        ResponseMessage s = new setCardsResponse(cards);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

    @Override
    public void setNumToPlayer(HashMap<Integer, String> map) throws IOException {
        ResponseMessage s = new NumToPlayerResponse(map);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

    @Override
    public void setState(String state) throws IOException {
        ResponseMessage s = new setStateMessage(state);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

    @Override
    public void addChat(int idx, ChatMessage message) throws IOException {

    }

    @Override
    public void insertId(int id) throws IOException {

    }

    @Override
    public void insertNumPlayers(int numPlayersMatch) throws IOException {

    }

    @Override
    public void insertPlayer(Player player) throws IOException {

    }

    private void startCheckingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(200);
                    ResponseMessage s = miniModel.popOut();
                    if(s!=null){
                       output.writeObject(s);
                       output.flush();
                       output.reset();
                    }
                } catch (InterruptedException e) {

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
                            this.token = common.createToken(this);

                        } else if (mayToken.equals("false")) {

                        } else{
                            this.token = mayToken;
                            int port = common.getPort(token);
                            Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
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
                        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);

                        startCheckingMessages();
                        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                    //    this.rmi_controller.connectSocket(this);
                        startSendingHeartbeats();

                    }
                    else if(DP_message instanceof FindRMIControllerMessage){
                        ((FindRMIControllerMessage) DP_message).setClientHandler(this);
                       if( ((FindRMIControllerMessage)DP_message).actionFindRmi()){
                           //System.out.println(token);
                           int port = common.getPort(token);
                           Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
                           this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                           ResponseMessage s = new CheckRmiResponse(true);
                           output.writeObject(s);
                           output.flush();
                           startSendingHeartbeats();
                           startCheckingMessages();
                       //    this.rmi_controller.connectSocket(this);
                       }else{
                           ResponseMessage s = new CheckRmiResponse(false);
                           output.writeObject(s);
                           output.flush();
                       }
                    }else if(DP_message instanceof getGoalCard){
                        boolean isPresent = ((getGoalCard) DP_message).getGoalCardAction();
                        ResponseMessage s = new checkGoalCardPresent(isPresent);
                        output.writeObject(s);
                        output.flush();
                    }
                    else if(DP_message instanceof getListGoalCard){
                        List<Goal> list_goal_card = ((getListGoalCard) DP_message).actionGetListGoalCard();
                        ResponseMessage s = new getListGoalCardResponse(list_goal_card);
                        output.writeObject(s);
                        output.flush();
                    }
                    else if(DP_message instanceof getStartingCard) {
                        PlayCard starting_card = ((getStartingCard) DP_message).getStartingCardAction();
                        ResponseMessage s = new StartingCardResponse(starting_card);
                        output.writeObject(s);
                        output.flush();
                    }
                    else if(DP_message instanceof firstCardIsPlaced) {
                        boolean isPlaced = ((firstCardIsPlaced) DP_message).firstCardIsPlacedAction();
                        ResponseMessage s = new checkStartingCardSelected(isPlaced);
                        output.writeObject(s);
                        output.flush();
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



}
