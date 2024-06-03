package it.polimi.ingsw.SOCKET_FINAL;


import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.*;

import it.polimi.ingsw.RMI_FINAL.MESSAGES.SocketResponseMess.*;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.Message.*;
import it.polimi.ingsw.VIEW.GraficInterterface;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;


import java.io.*;
import java.rmi.NotBoundException;

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
    public String name;

    private VirtualGameServer rmi_controller;
    public boolean client_is_connected = true;

    public ClientHandler(Server server, ObjectInputStream input, ObjectOutputStream output, Common_Server common ) throws IOException, NotBoundException {
        this.server = server;
        this.input = input;
        this.output = output;
       this.common = common;

    }

    public void startSendingHeartbeats() {
        new Thread(() -> {
            while (client_is_connected) {
                int k = 0;
                try {
                    Thread.sleep(100);
                    common.receiveHeartbeat(token);
                } catch (IOException | InterruptedException e) {

                    if( k == 0) System.err.println("[SERVER ERROR] SERVER DISCONNECTED");
                    System.err.println("\n              [SERVER ERROR] \n           SERVER DISCONNECTED");
                }
            }
        }).start();
    }

    @Override
    public void setGameFieldMiniModel() throws IOException {

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
        output.reset();
    }

    @Override
    public void setGameField(List<GameField> games) throws IOException {
        ResponseMessage s = new setGameFieldResponse(games);
        output.writeObject(s);
        output.flush();
        output.reset();
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
        ResponseMessage s = new addChatResponse(idx,message);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

    @Override
    public void insertId(int id) throws IOException {
        ResponseMessage s = new insertIdResponse(id);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

    @Override
    public void insertPlayer(Player player) throws IOException {
        ResponseMessage s = new insertPlayerResponse(player);
        output.writeObject(s);
        output.flush();
        output.reset();
    }

    @Override
    public GraficInterterface getTerminal_interface() throws IOException{
        return null;
    }

    @Override
    public void insertNumPlayers(int numPlayersMatch) throws IOException {
        ResponseMessage s = new NumPlayerResponse(numPlayersMatch);
        output.writeObject(s);
        output.flush();
        output.reset();
    }
    public void startCheckingMessages() {
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

                }
            }
        }).start();
    }

    @Override
    public boolean findRmiController(int id, String player_name) throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public void connectGameServer() throws IOException, NotBoundException, InterruptedException {

    }

    @Override
    public boolean isGoalCardPlaced() throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public String getGoalPlaced() throws IOException {
        return null;
    }

    @Override
    public String getFirstGoal() throws IOException, ClassNotFoundException, InterruptedException {
        return null;
    }

    @Override
    public String getSecondGoal() throws IOException {
        return null;
    }

    @Override
    public void chooseGoal(int i) throws IOException {

    }

    @Override
    public void showStartingCard() throws IOException, ClassNotFoundException, InterruptedException {

    }

    @Override
    public void chooseStartingCard(boolean b) throws IOException {

    }

    @Override
    public boolean isFirstPlaced() throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public String getToken() throws InterruptedException {
        return null;
    }


    public void runVirtualView() throws IOException, ClassNotFoundException {
        synchronized (this) {
            try {
                Message DP_message = null;

                while ((DP_message = (Message) input.readObject()) != null) {
                    if (token != null) {
                        DP_message.setToken(token);
                    }
                    if (rmi_controller != null) {
                        DP_message.setRmiController(this.rmi_controller);
                    }
                    DP_message.setServer(server);
                    DP_message.setOutput(output);
                    DP_message.setCommonServer(this.common);

                    if ((DP_message instanceof CheckNameMessage)) {
                        String nome = ((CheckNameMessage) DP_message).nome;
                        String mayToken =  common.checkName(nome,this);
                        if (mayToken.equals("true")) {
                            this.name = ((CheckNameMessage) DP_message).nome;
                            this.token = common.createToken(this);
                            ResponseMessage s = new checkNameResponse(1);
                            output.writeObject(s);
                            output.flush();
                            output.reset();
                        } else if (mayToken.equals("false")) {
                            ResponseMessage s = new checkNameResponse(0);
                            output.writeObject(s);
                            output.flush();
                            output.reset();
                        } else {
                            this.token = mayToken;
                            startCheckingMessages();
                            int port = common.getPort(token);
                            Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
                            this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                            client_is_connected = true;
                            ResponseMessage s = new checkNameResponse(2);
                            output.writeObject(s);
                            output.flush();
                            output.reset();
                            startSendingHeartbeats();
                        }


                    }

                     else if ((DP_message instanceof CreateGame)) {
                        ((CreateGame) DP_message).setClientHandler(this);
                        startCheckingMessages();
                        int port = ((CreateGame) DP_message).actionCreateGameMessage();

                        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
                        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                        startSendingHeartbeats();
                    } else if (DP_message instanceof FindRMIControllerMessage) {
                        ((FindRMIControllerMessage) DP_message).setClientHandler(this);
                        if (((FindRMIControllerMessage) DP_message).actionFindRmi()) {
                            ResponseMessage s = new CheckRmiResponse(true);
                            output.writeObject(s);
                            output.flush();
                            output.reset();
                        } else {
                            ResponseMessage s = new CheckRmiResponse(false);
                            output.writeObject(s);
                            output.flush();
                            output.reset();
                        }
                    } else if (DP_message instanceof connectGame) {
                         startCheckingMessages();
                        int port = common.getPort(token);
                        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
                        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                        startSendingHeartbeats();
                    } else if (DP_message instanceof getGoalCard) {
                        boolean isPresent = ((getGoalCard) DP_message).getGoalCardAction();
                        ResponseMessage s = new checkGoalCardPresent(isPresent);
                        output.writeObject(s);
                        output.flush();
                        output.reset();
                    } else if (DP_message instanceof getListGoalCard) {
                        List<Goal> list_goal_card = ((getListGoalCard) DP_message).actionGetListGoalCard();
                        ResponseMessage s = new getListGoalCardResponse(list_goal_card);
                        output.writeObject(s);
                        output.flush();
                        output.reset();
                    } else if (DP_message instanceof getStartingCard) {
                        PlayCard starting_card = ((getStartingCard) DP_message).getStartingCardAction();
                        ResponseMessage s = new StartingCardResponse(starting_card);
                        output.writeObject(s);
                        output.flush();
                        output.reset();
                    } else if (DP_message instanceof firstCardIsPlaced) {
                        boolean isPlaced = ((firstCardIsPlaced) DP_message).firstCardIsPlacedAction();
                        ResponseMessage s = new checkStartingCardSelected(isPlaced);
                        output.writeObject(s);
                        output.flush();
                        output.reset();
                    } else {
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
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }




    // --------------------- THESE METHODS ARE NEVER CALLED -------------------



    @Override
    public int checkName(String playerName) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        return 0;
    }

    @Override
    public boolean areThereFreeGames() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public void createGame(String gameName, int numplayers, String playerName) throws IOException, NotBoundException, ClassNotFoundException {

    }

    @Override
    public void manageGame(boolean endgame) throws IOException {

    }

    @Override
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException, ClassNotFoundException {

    }

    @Override
    public void drawCard(SendFunction function) throws IOException, InterruptedException {

    }

    @Override
    public void ChatChoice(String message, int decision) throws IOException {

    }

    @Override
    public List<SocketRmiControllerObject> getFreeGames() throws IOException, ClassNotFoundException, InterruptedException {
        return null;
    }

    @Override
    public VirtualGameServer getGameServer() throws IOException {
        return null;
    }



    @Override
    public boolean isGoldDeckPresent() throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public boolean isResourceDeckPresent() throws IOException, ClassNotFoundException, InterruptedException {
        return false;
    }

    @Override
    public void showCardsInCenter() throws IOException, ClassNotFoundException, InterruptedException {

    }

    @Override
    public void runGUI(SceneController scene) throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {

    }


}
