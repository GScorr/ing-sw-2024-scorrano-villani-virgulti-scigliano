package it.polimi.ingsw.VIEW;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.MiniModel;
import it.polimi.ingsw.RMI_FINAL.GameServer;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.RMI_FINAL.VirtualGameServer;
import it.polimi.ingsw.RMI_FINAL.VirtualViewF;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;
import it.polimi.ingsw.StringCostant;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TUI implements Serializable {
    private VirtualViewF client;

    private StringCostant stringcostant = new StringCostant();
    private boolean newClient;

    public List<Integer> id_games = new ArrayList<>();

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

    private void startSendingHeartbeats() {
        client.startSendingHeartbeats();
    }

    private void waitFullGame() throws IOException, InterruptedException {

        Scanner scan = new Scanner(System.in);
        if(client.getMiniModel().getState().equals("NOT_INITIALIZED")) {
            System.out.print("[WAIT FOR OTHER PLAYERS]\n");
            while (client.getMiniModel().getState().equals("NOT_INITIALIZED")) {
                buffering();
            }
            System.out.println("\n[GAME IS FULL, YOU ARE ABOUT TO START]!\n");
        }
        client.setGameFieldMiniModel();
        startCheckingMessages();
    }

    private void startCheckingMessages() {
        client.startCheckingMessages();
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
        boolean check = false;
        System.out.println("\nEXISTING GAMES: ");
        List<SocketRmiControllerObject> games = client.getFreeGames();
        for (SocketRmiControllerObject r : games) {
            System.out.println(r.name + " ID:" + r.ID
                    + " " + r.num_player + "/" + r.max_num_player);
            id_games.add(r.ID);
        }
        do {
            System.out.println("\nINSERT GAME ID > ");
            int ID = scan.nextInt();
            check = client.findRmiController(ID, player_name);
        }while(!check);
        client.connectGameServer();
    }

    private void chooseGoalState() throws IOException, InterruptedException {
        if(client.getMiniModel().getState().equals("CHOOSE_GOAL")) {
            if(client.isGoalCardPlaced()){
                chooseGoal();
                System.out.println("\nYOU CHOOSE :" + client.getGoalPlaced());
            }
            while (client.getMiniModel().getState().equals("CHOOSE_GOAL")) {
                buffering();
            }
        }
    }

    private void chooseGoal() throws IOException {
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {

            System.out.println("\nCHOOSE YOUR GOAL:\n 1-" + client.getFirstGoal()
                    + "\n 2-" + client.getSecondGoal());
            String choice = scan.nextLine();
            if (choice.equals("1")) {
                done=1;
                client.chooseGoal(0);
            } else if (choice.equals("2")){
                done=1;
                client.chooseGoal(1);
            } else System.out.println("[ERROR] WRONG INSERT!");
        }
    }

    private void chooseStartingCardState() throws IOException, InterruptedException {
        if(client.getMiniModel().getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            if(!client.isFirstPlaced()) {
                chooseStartingCard();
            }
            while (client.getMiniModel().getState().equals("CHOOSE_SIDE_FIRST_CARD")) {
                buffering();
            }
        }
    }

    private void chooseStartingCard() throws IOException{
        Scanner scan = new Scanner(System.in);
        System.out.println("\nCHOOSE STARTING CARD SIDE:\n");
        client.showStartingCard();
        int done=0;
        while(done==0){
            System.out.println("\n-'B' FOR BACK SIDE \n-'F' FOR FRONT SIDE:");
            String dec = scan.nextLine();
            if (dec.equals("F")||dec.equals("f")){
                done=1;
                client.chooseStartingCard(false);
            } else if (dec.equals("B")||dec.equals("b")){
                done=1;
                client.chooseStartingCard(true);
            }
            else System.out.println("[ERROR] WRONG INSERT!");
        }
    }

}
