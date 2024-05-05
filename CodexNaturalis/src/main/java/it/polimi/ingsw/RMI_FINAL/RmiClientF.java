package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.MODEL.Card.GoldCard;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.ResourceCard;
import it.polimi.ingsw.MODEL.Card.Side;
import it.polimi.ingsw.MODEL.ENUM.EdgeEnum;
import it.polimi.ingsw.MODEL.GameField;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;


public class RmiClientF extends UnicastRemoteObject implements VirtualViewF {
    final VirtualServerF server;
    private String token;
    private VirtualRmiController rmi_controller;
    private boolean newClient;

    public RmiClientF(VirtualServerF server) throws RemoteException {
        this.server = server;
    }

    public void run() throws RemoteException, InterruptedException, NotBoundException {
        this.server.connect(this);
        runCli();
    }

    private void runCli() throws RemoteException, InterruptedException, NotBoundException {
        String player_name = selectNamePlayer();
        gameAccess(player_name);
        startSendingHeartbeats();
        waitFullGame();
        chooseGoalState();
        chooseStartingCardState();
        manageGame();
    }

    private void manageGame() throws RemoteException, InterruptedException {
        if(rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("WAIT_TURN")) {
            System.out.println("\nAspetta il tuo turno ");
            while (rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("WAIT_TURN")) {
                buffering();
            }
        }
        if(rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("PLACE_CARD")) {
            System.out.println("\nInserisci la tua carta: ");
            while (rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("PLACE_CARD")) {
                //
            }
        }
    }

    private void gameAccess(String player_name) throws RemoteException, NotBoundException {
        if(newClient) {
            if (server.getFreeGames() == null || server.getFreeGames().isEmpty()) {
                newGame_notavailable(player_name);
            } else {
                makeChoice(player_name);
            }
            System.out.print("creazione Player andata a buon fine!\n");
        }
    }

    private String selectNamePlayer() throws RemoteException, NotBoundException {
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
                int port = server.getPort(token);
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                this.rmi_controller = (VirtualRmiController) registry.lookup(String.valueOf(port));
                flag=true;
                newClient = false;
                System.out.println(player_name + " riconnesso!");
            }
        } while(!flag);
        return player_name;
    }

    private void chooseStartingCardState() throws RemoteException, InterruptedException {
        if(rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            if(!rmi_controller.getTtoP().get(token).isFirstPlaced()) {
                chooseStartingCard();
            }
            while (rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD")) {
                buffering();
            }
        }
    }

    private void chooseGoalState() throws RemoteException, InterruptedException {
        if(rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_GOAL")) {
            if(rmi_controller.getTtoP().get(token).getGoalCard()==null) {
                chooseGoal();
                System.out.println("\nHai scelto :" + rmi_controller.getTtoP().get(token).getGoalCard().toString());
            }
            while (rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_GOAL")) {
                buffering();
            }
        }
    }

    private void waitFullGame() throws RemoteException, InterruptedException {
        if(rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("NOT_INITIALIZED")) {
            System.out.print("Aspetta il riempimento partita -");
            while (rmi_controller.getTtoP().get(token).getActual_state().getNameState().equals("NOT_INITIALIZED")) {
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
        rmi_controller.showStartingCard(token);
        int done=0;
        while(done==0){
            System.out.println("\nInserisci B per scegliere Back Side o F per scegliere Front side:");
            String dec = scan.nextLine();
            if (dec.equals("F")){
                done=1;
                rmi_controller.chooseStartingCard(token,false);
            } else if (dec.equals("B")){
                done=1;
                rmi_controller.chooseStartingCard(token,true);
            }
            else System.out.println("Inserimento errato!");
        }
        rmi_controller.showGameField(token);
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
            System.out.println("\nScegli obiettivo tra:\n 1-" + rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(0).toString()
                    + "\n 2-" + rmi_controller.getTtoP().get(this.token).getInitial_goal_cards().get(1).toString());
            String choice = scan.nextLine();
            if (choice.equals("1")) {
                done=1;
                rmi_controller.chooseGoal(token,0);
            } else if (choice.equals("2")){
                done=1;
                rmi_controller.chooseGoal(token,1);
            } else System.out.println("Inserimento errato!");
        }
    }

    private void makeChoice(String player_name) throws RemoteException, NotBoundException {
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

    private void chooseMatch(String player_name) throws RemoteException, NotBoundException {
        Scanner scan = new Scanner(System.in);
        boolean check;
            System.out.println("\nElenco partite disponibili: ");
            List<RmiController> partite = server.getFreeGames();
            for ( VirtualRmiController r : partite) {
                System.out.println( r.getController().getGame().getName() + " ID:" + r.getController().getGame().getIndex_game()
                        + " " + r.getController().getGame().getNumPlayer() + "/" + r.getController().getGame().getMax_num_player() );
            }
            do {
                System.out.println("\nInserisci ID partita in cui entrare");
                int ID = scan.nextInt();
                check = server.findRmiController(ID, token, player_name,this);
            }while(!check);
        int port = server.getPort(token);
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
        this.rmi_controller = (VirtualRmiController) registry.lookup(String.valueOf(port));
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
                int port;
                port = server.createGame(game_name, numplayers, token, player_name,this);
                System.out.println("porta" + port);
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                this.rmi_controller = (VirtualRmiController) registry.lookup(String.valueOf(port));

            } catch (ControllerException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
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
                int port;
                port = server.createGame(game_name, numplayers, token, playerName,this);
                System.out.println("porta" + port);
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", port);
                this.rmi_controller = (VirtualRmiController) registry.lookup(String.valueOf(port));

            } catch (ControllerException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        } while(flag);
    }

    @Override
    public void showUpdate(GameField gamefield) throws RemoteException {

    }

    @Override
    public void reportError(String details) throws RemoteException {
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    @Override
    public void reportMessage(String details) throws RemoteException {
        System.out.print("\n[ERROR] " + details + "\n> ");

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

    public void showField(GameField field) throws RemoteException {
        for (int i = 0; i < Constants.MATRIXDIM * 2; i++) {
            // Stampa la riga superiore della cella
            if (i % 2 == 0) {
                for (int j = 0; j < Constants.MATRIXDIM * 2; j++) {
                    if (j % 2 == 0) {
                        // Stampa i bordi superiori delle carte
                        int x = i / 2;
                        int y = (j - 1) / 2;
                        if (x >= 0 && x < Constants.MATRIXDIM && y >= 0 && y < Constants.MATRIXDIM &&
                                (field.getCell(x, y, Constants.MATRIXDIM).getEdges().contains(EdgeEnum.LEFTUP) ||
                                        field.getCell(x, y, Constants.MATRIXDIM).getEdges().contains(EdgeEnum.RIGHTUP))) {
                            System.out.print("--------");
                        } else {
                            System.out.print("        "); // Cell space
                        }
                    } else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            } else {
                // Stampa le righe delle carte e dei separatori
                for (int j = 0; j < Constants.MATRIXDIM * 2; j++) {
                    if (j % 2 == 0) {
                        int x = (i - 1) / 2;
                        int y = j / 2;
                        if (x >= 0 && x < Constants.MATRIXDIM && y >= 0 && y < Constants.MATRIXDIM) {
                            // Stampa il bordo sinistro delle carte
                            if (field.getCell(x, y, Constants.MATRIXDIM).getEdges().contains(EdgeEnum.LEFTUP) ||
                                    field.getCell(x, y, Constants.MATRIXDIM).getEdges().contains(EdgeEnum.LEFTDOWN)) {
                                System.out.print("|");
                            } else {
                                System.out.print(" ");
                            }
                            // Stampa il contenuto delle carte
                            System.out.print(" " + field.getCell(x, y, Constants.MATRIXDIM).getShort_value() + " ");
                        } else {
                            System.out.print("   "); // Cella vuota
                        }
                    } else {
                        System.out.print("|");
                    }
                }
                System.out.println();
            }
        }
        // Stampa la riga inferiore della cella
        for (int j = 0; j < Constants.MATRIXDIM * 2; j++) {
            if (j % 2 == 0) {
                int x = Constants.MATRIXDIM - 1;
                int y = (j - 1) / 2;
                if (x >= 0 && x < Constants.MATRIXDIM && y >= 0 && y < Constants.MATRIXDIM &&
                        (field.getCell(x, y, Constants.MATRIXDIM).getEdges().contains(EdgeEnum.LEFTDOWN) ||
                                field.getCell(x, y, Constants.MATRIXDIM).getEdges().contains(EdgeEnum.RIGHTDOWN))) {
                    System.out.print("--------");
                } else {
                    System.out.print("        "); // Cell space
                }
            } else {
                System.out.print("-");
            }
        }
        System.out.println();
    }



    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1);
        VirtualServerF server = (VirtualServerF) registry.lookup("VirtualServer");
        new RmiClientF(server).run();
    }
}

