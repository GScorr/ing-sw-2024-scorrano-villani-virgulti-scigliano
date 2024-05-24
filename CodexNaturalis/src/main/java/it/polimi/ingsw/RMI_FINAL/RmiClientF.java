package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.*;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.StringCostant;
import it.polimi.ingsw.VIEW.TUI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;


public class RmiClientF extends UnicastRemoteObject implements VirtualViewF {
    final VirtualServerF server;
    private String token;
    private VirtualGameServer rmi_controller;
    private final MiniModel miniModel =  new MiniModel();
    private final StringCostant stringcostant = new StringCostant();
    private TUI tui;

    public RmiClientF(VirtualServerF server) throws IOException {
        this.server = server;
    }

    public void run() throws IOException, InterruptedException, NotBoundException, ClassNotFoundException {
        this.server.connect(this);
        tui = new TUI(this);
        runCli();
    }


    private void runCli() throws NotBoundException, IOException, InterruptedException, ClassNotFoundException {
        tui.runCli();
    }

    //GAME FLOW

    public int checkName(String player_name) throws IOException, NotBoundException {
        int flag;
            String isnew = server.checkName(player_name,this);
        if(isnew.equals("true")) {
            flag = 1;
            this.token = server.createToken(this);}
        else if(isnew.equals("false")){
            flag=0;
            System.err.println(stringcostant.name_is_not_valid);}
        else{
            this.token = isnew;
            int port = server.getPort(token);
            Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
            this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
            rmi_controller.connectRMI(this);
            flag=2;
        }
        startSendingHeartbeats();
        tui.setToken(token);
        return flag;
    }

    public boolean areThereFreeGames () throws IOException {
        return server.getFreeGames() != null && !server.getFreeGames().isEmpty();
    }


    public List<SocketRmiControllerObject> getFreeGames() throws IOException {return server.getFreeGamesSocket();}
    public void createGame(String game_name, int numplayers, String player_name) throws IOException, NotBoundException, InterruptedException {
        int port;
        port = server.createGame(game_name, numplayers, token, player_name,this);
        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
        //rmi_controller.connectRMI(this);
        //startSendingHeartbeats();
        startCheckingMessages();
    }


    public void manageGame(boolean endgame) throws IOException {
        if(!endgame) rmi_controller.getPoints(token);
        else rmi_controller.getFinalStandings(token);
    }
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException {
        SendFunction function = new SendInsertCard(token, choice-1, x,y,flipped);
        rmi_controller.addQueue(function);
        Thread.sleep(750);
    }

    public void drawCard(SendFunction function) throws IOException, InterruptedException {
        rmi_controller.addQueue(function);
        Thread.sleep(750);
    }

    public boolean findRmiController(int id, String player_name) throws IOException, InterruptedException {
        return server.findRmiController(id, token, player_name,this);
    }

    public void connectGameServer() throws IOException, NotBoundException {
        int port = server.getPort(token);
        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, port);
        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
        //rmi_controller.connectRMI(this);
        startCheckingMessages();
        //startSendingHeartbeats();
    }

    public boolean isGoalCardPlaced() throws IOException {
        return rmi_controller.getTtoP().get(token).getGoalCard() == null;
    }

    public String getGoalPlaced() throws IOException {
        return rmi_controller.getTtoP().get(token).getGoalCard().toString();
    }

    @Override
    public String getFirstGoal() throws IOException {
        return rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().getFirst().toString();
    }

    @Override
    public String getSecondGoal() throws IOException {
        return rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(1).toString();
    }

    @Override
    public void chooseGoal(int i) throws IOException, InterruptedException {
        rmi_controller.chooseGoal(token,i);
    }

    @Override
    public void showStartingCard() throws IOException {
        rmi_controller.showStartingCard(token);
    }

    @Override
    public void chooseStartingCard(boolean b) throws IOException, InterruptedException {
        rmi_controller.chooseStartingCard(token,b);
    }

    @Override
    public boolean isFirstPlaced() throws IOException {
        return rmi_controller.getTtoP().get(token).isFirstPlaced();
    }

    @Override
    public String getToken()  {
        return this.token;
    }

    @Override
    public boolean isGoldDeckPresent() throws IOException {
        return rmi_controller.getController().getGame().getGold_deck().getNumber()>0;
    }

    @Override
    public boolean isResourceDeckPresent() throws IOException {
        return rmi_controller.getController().getGame().getResources_deck().getNumber()>0;
    }


    // THREADS

    public void startCheckingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    ResponseMessage s = miniModel.popOut();
                    if(s!=null) s.action();
                } catch (IOException e) {throw new RuntimeException(e);}
                try {Thread.sleep(100);} catch (InterruptedException e) {throw new RuntimeException(e);}
            }
        }).start();
    }

    public void startSendingHeartbeats() {
        new Thread(() -> {
            int cracked = 0;
            while (true) {
                try {
                    Thread.sleep(150);
                    server.receiveHeartbeat(token);
                } catch (IOException | InterruptedException e) {
                    if(cracked==0) {
                        cracked = 1;
                        System.err.println("[SERVER ERROR] SERVER DISCONNECTED");
                    }
                }
            }
        }).start();
    }

    public void setGameFieldMiniModel() throws IOException {miniModel.setGameField(rmi_controller.getGameFields(token));}

    //SETTERS

    public void setGameField(List<GameField> games){miniModel.setGameField(games);}
    public void setCards(List<PlayCard> cards){miniModel.setCards(cards);}
    public void pushBack(ResponseMessage message){miniModel.pushBack(message);}
    public void setState(String state){ miniModel.setState(state);}
    public void setNumToPlayer(HashMap<Integer, String> map){miniModel.setNumToPlayer(map);}


    // HELP FUNCTIONS
    public void ChatChoice(String message, int decision) throws IOException {
        if(decision==miniModel.getNum_players()+1){rmi_controller.chattingGlobal(new ChatMessage(message, miniModel.getMy_player()));}
        else{rmi_controller.chattingMoment(miniModel.getMy_index(), decision, new ChatMessage(message, miniModel.getMy_player()));}
    }

    @Override
    public void showUpdate(GameField gamefield) {}
    @Override
    public void reportError(String details) {System.err.print("\n[ERROR] " + details + "\n> ");}
    @Override
    public void reportMessage(String details) {System.out.print("\n[ERROR] " + details + "\n> ");}

    public MiniModel getMiniModel() {return miniModel;}
    public VirtualGameServer getGameServer(){ return rmi_controller;}



    public void showCardsInCenter() throws IOException {rmi_controller.showCardsInCenter(token);}
    public void printString(String s) {System.out.println(s);}

    public void addChat(int idx, ChatMessage message) throws IOException{
        miniModel.addChat(idx, message);
    }

    public void insertId(int id) throws IOException{
        miniModel.setMy_index(id);
    }

    public void insertNumPlayers(int numPlayersMatch) throws IOException{
        miniModel.setNum_players(numPlayersMatch);
    }

    public void insertPlayer(Player player) throws IOException{
        miniModel.setMy_player(player);
    }


    @Override
    public void showCard(PlayCard card) {
        Side back = card.getBackSide();
        Side front = card.getFrontSide();

        System.out.println("BACK SIDE\n----------------------------");
        System.out.println( " | " + back.getAngleLeftUp().toString().substring(0,2)  +   " |               "+ " | " + back.getAngleRightUp().toString().substring(0,2) + " |\n " );
        //System.out.println( " | " + back.getAngleRightUp().toString().charAt(0) + " |\n " );
        System.out.println( " |       | " + back.getCentral_resource().toString().substring(0,2) + back.getCentral_resource2().toString().substring(0,2) + back.getCentral_resource3().toString().substring(0,2) + " |         |\n " );
        System.out.println( " | " + back.getAngleLeftDown().toString().substring(0,2) +  " |               " + " | " + back.getAngleRightDown().toString().substring(0,2) + " |\n " );
        //System.out.println(  );
        System.out.println("----------------------------\n\n");

        System.out.println("FRONT SIDE\n----------------------------");

        if(card instanceof ResourceCard) {
            System.out.println(" | " + card.getPoint() + " | ");
            if (card instanceof GoldCard) {
                System.out.println(" | " + ((GoldCard) card).getPointBonus().toString().substring(0, 2) + " | " + "             | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            } else {
                System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
            }
        }
        else {
            System.out.println(" | " + front.getAngleLeftUp().toString().substring(0, 2) + " | " + "              | " + front.getAngleRightUp().toString().substring(0, 2) + " |\n ");
        }
        //System.out.println( " | " + front.getAngleRightUp().toString().charAt(0) + " |\n " );
        System.out.println( " |       | " + front.getCentral_resource().toString().substring(0,2) + front.getCentral_resource2().toString().substring(0,2) + front.getCentral_resource3().toString().substring(0,2) + " |        |\n " );
        //System.out.println( " | " + front.getAngleLeftDown().toString().charAt(0) + " |       " );
        if ( card instanceof GoldCard ){
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " | " +
                    "  " + card.getCostraint().toString()  + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n ");
        }else{
            System.out.println( " | " + front.getAngleLeftDown().toString().substring(0,2) + " |              " + " | " + front.getAngleRightDown().toString().substring(0,2) + " |\n " );
        }
        //System.out.println( " | " + front.getAngleRightDown().toString().charAt(0) + " |\n " );
        System.out.println("----------------------------\n\n");

    }
    public void showField(GameField field) {
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
    //MAIN
    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException, ClassNotFoundException {
        Registry registry = LocateRegistry.getRegistry(Constants.IPV4, 1);
        VirtualServerF server = (VirtualServerF) registry.lookup("VirtualServer");
        new RmiClientF(server).run();
    }

}

