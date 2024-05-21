package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.PlayerState;
import it.polimi.ingsw.MODEL.Game.IndexRequestManagerF;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.*;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ErrorMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.GameFieldMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.ResponseMessage;
import it.polimi.ingsw.RMI_FINAL.MESSAGES.UpdateMessage;
import it.polimi.ingsw.StringCostant;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class RmiClientF extends UnicastRemoteObject implements VirtualViewF {
    final VirtualServerF server;
    private String token;
    private VirtualGameServer rmi_controller;
    private boolean newClient;
    private MiniModel miniModel =  new MiniModel();
    private StringCostant stringcostant = new StringCostant();
    

    public RmiClientF(VirtualServerF server) throws IOException {
        this.server = server;
    }

    public void run() throws IOException, InterruptedException, NotBoundException {
        this.server.connect(this);
        runCli();
    }

    private void runCli() throws IOException, InterruptedException, NotBoundException {
        String player_name = selectNamePlayer();
        gameAccess(player_name);
        startSendingHeartbeats();
        waitFullGame();
        chooseGoalState();
        chooseStartingCardState();
        manageGame();
    }


    //GAME FLOW

    private String selectNamePlayer() throws IOException, NotBoundException {
        Scanner scan = new Scanner(System.in);
        String player_name = " ";
        String isnew;
        boolean flag;
        do{
            System.out.print(stringcostant.choose_name_player);
            player_name = scan.nextLine();
            isnew = server.checkName(player_name,this);
            if(isnew.equals("true")) {
                flag = true;
                newClient = true;
                this.token = server.createToken(this);}
            else if(isnew.equals("false")){
                flag=false;
                System.out.println(stringcostant.name_is_not_valid);}
            else{
                this.token = isnew;
                int port = server.getPort(token);
                Registry registry = LocateRegistry.getRegistry("192.168.39.78", port);
                this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                rmi_controller.connectRMI(this);
                flag=true;
                newClient = false;
                startSendingHeartbeats();
                System.out.println(player_name + " RECONNETED!");
            }
        } while(!flag);
        return player_name;
    }
    private void gameAccess(String player_name) throws IOException, NotBoundException {
        if(newClient) {
            makeChoice(player_name);
            System.out.print("[SUCCESS] YOUR PLAYER HAS BEEN CREATED!\n");}
    }
    private void makeChoice(String player_name) throws IOException, NotBoundException {
        if (server.getFreeGames() == null || server.getFreeGames().isEmpty()) {
            newGame(player_name,false);
            return;
        }
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {
            System.out.println("\n-'new' CREATE NEW GAME \n-'old' JOIN EXISTING GAME");
            String decision = scan.nextLine();
            if (decision.equalsIgnoreCase("old")) {
                done = 1;
                chooseMatch(player_name);
            } else if (decision.equalsIgnoreCase("new")) {
                done=1;
                newGame(player_name,true);
            } else {
                System.out.println("\n[ERROR] WRONG INSERT!");
            }
        }
    }
    private void chooseMatch(String player_name) throws IOException, NotBoundException {
        Scanner scan = new Scanner(System.in);
        boolean check;
        System.out.println("\nEXISTING GAMES: ");
        List<GameServer> partite = server.getFreeGames();
        for ( VirtualGameServer r : partite) {
            System.out.println( r.getController().getGame().getName() + " ID:" + r.getController().getGame().getIndex_game()
                    + " " + r.getController().getGame().getNumPlayer() + "/" + r.getController().getGame().getMax_num_player() );
        }
        do {
            System.out.println("\nINSERT GAME ID > ");
            int ID = scan.nextInt();
            check = server.findRmiController(ID, token, player_name,this);
        }while(!check);
        int port = server.getPort(token);
        Registry registry = LocateRegistry.getRegistry("192.168.39.78", port);
        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
        rmi_controller.connectRMI(this);
    }
    private void newGame(String player_name, boolean empty) throws IOException {
        Scanner scan = new Scanner(System.in);
        if( !empty ) System.out.println("\nTHERE AREN'T EXISTING GAMES");
        System.out.print("\nCHOOSE GAME NAME  > ");
        String game_name = scan.nextLine();
        int numplayers;
        boolean flag;
        do {
            flag = false;
            System.out.print("\nCHOOSE NUMBER OF PLAYERS ( 2 - 4 ) > ");
            numplayers = scan.nextInt();

            try {
                int port;
                port = server.createGame(game_name, numplayers, token, player_name,this);
                Registry registry = LocateRegistry.getRegistry("192.168.39.78", port);
                this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
                rmi_controller.connectRMI(this);

            } catch (ControllerException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        } while(flag);
    }
    
    private void waitFullGame() throws IOException, InterruptedException {
        
        Scanner scan = new Scanner(System.in);
        if(miniModel.getState().equals("NOT_INITIALIZED")) {
            System.out.print("[WAIT FOR OTHER PLAYERS]\n");
            while (miniModel.getState().equals("NOT_INITIALIZED")) {
                buffering();
            }
            System.out.println("\n[GAME IS FULL, YOU ARE ABOUT TO START]!\n");
        }
        miniModel.setGameField(rmi_controller.getGameFields(token));
        startCheckingMessages();
    }
    private void chooseGoalState() throws IOException, InterruptedException {
        if(miniModel.getState().equals("CHOOSE_GOAL")) {
            if(rmi_controller.getTtoP().get(token).getGoalCard()==null) {
                chooseGoal();
                System.out.println("\nYOU CHOOSE :" + rmi_controller.getTtoP().get(token).getGoalCard().toString());
            }
            while (miniModel.getState().equals("CHOOSE_GOAL")) {
                buffering();
            }
        }
    }
    private void chooseStartingCardState() throws IOException, InterruptedException {
        if(miniModel.getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            if(!rmi_controller.getTtoP().get(token).isFirstPlaced()) {
                chooseStartingCard();
            }
            while (miniModel.getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
                buffering();
            }
        }
    }
    private void chooseGoal() throws IOException {
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {
            System.out.println("\nCHOOSE YOUR GOAL:\n 1-" + rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(0).toString()
                    + "\n 2-" + rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(1).toString());
            String choice = scan.nextLine();
            if (choice.equals("1")) {
                done=1;
                rmi_controller.chooseGoal(token,0);
            } else if (choice.equals("2")){
                done=1;
                rmi_controller.chooseGoal(token,1);
            } else System.out.println("[ERROR] WRONG INSERT!");
        }
    }
    private void chooseStartingCard() throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("\nCHOOSE STARTING CARD SIDE:\n");
        rmi_controller.showStartingCard(token);
        int done=0;
        while(done==0){
            System.out.println("\n-'B' FOR BACK SIDE \n-'F' FOR FRONT SIDE:");
            String dec = scan.nextLine();
            if (dec.equals("F")||dec.equals("f")){
                done=1;
                rmi_controller.chooseStartingCard(token,false);
            } else if (dec.equals("B")||dec.equals("b")){
                done=1;
                rmi_controller.chooseStartingCard(token,true);
            }
            else System.out.println("[ERROR] WRONG INSERT!");
        }
    }
    private void manageGame() throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        int decision;
        while(!miniModel.getState().equals("END_GAME")) {
            if (miniModel.getState().equals("WAIT_TURN")) {
                decision = -1;
                System.out.println("\n[ NOT YOUR TURN ] ");
                while (miniModel.getState().equals("WAIT_TURN")) {
                    if( decision != 3 ){
                        miniModel.printMenu("GO IN BUFFERING");
                        do{
                            decision = scan.nextInt();
                            scan.nextLine();
                        }while( !menuChoice(decision));
                    }else{buffering();}
                }
            }
            if (miniModel.getState().equals("PLACE_CARD")) {
                System.out.println("\n[ IT'S YOUR TURN ] ");
                selectAndInsertCard();
            }
            if (miniModel.getState().equals("DRAW_CARD")) {
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
            rmi_controller.getPoints(token);
            System.out.println("\nEND OF YOUR TURN !");
        }
        System.out.println("[END OF THE GAME]!\nFINAL SCORES:\n");
        rmi_controller.getFinalStandings(token);
    }
    private void selectAndInsertCard() throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        int decision;
        while( miniModel.getState().equals("PLACE_CARD") ) {
            decision = -1;
            while( decision != 3 ) {
                miniModel.printMenu("PLACE A CARD");
                do {
                    decision = scan.nextInt();
                    scan.nextLine();
                } while (!menuChoice(decision));
            }

            System.out.println("\nCHOOSE CARD FROM YOUR DECK (1,2,3): ");
            String choicestring = scan.nextLine();
            int choice = Integer.parseInt(choicestring);
            if(choice>=1 && choice<=3){
                System.out.println("\nCHOOSE SIDE (B,F): ");
                String flip = scan.nextLine();
                if(flip.equals("B") || flip.equals("F")||flip.equals("b")||flip.equals("f")){
                    boolean flipped = false;
                    if(flip.equals("B")||flip.equals("b")){flipped = true;}
                    System.out.println("\nCHOOSE COORDINATES: ");
                    int x = scan.nextInt();
                    int y = scan.nextInt();
                    scan.nextLine();
                    if(x>=0 && x<Constants.MATRIXDIM && y>=0 && y<Constants.MATRIXDIM){
                        SendFunction function = new SendInsertCard(token, choice-1, x,y,flipped);
                        rmi_controller.addQueue(function);
                        Thread.sleep(750);
                    } else{System.err.println("\n[COORDINATES OUT OF BOUND]!");}
                } else{System.err.println("\n[ONLY 'B' OR 'F' ALLOWED]!");}
            } else{System.err.println("\n[OUT OF BOUND CARD]");}
        }
    }

    private void drawCard() throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n DRAW A CARD FROM: ");
        if(rmi_controller.getController().getGame().getGold_deck().getNumber()>0){
            System.out.println("1. GOLD DECK");
        }
        if (rmi_controller.getController().getGame().getResources_deck().getNumber()>0){
            System.out.println("2. RESOURCE DECK");
        }
        System.out.println("3. CENTRAL CARDS DECK");
        String numstring = scan.nextLine();
        int num = Integer.parseInt(numstring);
        boolean done = false;
        while(!done){
            if(num==1){
                done = true;
                SendFunction function = new SendDrawGold(token);
                rmi_controller.addQueue(function);
            } else if (num==2) {
                done = true;
                SendFunction function = new SendDrawResource(token);
                rmi_controller.addQueue(function);
            } else if (num==3) {
                done=true;
                showCardsInCenter();
                System.out.println("CHOSE CARD FROM CENTER (1/2/3 ) : ");
                String choicestr = scan.nextLine();
                int index = Integer.parseInt(choicestr);
                SendFunction function = new SendDrawCenter(token, index-1);
                rmi_controller.addQueue(function);
            } else{
                System.err.println("\n[OUT OF BOUND] WRONG INSERT! ONLY 1/2/3 ALLOWED");
            }
            Thread.sleep(500);
        }
    }



    // THREADS

    private void startCheckingMessages() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    ResponseMessage s = miniModel.popOut();
                    if(s!=null){
                        if( s instanceof  GameFieldMessage){
                            showField(((GameFieldMessage) s).getField());
                        }
                        if ( s instanceof ErrorMessage){
                            printString(s.getMessage());
                        }
                        if ( s instanceof UpdateMessage){
                            printString(s.getMessage());
                        }
                    }
                } catch (InterruptedException e) {
                    System.err.println("impossible to pop out");
                }
            }
        }).start();
    }

    private void startSendingHeartbeats() {
        new Thread(() -> {
            int cracked = 0;
            while (true) {
                try {
                    Thread.sleep(100);
                    server.receiveHeartbeat(token);
                } catch (IOException | InterruptedException e) {
                    if(cracked==0) {
                        cracked = 1;
                        System.err.println("Network disconnected");
                    }
                }
            }
        }).start();
    }



    //SETTERS

    public void setGameField(List<GameField> games){miniModel.setGameField(games);}
    public void setCards(List<PlayCard> cards){miniModel.setCards(cards);}
    public void pushBack(ResponseMessage message){miniModel.pushBack(message);}
    public void setState(String state){ miniModel.setState(state);}
    public void setNumToPlayer(HashMap<Integer, String> map){miniModel.setNumToPlayer(map);}


    // HELP FUNCTIONS
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
                miniModel.uploadChat();
                int decision;
                do{
                    decision = scan.nextInt();
                    scan.nextLine();
                }while( !chatChoice(decision));

                break;
            case ( 3 ):
                return true;}
        return true;
    }

    private boolean chatChoice(int decision) throws IOException {
        Scanner scan = new Scanner(System.in);
        if(!miniModel.showchat(decision)){
            return false;
        }
        int choice = 0;
        while(true) {
            while (choice < 1 || choice > 2) {
                System.out.println("Do you want to send a message?");
                System.out.println("1- Yes");
                System.out.println("2- No, close the chat");
                choice = scan.nextInt();
                scan.nextLine();
            }
            if (choice == 1) {
                System.out.println("Insert message: ");
                String message = scan.nextLine();
                if(decision==miniModel.getNum_players()+1){
                    rmi_controller.chattingGlobal(new ChatMessage(message, miniModel.getMy_player()));
                }
                else{
                    rmi_controller.chattingMoment(miniModel.getMy_index(), decision, new ChatMessage(message, miniModel.getMy_player()));
                }
                System.out.println("Message sent successfully!");
                miniModel.showchat(decision);
                choice = 0;
            } else {
                return true;
            }
        }

    }

    private ChatMessage insertMessage() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert message to send:");
        String s = scan.nextLine();
        return new ChatMessage(s);
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
    @Override
    public void showUpdate(GameField gamefield) {}
    @Override
    public void reportError(String details) {System.err.print("\n[ERROR] " + details + "\n> ");}
    @Override
    public void reportMessage(String details) {System.out.print("\n[ERROR] " + details + "\n> ");}
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
    public MiniModel getMiniModel() {return miniModel;}
    private void showCardsInCenter() throws IOException {rmi_controller.showCardsInCenter(token);}
    public void printString(String s) {System.out.println(s);}

    public void addChat(int idx, ChatMessage message) throws RemoteException{
        miniModel.addChat(idx, message);
    }

    public void insertId(int id) throws RemoteException{
        miniModel.setMy_index(id);
    }

    public void insertNumPlayers(int numPlayersMatch) throws RemoteException{
        miniModel.setNum_players(numPlayersMatch);
    }

    public void insertPlayer(Player player) throws RemoteException{
        miniModel.setMy_player(player);
    }


    //MAIN
    public static void main(String[] args) throws IOException, NotBoundException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1);
        VirtualServerF server = (VirtualServerF) registry.lookup("VirtualServer");
        new RmiClientF(server).run();
    }
}

