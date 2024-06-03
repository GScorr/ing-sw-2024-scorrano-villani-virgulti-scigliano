package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.VIEW.GraficInterterface;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;

import it.polimi.ingsw.VIEW.GuiPackage.GUI;
import it.polimi.ingsw.VIEW.GuiPackage.SceneController;
import it.polimi.ingsw.VIEW.TUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.NotBoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class clientSocket implements VirtualViewF, Serializable {

    public MiniModel miniModel = new MiniModel();

    // ServerProxy is used for sending messages from client to server through the  output Socket
    private ServerProxy server_proxy;

    ObjectInputStream input;
    
    public boolean flag_check;
    public boolean check;
    public boolean starting_card_is_placed;
    public boolean place_flag_check;
    public int checkName;
    public List<SocketRmiControllerObject> free_games;
    public boolean checkSizeGoldDeck;
    public boolean checkSizeResourcesDeck;
    public int point;

    public boolean GoalCardisPresent;
    public List<Goal> goalsCard;

    public PlayCard startingCard;


    public Goal goal_choosed;

    GraficInterterface terminal_interface;
    public String token;
    private boolean flag_Server_Disconneted = false;





    public clientSocket(ObjectInputStream input, ObjectOutputStream output) throws IOException, ClassNotFoundException {
        this.server_proxy = new ServerProxy(output);
        this.input = input;
    }

    public void runTUI() throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {
        new Thread(() -> {
            try {
                startCheckingMessagesSocket();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        terminal_interface = new TUI(this);
        terminal_interface.runCli();
    }

    public void runGUI(SceneController scene) throws IOException, ClassNotFoundException, InterruptedException, NotBoundException {
        new Thread(() -> {
            try {
                startCheckingMessagesSocket();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        terminal_interface = new GUI(scene);
        terminal_interface.runCli();
    }





    //Thread that receive the input Object from the server
    public void  startCheckingMessagesSocket() throws IOException,ClassNotFoundException{
        new Thread( () -> {
            ResponseMessage s;
            while(true) {
                try {
                    if (((s = (ResponseMessage) input.readObject()) != null)) {
                        s.setClient(this);
                        s.action();
                    }
                } catch (IOException e) {
                    if(! flag_Server_Disconneted){
                        System.err.println("[SERVER ERROR] SERVER DISCONNECTED");
                        flag_Server_Disconneted = true;
                    }

                } catch (ClassNotFoundException e) {
                    System.out.println("errore startCheckingMessagesSocket ");
                }
            }
        }).start();
    }

    @Override
    public void showUpdate(GameField game_field) throws IOException {

    }



    @Override
    public void showCard(PlayCard card) throws IOException {

    }

    @Override
    public void pushBack(ResponseMessage message) throws IOException {

    }

    @Override
    public void showField(GameField field) throws IOException {

    }

    @Override
    public void printString(String s) throws IOException {

    }

    @Override
    public void setGameField(List<GameField> games) throws IOException {

    }

    @Override
    public MiniModel getMiniModel() throws IOException {
        return this.miniModel;
    }

    @Override
    public void setCards(List<PlayCard> cards) throws IOException {

    }

    @Override
    public void setNumToPlayer(HashMap<Integer, String> map) throws IOException {

    }

    @Override
    public void setState(String state) throws IOException {

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

    @Override
    public GraficInterterface getTerminal_interface() throws IOException{
        return terminal_interface;
    }

    @Override
    public int checkName(String playerName) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        server_proxy.checkName(playerName);
        waitResponse();
        return checkName;
    }

    @Override
    public boolean areThereFreeGames() throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        this.getFreeGames();
        return free_games != null && !free_games.isEmpty();
    }
    @Override
    public List<SocketRmiControllerObject> getFreeGames() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getFreeGame();
        waitResponse();
        return this.free_games;
    }


    @Override
    public void createGame(String gameName, int numplayers, String playerName) throws IOException, NotBoundException, ClassNotFoundException, InterruptedException {
        server_proxy.createGame(gameName,numplayers,playerName);
        while (this.miniModel.getState().equals("NOT_IN_A_GAME")){
            buffering();
        }
    }

    @Override
    public boolean findRmiController(int id, String playerName) throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.findRmiController(id,playerName);
        waitResponse();
        return this.check;
    }

    @Override
    public void connectGameServer() throws IOException, NotBoundException, InterruptedException {
        server_proxy.connectGame();
        while(this.miniModel.getState().equals( "NOT_IN_A_GAME")){
            buffering();
        }
    }
    @Override
    public void manageGame(boolean endgame) throws IOException {

    }

    @Override
    public void selectAndInsertCard(int choice, int x, int y, boolean flipped) throws IOException, InterruptedException, ClassNotFoundException {
        server_proxy.placeCard(choice,x,y,flipped);
        waitResponse();

        // time that I have to wait for receive the next State, NB : next state could be both PlaceCard or DrawCard
        buffering();
        buffering();
    }

    @Override
    public void drawCard(SendFunction function) throws IOException, InterruptedException {
        server_proxy.drawCard(function);
        while (this.miniModel.getState().equals("DRAW_CARD")){
            buffering();
        }
    }

    @Override
    public void ChatChoice(String message, int decision) throws IOException {
       if(miniModel.getNum_players() != 2){
           if(decision==miniModel.getNum_players()+1){
               server_proxy.chattingGlobal(new ChatMessage(message, miniModel.getMy_player()));
           }
           else{
               server_proxy.chattingMoment(miniModel.getMy_index(), decision, new ChatMessage(message,miniModel.getMy_player()));
           }
       } else{
           server_proxy.chattingGlobal(new ChatMessage(message, miniModel.getMy_player()));
       }

    }



    @Override
    public VirtualGameServer getGameServer() throws IOException {
        return null;
    }





    @Override
    public void setGameFieldMiniModel() throws IOException {

    }



    @Override
    public boolean isGoalCardPlaced() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getGoalCard();
        waitResponse();
        return ! this.GoalCardisPresent;
    }

    @Override
    public String getGoalPlaced() {
        return goal_choosed.toString();
    }

    @Override
    public String getFirstGoal() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getListGoalCard();
        waitResponse();
        return goalsCard.get(0).toString();
    }

    @Override
    public String getSecondGoal() {
        return goalsCard.get(1).toString();
    }

    @Override
    public void chooseGoal(int i) throws IOException {
        this.goal_choosed = goalsCard.get(i);
        server_proxy.chooseGoal(i);
    }

    @Override
    public void showStartingCard() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getStartingCard();
        waitResponse();
        showStartingCardHelper(this.startingCard);
    }

    @Override
    public void chooseStartingCard(boolean b) throws IOException {
        server_proxy.chooseStartingCard(b);
    }

    @Override
    public boolean isFirstPlaced() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.startingCardIsPlaced();
        waitResponse();
        return this.starting_card_is_placed;
    }

    @Override
    public String getToken() throws InterruptedException, IOException {
        server_proxy.getToken();
        waitResponse();
        return this.token;
    }

    @Override
    public boolean isGoldDeckPresent() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.isGoldDeckPresent();
        waitResponse();
        return this.checkSizeGoldDeck;
    }

    @Override
    public boolean isResourceDeckPresent() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.isResourceDeckPresent();
        waitResponse();
        return this.checkSizeResourcesDeck;
    }

    @Override
    public void showCardsInCenter() throws IOException, ClassNotFoundException, InterruptedException {
        server_proxy.getCardsInCenter();
        waitResponse();
    }


    //report Messages/Errors from Server
    @Override
    public void reportError(String details) throws IOException {

    }

    @Override
    public void reportMessage(String details) throws IOException {

    }


    //help function
    private void buffering() throws IOException, InterruptedException{
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("/");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("|");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("\\");
        Thread.sleep(1000);
        System.out.print("\b");
        System.out.print("-");
    }
    private boolean menuChoice(int choice) throws IOException {
        Scanner scan = new Scanner(System.in);
        if ( choice < 0 || choice > 3 ) return false;
        switch ( choice ){
            case ( 0 ):
                miniModel.printNumToField();
                Integer i = scan.nextInt();
                miniModel.showGameField(i);
                break;
            case( 1 ):
                miniModel.showCards();
                break;
            case ( 2 ):
                //   miniModel.showChat();
            case ( 3 ):
                return true;}
        return true;
    }

    public void showCardInCenterHelper(PlayCard card) throws IOException {

        Side front = card.getFrontSide();

        System.out.println(" You can Only See the FRONT SIDE\n----------------------------");

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

    public void showStartingCardHelper(PlayCard card) throws IOException {
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
    public void showFieldHelper(GameField field) throws IOException {
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

    public void waitResponse() throws InterruptedException {
        flag_check = true;
        while(flag_check){
            Thread.sleep(200);
        }
    }

}
