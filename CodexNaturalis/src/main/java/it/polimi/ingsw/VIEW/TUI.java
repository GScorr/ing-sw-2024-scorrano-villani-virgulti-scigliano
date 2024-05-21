package it.polimi.ingsw.VIEW;

import it.polimi.ingsw.CONTROLLER.ControllerException;
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
}
