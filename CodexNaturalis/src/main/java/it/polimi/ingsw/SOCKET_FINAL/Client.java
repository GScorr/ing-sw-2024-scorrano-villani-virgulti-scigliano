package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Goal.Goal;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ErrorMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.GameFieldMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.StringCostant;


import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
public class Client implements VirtualView {

    StringCostant string_costant = new StringCostant();
    private MiniModel miniModel =  new MiniModel();
    final ServerProxy server;

    private boolean newClient;
    ObjectInputStream input;

    public Client(ObjectInputStream input, ObjectOutputStream output) throws IOException {
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
                    if (!((s = (ResponseMessage) input.readObject()) != null)) {
                        s.setMiniModel(miniModel);
                        //se non fa nulla è perchè potrebbe non aver preso l'overide
                        s.action();
                        if( s instanceof GameFieldMessage){
                            showField(((GameFieldMessage) s).getField());
                        }
                        if ( s instanceof ErrorMessage){
                            System.out.println(s.getMessage());
                        }
                    };
                } catch (IOException e) {
                    System.out.println("errore nel startCheckingMessages thread");
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
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
        startCheckingMessages();
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

    public void gameAccess(String player_name) throws IOException, ClassNotFoundException {
        if(newClient) {

            if (server.getFreeGame() == null || server.getFreeGame().isEmpty()) {
                newGame_notavailable(player_name);
            } else {
                makeChoice(player_name);
            }
            System.out.print(string_costant.creation_player);
        }
    }

    private void newGame_notavailable(String playerName) throws RemoteException {
        Scanner scan = new Scanner(System.in);
        System.out.println(string_costant.new_game_creation);
        String game_name = scan.nextLine();
        boolean flag;
        do {
            flag = false;
            System.out.print(string_costant.new_game_num_player);
            int numplayers = scan.nextInt();
            try {
                String result = server.createGame(game_name, numplayers, playerName);
                System.out.println(result);
            } catch (ControllerException | IOException | ClassNotFoundException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            }
        } while(flag);
    }

    private void makeChoice(String player_name) throws IOException, ClassNotFoundException {
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
            } else {
                System.out.println(string_costant.error);
            }
        }
    }

    private void newGame(String player_name) throws RemoteException {
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
                String result  = server.createGame(game_name, numplayers, player_name);
                System.out.println(result);
            } catch (ControllerException | IOException | ClassNotFoundException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            }
        } while(flag);
    }


    private void chooseMatch(String player_name) throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        boolean check;
        System.out.println(string_costant.list_game_avilable);
         List<SocketRmiControllerObject> games = server.getFreeGame();

        for (SocketRmiControllerObject r : games) {
            System.out.println(r.name + " ID:" + r.ID
                    + " " + r.num_player + "/" + r.max_num_player);
        }
        do {
            System.out.println(string_costant.Id_game);
            int ID = scan.nextInt();
            check = server.findRmiController(ID, player_name);
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

    private void buffering() throws RemoteException, InterruptedException{
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

    private void chooseGoalState() throws IOException, InterruptedException, ClassNotFoundException {
       if(server.getPlayerState().equals("CHOOSE_GOAL")){
           if(server.getGoalCard() == null){
               chooseGoal();
               Goal goal = server.getGoalCard();
               System.out.println("\n You choose :" + goal.toString());
           }
       }
       while(server.getPlayerState().equals("CHOOSE_GOAL")){
           buffering();
       }
    }

    private void chooseGoal() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {

            System.out.println("\nChoose Goal between :\n 1-" + server.getListGoalCard().get(0).toString()
                    + "\n 2-" + server.getListGoalCard().get(1).toString());

            String choice = scan.nextLine();
            if (choice.equals("1")) {
                done=1;
                server.chooseGoal(0);
            } else if (choice.equals("2")){
                done=1;
                server.chooseGoal(1);
            } else System.out.println("Index boundaries not respected!");
        }
    }

    private void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException {
        if(server.getPlayerState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            System.out.println(string_costant.starting_card);
            PlayCard starting_card =  server.getStartingCard();
            showCard(starting_card);

            if(!server.startingCardIsPlaced()) {
                chooseStartingCard();
            }
            while (server.getPlayerState().equals("CHOOSE_SIDE_FIRST_CARD")) {
                buffering();
            }
            GameField game_field = server.getGameField();
            showField(game_field);
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

        while (!server.getPlayerState().equals("END_GAME")) {
            if (server.getPlayerState().equals("WAIT_TURN")) {
                System.out.println(string_costant.waitTurn);
                while (server.getPlayerState().equals("WAIT_TURN")) {
                    buffering();
                }
            }
            if (server.getPlayerState().equals("PLACE_CARD")) {
                System.out.println(string_costant.insertHandsCard);
                List<PlayCard> cards_in_hand = server.getCardsInHand();
                for(PlayCard c : cards_in_hand){
                    showCard(c);
                }
                selectAndInsertCard();
            }
            if(server.getPlayerState().equals("DRAW_CARD")) {
                drawCard();
            }

            int point = server.getPoint();
            System.out.println("end of your turn, you have a total of : " +  point + "points");
        }

        System.out.println("End of the GAME, completare questa parte");


    }

    private void selectAndInsertCard() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        boolean done = false;
        while(!done) {
            System.out.println("\nChoosed index (1,2,3): ");
            String choicestring = scan.nextLine();
            int choice = Integer.parseInt(choicestring);
            if(choice>=1 && choice<=3){
                System.out.println("\nChoose the side (B,F): ");
                String flip = scan.nextLine();
                if(flip.equals("B") || flip.equals("F")){
                    boolean flipped = false;
                    if(flip.equals("B")){
                        flipped = true;
                    }
                    System.out.println("\nInserisci coordinate x e y di inserimento carta: ");
                    int x = scan.nextInt();
                    int y = scan.nextInt();
                    scan.nextLine();
                    if(x>=0 && x<Constants.MATRIXDIM && y>=0 && y<Constants.MATRIXDIM){
                            /*
                            done = server.placeCard(choice - 1, x, y, flipped);
                            System.out.println(done);
                             */

                    }
                    if( ! done){System.out.println("\nInserimento sbagliato!");}

                }
                else{
                    System.out.println("\nInserimento sbagliato!");
                }
            }
            else{
                System.out.println("\nInserimento sbagliato!");
            }
        }
        GameField game_field = server.getGameField();
        showField(game_field);
    }

    private void drawCard() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n Draw a card, deck available: ");
        if(server.getGoldDeckSize() > 0){
            System.out.println("1. Gold Deck");
        }
        if (server.getResourcesDeckSize() > 0){
            System.out.println("2. Resources Deck");
        }
        System.out.println("3  Center Cards");


        String numstring = scan.nextLine();
        int num = Integer.parseInt(numstring);
        boolean done = false;
        while(!done){
            if(num==1){
                done = true;
                server.peachFromGoldDeck();
            } else if (num==2) {
                done = true;
                server.peachFromResourcesDeck();
            } else if (num==3) {
                done=true;
                List<PlayCard> center_cards = server.getCardsInCenter();
                for(PlayCard c : center_cards){
                    showCardInCenter(c);
                }
                System.out.println("Scegli indice carta da pescare: ");
                String choicestr = scan.nextLine();
                int index = Integer.parseInt(choicestr);
                server.peachFromCardsInCenter(index-1);
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
    public void showValue(String message) {
        System.out.println(message);
    }


    public void showCard(PlayCard card) throws RemoteException {
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
    public void showField(GameField field) throws RemoteException {
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

    public void showCardInCenter(PlayCard card) throws RemoteException {

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

