package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendInsertCard;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.*;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.Message.firstCardIsPlaced;
import it.polimi.ingsw.StringCostant;


import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
public class Client implements VirtualViewF {

    boolean flag_check;
    boolean place_flag_check;
    boolean check;

    boolean checkSizeGoldDeck;
    boolean checkSizeResourcesDeck;
    int point;

    boolean GoalCardisPresent;
    List<Goal> goalsCard;

    PlayCard startingCard;
    boolean startingCardChoosed;

    Goal goal_choosed;

    StringCostant string_costant = new StringCostant();
    private MiniModel miniModel =  new MiniModel();
    final ServerProxy server;

    private boolean newClient;
    ObjectInputStream input;

    public Client(ObjectInputStream input, ObjectOutputStream output) throws IOException, InterruptedException {
        this.server = new ServerProxy(output,input);
        this.input = input;
    }

    public void run() throws IOException, ClassNotFoundException, InterruptedException {
        new Thread(() -> {
            try {
               // runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }


    private void startCheckingMessages() throws IOException, ClassNotFoundException {
        new Thread(() -> {
            ResponseMessage s;
            // Read message type
            while (true) {
                try {
                    if (((s = (ResponseMessage) input.readObject()) != null)) {
                        if(s instanceof CheckRmiResponse){
                            check = ((CheckRmiResponse) s).check;
                            this.flag_check = false;

                        }
                        else if(s instanceof checkGoalCardPresent){
                            System.out.println("checkGoalCardPresent");
                             GoalCardisPresent = ((checkGoalCardPresent) s).isPresent;
                             this.flag_check = false;
                        }
                        else if( s instanceof getListGoalCardResponse ){

                            this.goalsCard = ((getListGoalCardResponse) s).goal_cards;
                            this.flag_check = false;
                        }
                        else if( s instanceof StartingCardResponse ){
                            this.startingCard = ((StartingCardResponse) s).starting_card;
                            this.flag_check = false;
                        }
                        else if ( s instanceof checkStartingCardSelected){
                            this.startingCardChoosed = ((checkStartingCardSelected) s).isSelected;
                            this.flag_check = false;
                        }
                        else if ( s instanceof CardResponse){
                            showCard(((CardResponse) s).CardResponseAction());
                        }
                        else if ( s instanceof CheckGoldDeckSize){
                            checkSizeGoldDeck = ((CheckGoldDeckSize) s).checkSize;
                            this.flag_check = false;
                        }
                        else if ( s instanceof CheckResourcesDeckSize){
                            checkSizeResourcesDeck = ((CheckResourcesDeckSize) s).checkSize;
                            this.flag_check = false;
                        }
                        else if ( s instanceof PointResponse){
                            point = ((PointResponse) s).player_point;
                            this.flag_check = false;
                        }
                        else {
                            s.setMiniModel(miniModel);
                            s.action();
                            if( s instanceof GameFieldMessage){
                                showField(((GameFieldMessage) s).getField());
                                place_flag_check = false;
                            }
                            if ( s instanceof ErrorMessage){
                                System.out.println(s.getMessage());
                            }
                            //ce ne devono essere 2 uno per la starting Card, uno per le Central Card
                            if(s instanceof showCenterCardsResponse){
                                showCardInCenter(((showCenterCardsResponse) s).card);
                            }
                        }

                    };
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("errore nel startCheckingMessages thread");
                    throw new RuntimeException(e);
                }

            }
        }).start();
    }


    private void runCli() throws IOException, ClassNotFoundException, InterruptedException {


        //TODO : gestione persistenza connessioni
        String player_name = selectNamePlayer();
        String game_name;

        gameAccess(player_name);
        waitFullGame();

        chooseGoalState();

        chooseStartingCardState();
        manageGame();

    }

    public String selectNamePlayer() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        Player curr_player;
        String player_name = " ";
        boolean flag = false;
        do{
            System.out.print(string_costant.choose_name_player);
            player_name = scan.nextLine();
            String isnew = server.checkName(player_name);
            if(isnew.equals("true")){
                flag = true;
                newClient=true;
                System.out.println(string_costant.name_is_valid);
            }else if(isnew.equals("false")){
                System.out.println(string_costant.name_is_not_valid);
            }else{
                flag = true;
                newClient = false;
                System.out.println(player_name + string_costant.riconnected);
            }

        }while (!flag);

        return player_name;
    }

    public void gameAccess(String player_name) throws IOException, ClassNotFoundException, InterruptedException {
        if(newClient) {

            if (server.getFreeGame() == null || server.getFreeGame().isEmpty()) {
                newGame_notavailable(player_name);
                startCheckingMessages();
            } else {
                makeChoice(player_name);
            }
            System.out.print(string_costant.creation_player);
        }
    }

    private void newGame_notavailable(String playerName) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println(string_costant.new_game_creation);
        String game_name = scan.nextLine();
        boolean flag;
        do {
            flag = false;
            System.out.print(string_costant.new_game_num_player);
            int numplayers = scan.nextInt();
            try {
                server.createGame(game_name, numplayers, playerName);
                System.out.println("Congratulation, Game created");
            } catch (ControllerException | IOException | ClassNotFoundException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            }
        } while(flag);
    }

    //tieni d'occhio quando chiami startCheckingMessages();
    private void makeChoice(String player_name) throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {
            System.out.println(string_costant.new_or_old);
            String decision = scan.nextLine();
            if (decision.equalsIgnoreCase("old")) {
                done = 1;
                chooseMatch(player_name);
            } else if (decision.equalsIgnoreCase("new")) {
                done=1;
                newGame(player_name);
                startCheckingMessages();
            } else {
                System.out.println(string_costant.error);
            }
        }
    }

    private void newGame(String player_name) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.print(string_costant.game_creation);
        String game_name = scan.nextLine();
        int numplayers=4;
        boolean flag;
        do {
            flag = false;
            System.out.print(string_costant.new_game_num_player);
            numplayers = scan.nextInt();
            try {
                 server.createGame(game_name, numplayers, player_name);
                System.out.println("congratulation Game created");
            } catch (ControllerException | IOException | ClassNotFoundException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            }
        } while(flag);
    }


    private void chooseMatch(String player_name) throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        List<Integer> id_games = new ArrayList<>();
        System.out.println(string_costant.list_game_avilable);
         List<SocketRmiControllerObject> games = server.getFreeGame();

        for (SocketRmiControllerObject r : games) {
            System.out.println(r.name + " ID:" + r.ID
                    + " " + r.num_player + "/" + r.max_num_player);
            id_games.add(r.ID);
        }
        do {
            startCheckingMessages();

            boolean checkId = true;
            int ID = 0;
            while(checkId){
                System.out.println(string_costant.Id_game);
                ID  = scan.nextInt();
                if(id_games.contains(ID)){
                    checkId = false;
                }else{
                    System.out.println("ERR: INSERT A VALID ID\n");
                }
            }

            server.findRmiController(ID, player_name);
            flag_check = true;

            while (flag_check){
                Thread.sleep(100);
            }


        }while(!check);

        System.out.println(string_costant.enter);

    }

    /**
     * manca getTtop, non so in che classe va
     */

    private void waitFullGame() throws IOException, InterruptedException, ClassNotFoundException {
        if(miniModel.getState().equals("NOT_INITIALIZED")){
            System.out.println("Wait until the game is full");
            while(miniModel.getState().equals("NOT_INITIALIZED")){
                buffering();
            }
            System.out.println("\nYou Game is ready to start");
        }
    }


    private void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException {

        if(miniModel.getState().equals("CHOOSE_GOAL")){
            server.getGoalCard();
           flag_check = true;
           while (flag_check){
               Thread.sleep(100);
           }
           if(!GoalCardisPresent){
               chooseGoal();
               System.out.println("\n You choose :" + goal_choosed.toString());
           }
       }
       while(miniModel.getState().equals("CHOOSE_GOAL")){
           buffering();
       }
    }

    private void chooseGoal() throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {

            server.getListGoalCard();
            flag_check = true;

            while (flag_check){
                Thread.sleep(100);
            }


            System.out.println("\nChoose Goal between :\n 1-" + goalsCard.get(0).toString()
                    + "\n 2-" + goalsCard.get(1).toString());

            String choice = scan.nextLine();
            if (choice.equals("1")) {
                goal_choosed = goalsCard.get(0);
                done=1;
                server.chooseGoal(0);
            } else if (choice.equals("2")){
                goal_choosed = goalsCard.get(1);
                done=1;
                server.chooseGoal(1);
            } else System.out.println("Index boundaries not respected!");
        }
    }

    private void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException {
        if(miniModel.getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            System.out.println(string_costant.starting_card);
            server.getStartingCard();
            flag_check = true;
            while(flag_check){
                Thread.sleep(100);
            }
            server.startingCardIsPlaced();
            flag_check = true;
            while (flag_check){
                Thread.sleep(100);
            }
            if(!startingCardChoosed) {
                showCard(this.startingCard);
                chooseStartingCard();
            }
            while (miniModel.getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
                buffering();
            }
        }


    }

    private void chooseStartingCard() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nScegli lato carta iniziale:\n");
        int done=0;
        while(done==0){
            System.out.println(string_costant.BackorFront);
            String dec = scan.nextLine();
            if (dec.equals("F")){
                done=1;
                server.chooseStartingCard(false);
            } else if (dec.equals("B")){
                done=1;
                server.chooseStartingCard(true);
            }
            else System.out.println(string_costant.error);
        }
        server.showGameField();
    }


    private void manageGame() throws IOException, InterruptedException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        int decision;
        while (!miniModel.getState().equals("END_GAME")) {
            if (miniModel.getState().equals("WAIT_TURN")) {
                decision = -1;
                System.out.println("\n[ NOT YOUR TURN ] ");
               while(miniModel.getState().equals("WAIT_TURN")){
                   if(decision != 3){
                       miniModel.printMenu("GO IN BUFFERING");
                       do{
                           decision = scan.nextInt();
                           scan.nextLine();
                       }while(!menuChoice(decision));
                   }else{buffering();}
               }
            }
            if (miniModel.getState().equals("PLACE_CARD")) {
                System.out.println("\n[ IT'S YOUR TURN ] ");
                selectAndInsertCard();

            }
            if(miniModel.getState().equals("DRAW_CARD")) {
                System.out.println("\n[ IT'S YOUR TURN ] ");
                decision = -1;
                while( decision != 3 ){
                    miniModel.printMenu("DRAW A CARD");
                    do{
                        decision = scan.nextInt();
                        scan.nextLine();
                    }while( !menuChoice(decision) );
                }
                drawCard();
            }

            server.getPoint();
            flag_check = true;
            while(flag_check){
            Thread.sleep(100);
            }
            System.out.println("end of your turn, you have a total of : " +  point + "points");

        }


        System.out.println("End of the GAME, completare questa parte");


    }

    public void selectAndInsertCard() throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        int decision;

        while (miniModel.getState().equals("PLACE_CARD")) {
            decision = -1;
            while (decision != 3) {
                miniModel.printMenu("PLACE A CARD");
                do {
                    decision = scan.nextInt();
                    scan.nextLine();
                } while (!menuChoice(decision));
            }

            System.out.println("\nCHOOSE CARD FROM YOUR DECK (1,2,3): ");
            String choicestring = scan.nextLine();
            int choice = Integer.parseInt(choicestring);
            if (choice >= 1 && choice <= 3) {
                System.out.println("\nCHOOSE SIDE (B,F): ");
                String flip = scan.nextLine();
                if (flip.equals("B") || flip.equals("F") || flip.equals("b") || flip.equals("f")) {
                    boolean flipped = false;
                    if (flip.equals("B") || flip.equals("b")) {
                        flipped = true;
                    }
                    System.out.println("\nCHOOSE COORDINATES: ");
                    int x = scan.nextInt();
                    int y = scan.nextInt();
                    scan.nextLine();
                    if (x >= 0 && x < Constants.MATRIXDIM && y >= 0 && y < Constants.MATRIXDIM) {
                        server.placeCard(choice, x, y, flipped);
                         place_flag_check = true;
                        while(place_flag_check){
                            Thread.sleep(750);
                            buffering();
                        }

                    } else {
                        System.err.println("\n[COORDINATES OUT OF BOUND]!");
                    }
                } else {
                    System.err.println("\n[ONLY 'B' OR 'F' ALLOWED]!");
                }
            } else {
                System.err.println("\n[OUT OF BOUND CARD]");
            }
        }

    }

    private void drawCard() throws IOException, ClassNotFoundException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n DRAW A CARD FROM: ");


        server.getGoldDeckSize();
        this.flag_check = true;
        while(flag_check){
            Thread.sleep(100);
        }
        if(checkSizeGoldDeck){
            System.out.println("1. GOLD DECK");
        }

        server.getResourcesDeckSize();
        this.flag_check = true;
        while(flag_check){
            Thread.sleep(100);
        }
        if (checkSizeResourcesDeck){
            System.out.println("2. RESOURCE DECK");
        }

        System.out.println("3  CENTRAL CARDS DECK");

        String numstring = scan.nextLine();
        int num = Integer.parseInt(numstring);
        boolean done = false;
        while(!done){
            if(num==1){
                done = true;
                server.peachFromGoldDeck();
                Thread.sleep(100);
            } else if (num==2) {
                done = true;
                server.peachFromResourcesDeck();
                Thread.sleep(100);
            } else if (num==3) {
                done=true;
                 server.getCardsInCenter();
                System.out.println("Scegli indice carta da pescare: ");
                String choicestr = scan.nextLine();
                int index = Integer.parseInt(choicestr);
                server.peachFromCardsInCenter(index-1);
                Thread.sleep(100);
            } else{
                System.out.println("\n Inserimento errato!");
            }
        }
    }









    /*
        public void showValue(Integer number) {
            // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
            System.out.print("\n= " + number + "\n> ");
        }


     */

    public void reportError(String details) {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    @Override
    public void reportMessage(String details) throws IOException {

    }

    public void showValue(String message) {
        System.out.println(message);
    }

    @Override
    public void showUpdate(GameField game_field) throws IOException {

    }


    public void showCard(PlayCard card) throws IOException {
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

    @Override
    public void pushBack(ResponseMessage message) throws IOException {

    }

    public void showField(GameField field) throws IOException {
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
    public void printString(String s) throws IOException {

    }

    @Override
    public void setGameField(List<GameField> games) throws IOException {

    }

    @Override
    public MiniModel getMiniModel() throws IOException {
        return null;
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

    public void showCardInCenter(PlayCard card) throws IOException {

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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String host = "127.0.0.1";
        int port = 12345;

        Socket serverSocket = new Socket(host, port);
        try{

            ObjectOutputStream outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(serverSocket.getInputStream());

            new Client(inputStream, outputStream).run();
        }catch (IOException | InterruptedException e) {
            System.out.println("impossibile creare socket input / output");
            return;
        }

    }




}

