package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.GameField;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;


public class RmiClientF extends UnicastRemoteObject implements VirtualViewF {
    final VirtualServerF server;
    private  String token;
    private boolean newClient;

    public RmiClientF(VirtualServerF server) throws RemoteException {
        this.server = server;
    }

    public void run() throws RemoteException, InterruptedException {
        this.server.connect(this);
        runCli();
    }

    private void runCli() throws RemoteException, InterruptedException {
        String player_name = selectNamePlayer();
        // Create a token associated with a client, in the rmi server we have a reference to TokenManagerImplement
        // which contains a map that associate the client with the token, and we also have a map in server that
        // associate the token with the player
        // < RmiClient , TOKEN > < TOKEN , Player >
        //System.out.print("\nToken Player > " + this.token);
        gameAccess(player_name);
        startSendingHeartbeats();
        waitFullGame();
        chooseGoalState();
        chooseStartingCardState();

    }

    private void gameAccess(String player_name) throws RemoteException {
        if(newClient) {
            if (server.getFreeGames() == null || server.getFreeGames().isEmpty()) {
                newGame_notavailable(player_name);
            } else {
                makeChoice(player_name);
            }
            System.out.print("creazione Player andata a buon fine!\n");
        }
    }

    private String selectNamePlayer() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        String player_name = " ";
        String isnew;
        boolean flag;
        // Creo giocatore
        do{
            System.out.print("\nScegli nome Player > ");
            player_name = scan.nextLine();
            isnew = server.checkName(player_name);
            if(isnew.equals("true")) {
                flag = true;
                newClient = true;
                this.token = server.createToken(this);
            }
            else if(isnew.equals("false")){
                flag=false;
                System.out.println("Giocatore già presente, reinserisci nome!");
            }
            else{
                this.token = isnew;
                flag=true;
                newClient = false;
                System.out.println(player_name + " riconnesso!");
            }
        } while(!flag);
        return player_name;
    }

    private void chooseStartingCardState() throws RemoteException, InterruptedException {
        if(server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            if(!server.getRmiController(token).getTtoP().get(token).isFirstPlaced()) {
                chooseStartingCard();
            }
            while (server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD")) {
                buffering();
            }
        }
    }

    private void chooseGoalState() throws RemoteException, InterruptedException {
        if(server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_GOAL")) {
            if(server.getRmiController(token).getTtoP().get(token).getGoalCard()==null) {
                chooseGoal();
                System.out.println("\nHai scelto :" + server.getRmiController(token).getTtoP().get(token).getGoalCard().toString());
            }
            while (server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_GOAL")) {
                buffering();
            }
        }
    }

    private void waitFullGame() throws RemoteException, InterruptedException {
        if(server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("NOT_INITIALIZED")) {
            System.out.print("Aspetta il riempimento partita -");
            while (server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("NOT_INITIALIZED")) {
                buffering();
            }
            System.out.println("\nEhi la tua partita è piena!\n");
        }
    }

    private void startSendingHeartbeats() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                    server.receiveHeartbeat(token);
                } catch (RemoteException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void chooseStartingCard() throws RemoteException{
        Scanner scan = new Scanner(System.in);
        System.out.println("\nScegli lato carta iniziale:\n");
        server.showStartingCard(token);
        int done=0;
        while(done==0){
            System.out.println("\nInserisci B per scegliere Back Side o F per scegliere Front side:");
            String dec = scan.nextLine();
            if (dec.equals("F")){
                done=1;
                server.chooseStartingCard(token,false);
            } else if (dec.equals("B")){
                done=1;
                server.chooseStartingCard(token,true);
            }
            else System.out.println("Inserimento errato!");
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

    private void chooseGoal() throws RemoteException{
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {
            System.out.println("\nScegli obiettivo tra:\n 1-" + server.getRmiController(token).getTtoP().get(this.token).getInitial_goal_cards().get(0).toString()
                    + "\n 2-" + server.getRmiController(token).getTtoP().get(this.token).getInitial_goal_cards().get(1).toString());
            String choice = scan.nextLine();
            if (choice.equals("1")) {
                done=1;
                server.chooseGoal(token,0);
            } else if (choice.equals("2")){
                done=1;
                server.chooseGoal(token,1);
            } else System.out.println("Inserimento errato!");
        }
    }

    private void makeChoice(String player_name) throws RemoteException{
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {
            System.out.println("\nDigita 'new' per creare una nuova partita, 'old' per entrare in una delle partite disponibili");
            String decision = scan.nextLine();
            if (decision.equalsIgnoreCase("old")) {
                done = 1;
                chooseMatch(player_name);
            } else if (decision.equalsIgnoreCase("new")) {
                done=1;
                newGame(player_name);
            } else {
                System.out.println("\nInserimento errato!");
            }
        }
    }

    private void chooseMatch(String player_name) throws RemoteException {
        Scanner scan = new Scanner(System.in);
        boolean check;
            System.out.println("\nElenco partite disponibili: ");
            List<RmiController> partite = server.getFreeGames();

            for (RmiController r : partite) {
                System.out.println(r.getController().getGame().getName() + " ID:" + r.getController().getGame().getIndex_game()
                        + " " + r.getController().getGame().getNumPlayer() + "/" + r.getController().getGame().getMax_num_player());
            }
            do {
                System.out.println("\nInserisci ID partita in cui entrare");
                int ID = scan.nextInt();
                check = server.findRmiController(ID, token, player_name);
            }while(!check);

    }

    private void newGame(String player_name) throws RemoteException {
        Scanner scan = new Scanner(System.in);
        System.out.print("\nScegli nome Partita > ");
        String game_name = scan.nextLine();
        int numplayers=4;
        boolean flag;
        do {
            flag = false;
            System.out.print("\nScegli numero giocatori partita (da 2 a 4) > ");
            numplayers = scan.nextInt();
            try {
                server.createGame(game_name, numplayers, token, player_name);
            } catch (ControllerException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            }
        } while(flag);
    }

    private void newGame_notavailable(String playerName) throws RemoteException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nNon esiste nessuna partita disponibile, creane una nuova!");
        System.out.print("\nScegli nome Partita > ");
        String game_name = scan.nextLine();
        boolean flag;
        do {
            flag = false;
            System.out.print("\nScegli numero giocatori partita (da 2 a 4) > ");
            int numplayers = scan.nextInt();
            try {
                server.createGame(game_name, numplayers, token, playerName);
            } catch (ControllerException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            }
        } while(flag);
    }

    @Override
    public void showUpdate(GameField gamefield) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    @Override
    public void reportMessage(String details) throws RemoteException {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.err.print("\n[ERROR] " + details + "\n> ");

    }

    @Override
    public void showCard(PlayCard card) throws RemoteException {
        Side back = card.getBackSide();
        Side front = card.getFrontSide();

        System.out.println("BACK SIDE\n--------------------------");
        System.out.println( " | " + back.getAngleLeftUp().toString().charAt(0)  +   " |               "+ " | " + back.getAngleRightUp().toString().charAt(0) + " |\n " );
        //System.out.println( " | " + back.getAngleRightUp().toString().charAt(0) + " |\n " );
        System.out.println( " |       | " + back.getCentral_resource().toString().charAt(0) + back.getCentral_resource2().toString().charAt(0) + back.getCentral_resource3().toString().charAt(0) + " |         |\n " );
        System.out.println( " | " + back.getAngleLeftDown().toString().charAt(0) +  " |               " + " | " + back.getAngleRightDown().toString().charAt(0) + " |\n " );
        //System.out.println(  );
        System.out.println("--------------------------\n\n");

        System.out.println("FRONT SIDE\n--------------------------");

        if(card instanceof ResourceCard) {
            System.out.println( " | " + card.getPoint() + " | ");
            if ( card instanceof GoldCard ){
                System.out.println("  " + ((GoldCard) card).getPointBonus().toString().charAt(0)  + "  " + " | " + front.getAngleRightUp().toString().charAt(0) + " |\n ");
            }
        }else{
            System.out.println( " | " + front.getAngleLeftUp().toString().charAt(0)  + " | " + "             | " + front.getAngleRightUp().toString().charAt(0) + " |\n ");
        }
        //System.out.println( " | " + front.getAngleRightUp().toString().charAt(0) + " |\n " );
        System.out.println( " |       | " + front.getCentral_resource().toString().charAt(0) + front.getCentral_resource2().toString().charAt(0) + front.getCentral_resource3().toString().charAt(0) + " |         |\n " );
        //System.out.println( " | " + front.getAngleLeftDown().toString().charAt(0) + " |       " );
        if ( card instanceof GoldCard ){
            System.out.println( " | " + front.getAngleLeftDown().toString().charAt(0) + " |       " );
            System.out.println("  " + card.getCostraint().toString().charAt(0)  + "  " + " | " + front.getAngleRightDown().toString().charAt(0) + " |\n ");
        }else{
            System.out.println( " | " + front.getAngleLeftDown().toString().charAt(0) + " |              " + " | " + front.getAngleRightDown().toString().charAt(0) + " |\n " );
        }
        //System.out.println( " | " + front.getAngleRightDown().toString().charAt(0) + " |\n " );
        System.out.println("--------------------------\n\n");

    }



    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        VirtualServerF server = (VirtualServerF) registry.lookup("VirtualServer");
        new RmiClientF(server).run();
    }
}

//todo riconnessione gianni





















































































//todo
// disconnessione e subito riconnessione con due client stesso nome
