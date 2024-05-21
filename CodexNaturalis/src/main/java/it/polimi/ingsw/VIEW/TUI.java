package it.polimi.ingsw.VIEW;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.ChatMessage;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawCenter;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawGold;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendDrawResource;
import it.polimi.ingsw.RMI_FINAL.FUNCTION.SendFunction;
import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;
import it.polimi.ingsw.StringCostant;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class TUI implements Serializable {
    private VirtualViewF client;
    private StringCostant stringcostant = new StringCostant();
    private boolean newClient;

    public TUI(VirtualViewF client) {
        this.client = client;
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

    private String selectNamePlayer() throws IOException, NotBoundException {
        Scanner scan = new Scanner(System.in);
        String player_name = " ";
        String isnew;
        int flag;
        do{
            System.out.print(stringcostant.choose_name_player);
            player_name = scan.nextLine();
            flag = client.checkName(player_name);
            if(flag==0){
                System.out.println(stringcostant.name_is_not_valid);
                newClient=false;
            }
            else if(flag==2) {
                System.out.println(player_name + " RECONNETED!");
                newClient=true;
            }
            else{
                newClient=true;
            }
        } while(flag==0);
        return player_name;
    }

    private void gameAccess(String player_name) throws IOException, NotBoundException {
        if(newClient) {
            makeChoice(player_name);
            System.out.print("[SUCCESS] YOUR PLAYER HAS BEEN CREATED!\n");}
    }

    private void makeChoice(String player_name) throws IOException, NotBoundException {
        if (!client.areThereFreeGames()) {
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

    private void newGame(String player_name, boolean empty) throws IOException, NotBoundException {
        Scanner scan = new Scanner(System.in);
        if( !empty ) System.out.println("\nTHERE AREN'T EXISTING GAMES");
        System.out.print("\nCHOOSE GAME NAME  > ");
        String game_name = scan.nextLine();
        int numplayers;
        boolean flag = false;
        do {
            System.out.print("\nCHOOSE NUMBER OF PLAYERS ( 2 - 4 ) > ");
            numplayers = scan.nextInt();
            if(numplayers>=2 && numplayers<=4){
                client.createGame(game_name,numplayers, player_name);
                flag=true;
            }
            else{
                System.out.println("Wrong number, please insert again");
            }
        } while(!flag);
    }

    private void chooseMatch(String player_name) throws IOException, NotBoundException {
        Scanner scan = new Scanner(System.in);
        boolean check;
        System.out.println("\nEXISTING GAMES: ");
        List<SocketRmiControllerObject> partite = client.getFreeGames();
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
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
        this.rmi_controller = (VirtualGameServer) registry.lookup(String.valueOf(port));
        rmi_controller.connectRMI(this);
    }




    private void manageGame() throws IOException, InterruptedException {
        boolean end = false;
        while( !client.getMiniModel().getState().equals("END_GAME") ){
            while (client.getMiniModel().getState().equals("WAIT_TURN")) {
                menuChoice("KEEP WAITING");
                buffering();
            }
            if (client.getMiniModel().getState().equals("PLACE_CARD")) {selectAndInsertCard();}
            else if (client.getMiniModel().getState().equals("DRAW_CARD")) {
                menuChoice("DRAW CARD");
                drawCard();
            }
            System.out.println("\nEND OF YOUR TURN !");
            client.manageGame(end);
        }
        end = true;
        System.out.println("[END OF THE GAME]!\nFINAL SCORES:\n");
        client.manageGame(end);
    }

    private void selectAndInsertCard() throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        int choice,x,y;
        String flip;
        boolean flipped;
        while ( client.getMiniModel().getState().equals("PLACE_CARD") ){
            menuChoice("PLACE CARD");
            do{
                System.out.println("\nCHOOSE CARD FROM YOUR DECK (1,2,3): ");
                String choicestring = scan.nextLine();
                choice = Integer.parseInt(choicestring);
                System.out.println("\nCHOOSE SIDE (B,F): ");
                flip = scan.nextLine();
                if( flip.equals('B') || flip.equals('b') ) flipped = true;
                else flipped = false;
                System.out.println("\nCHOOSE COORDINATES: ");
                x = scan.nextInt();
                y = scan.nextInt();
                scan.nextLine();
            }while( !(choice>=1 && choice<=3) ||
                    !(flip.equals("B") || flip.equals("F")||flip.equals("b")||flip.equals("f") ) ||
                    !(x>=0 && x< Constants.MATRIXDIM && y>=0 && y<Constants.MATRIXDIM ));
            client.selectAndInsertCard(choice,x,y,flipped);
        }
    }

    private void drawCard() throws IOException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n DRAW A CARD FROM: ");
        if(client.getGameServer().getController().getGame().getGold_deck().getNumber()>0){System.out.println("1. GOLD DECK");}
        if (client.getGameServer().getController().getGame().getResources_deck().getNumber()>0){System.out.println("2. RESOURCE DECK");}
        System.out.println("3. CENTRAL CARDS DECK");
        int num = -1;
        SendFunction function = null;
        do{
            if(num != -1) System.err.println("[ERROR] OUT OF BOUND");
            String numstring = scan.nextLine();
            num = Integer.parseInt(numstring);
            switch (num){
                case(1):
                     function = new SendDrawGold(client.getToken());
                    break;
                case(2):
                     function = new SendDrawResource(client.getToken());
                    break;
                case(3):
                    showCardsInCenter();
                    System.out.println("CHOSE CARD FROM CENTER (1/2/3 ) : ");
                    String choicestr = scan.nextLine();
                    int index = Integer.parseInt(choicestr);
                    function = new SendDrawCenter(client.getToken(), index-1);
                    break;
            }
        }while ( num < 0 || num > 3);
        client.drawCard(function);
    }


    private void menuChoice(String message) throws IOException {
        Scanner scan = new Scanner(System.in);
        do {
            client.getMiniModel().printMenu(message);
            int choice = scan.nextInt();
            switch (choice) {
                case (0):
                    client.getMiniModel().printNumToField();
                    Integer i = scan.nextInt();
                    client.getMiniModel().showGameField(i);
                    break;
                case (1):
                    client.getMiniModel().showCards();
                    break;
                case (2):
                    client.getMiniModel().uploadChat();
                    int decision;
                    do {
                        decision = scan.nextInt();
                        scan.nextLine();
                    } while (!chatChoice(decision));
                    break;
                case (3):
                    return;
                default:
                    System.err.println("[ERROR] INSERIMENTO ERRATO");
            }
        }while(true);
    }

    private boolean chatChoice(int decision) throws IOException {
        Scanner scan = new Scanner(System.in);
        if(!client.getMiniModel().showchat(decision)){
            return false;
        }
        int choice = 0;
        while(true) {
            while (choice < 1 || choice > 2) {
                System.out.println("DO YOU WANT TO SEND A MESSAGE?");
                System.out.println("1- YES");
                System.out.println("2- NO [CLOSE CHAT]");
                choice = scan.nextInt();
                scan.nextLine();
            }
            if (choice == 1) {
                System.out.println("INSERT MESSAGE: ");
                String message = scan.nextLine();
                client.ChatChoice(message, decision);
                System.out.println("[SUCCESS] MESSAGE SENT!");
                client.getMiniModel().showchat(decision);
                choice = 0;
            } else return true;
        }

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

    private void showCardsInCenter() throws IOException {client.getGameServer().showCardsInCenter(client.getToken());}
}
