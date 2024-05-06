package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.CONTROLLER.ControllerException;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.RmiController;
import it.polimi.ingsw.RMI_FINAL.SocketRmiControllerObject;
import it.polimi.ingsw.SOCKET_FINAL.TokenManager.TokenManager;


import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
public class Client implements VirtualView {

    final ServerProxy server;

    private boolean newClient;

    public Client(ObjectInputStream input, ObjectOutputStream output) throws IOException {
        this.server = new ServerProxy(output,input);
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

    /*
    private void runVirtualServer() throws IOException {

        String line;
        // Read message type
        while ((line = input.readLine()) != null) {
            // Read message and perform action
            switch (line) {
                case "errore" -> this.showValue(String.valueOf(Integer.parseInt(input.readLine())));
                case "update_message" -> this.showValue(input.readLine());
                case "update_number" -> this.reportError(input.readLine());
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }

    }
    */

    private void runCli() throws IOException, ClassNotFoundException, InterruptedException {

        //TODO : gestione persistenza connessioni
        String player_name = selectNamePlayer();
        String game_name;


        /**
         * ho cpiato tutte le funzioni e aggiunto tutti i metodi
         * e creato tutti i metodi connessi
         * fatta struttura messaggi
         * fatta struttura funzioni da chiamare su server proxy
         */

        gameAccess(player_name);
        startSendingHeartbeats();
        waitFullGame();
        /*
        chooseGoalState();
        chooseStartingCardState();
        manageGame();

         */

    }

    public String selectNamePlayer() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        Player curr_player;
        String player_name = " ";

        boolean flag = false;
        do{
            System.out.print("\nScegli nome Player > ");
            player_name = scan.nextLine();
            String isnew = server.checkName(player_name);
            if(isnew.equals("true")){
                flag = true;
                newClient=true;
                System.out.println("nome valido");
            }else if(isnew.equals("false")){
                System.out.println("Giocatore già presente, reinserisci nome!");
            }else{
                //TODO completa questo codice

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
            System.out.print("creazione Player andata a buon fine!\n");
        }
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
                String result = server.createGame(game_name, numplayers, playerName);
                System.out.println(result);
            } catch (ControllerException | IOException | ClassNotFoundException e) {
                System.err.print(e.getMessage() + "\n");
                flag = true;
            }
        } while(flag);
    }

    private void startSendingHeartbeats() {
        /*
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(50);
                    server.receiveHeartbeat(token);
                } catch (RemoteException | InterruptedException | IOException e) {
                   e.printStackTrace();
                }
            }
        }).start();

        */
    }

    private void makeChoice(String player_name) throws IOException, ClassNotFoundException {
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
        System.out.println("\nElenco partite disponibili: ");
         List<SocketRmiControllerObject> games = server.getFreeGame();

        for (SocketRmiControllerObject r : games) {
            System.out.println(r.name + " ID:" + r.ID
                    + " " + r.num_player + "/" + r.max_num_player);
        }
        do {
            System.out.println("\nInserisci ID partita in cui entrare");
            int ID = scan.nextInt();
            check = server.findRmiController(ID, player_name);
        }while(!check);

        System.out.println("sei entrato nella partita !");


    }

    /**
     * manca getTtop, non so in che classe va
     */

    private void waitFullGame() throws IOException, InterruptedException {
        System.out.println("sono nel waitFullGame");
        buffering();
        /**
         * da fixare
         */
        /*
        if(server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("NOT_INITIALIZED")) {
            System.out.print("Aspetta il riempimento partita -");
            while (server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("NOT_INITIALIZED")) {
                buffering();
            }
            System.out.println("\nEhi la tua partita è piena!\n");
        }

         */
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

    private void chooseGoalState() throws IOException, InterruptedException {
        chooseGoal();
        /**
         * da fixare
         */
        /*
        if(server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_GOAL")) {
            if(server.getRmiController(token).getTtoP().get(token).getGoalCard()==null) {
                chooseGoal();
                System.out.println("\nHai scelto :" + server.getRmiController(token).getTtoP().get(token).getGoalCard().toString());
            }
            while (server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_GOAL")) {
                buffering();
            }
        }

         */
    }

    /**
     *manca getTtop
     */
    private void chooseGoal() throws IOException {
        Scanner scan = new Scanner(System.in);
        int done=0;
        while(done==0) {
            System.out.println("scegli obiettivo tra 0 o 1: ");
            /**
             * da fixare
             */
            /*
            System.out.println("\nScegli obiettivo tra:\n 1-" + server.getRmiController(token).getTtoP().get(this.token).getInitial_goal_cards().get(0).toString()
                    + "\n 2-" + server.getRmiController(token).getTtoP().get(this.token).getInitial_goal_cards().get(1).toString());
             */
            String choice = scan.nextLine();
            if (choice.equals("0")) {
                done=1;
                System.out.println("scelta ok");
                server.chooseGoal(0);

            } else if (choice.equals("1")){
                done=1;
                server.chooseGoal(1);
            } else System.out.println("Inserimento errato!");
        }
    }

    private void chooseStartingCardState() throws IOException, InterruptedException, ClassNotFoundException {

        System.out.println("choose starting card");
        chooseStartingCard();

        /**
         * TODO:
         *  sistemare quando il player rimane in buffering
         */
    /*
        if(server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD")) {
            if(!server.getRmiController(token).getTtoP().get(token).isFirstPlaced()) {
                chooseStartingCard();
            }
            while (server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("CHOOSE_SIDE_FIRST_CARD")) {
                buffering();
            }
        }

     */
    }

    private void chooseStartingCard() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        System.out.println("\nScegli lato carta iniziale:\n");
        server.showStartingCard();
        int done=0;
        while(done==0){
            System.out.println("\nInserisci B per scegliere Back Side o F per scegliere Front side:");
            String dec = scan.nextLine();
            if (dec.equals("F")){
                done=1;
                server.chooseStartingCard(false);
            } else if (dec.equals("B")){
                done=1;
                server.chooseStartingCard(true);
            }
            else System.out.println("Inserimento errato!");
        }
        server.showGameField();
    }

    /**
     * manca getTtop
     */


    private void manageGame() throws IOException, InterruptedException {
        System.out.println("entro in manageGame");
        /**
         * da fixare
         */
       /*
        if(server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("WAIT_TURN")) {
            System.out.println("\nAspetta il tuo turno ");
            while (server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("WAIT_TURN")) {
                buffering();
            }
        }
        if(server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("PLACE_CARD")) {
            System.out.println("\nInserisci la tua carta: ");
            while (server.getRmiController(token).getTtoP().get(token).getActual_state().getNameState().equals("PLACE_CARD")) {
                buffering();
            }
        }

        */
    }











    /*
        public void showValue(Integer number) {
            // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
            System.out.print("\n= " + number + "\n> ");
        }


     */

    /*
    private void newGameNotAvailable(String playerName) throws IOException, ClassNotFoundException {
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

            }catch (SocketException e){
                System.out.println(e.getMessage());
                flag = true;
            }
        } while(flag);
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

