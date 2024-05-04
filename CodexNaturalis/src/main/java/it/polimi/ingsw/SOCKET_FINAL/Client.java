package it.polimi.ingsw.SOCKET_FINAL;

import it.polimi.ingsw.MODEL.Player.Player;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
public class Client implements VirtualView {

    final ObjectInputStream input;
    final ServerProxy server;

    //genero token
    public String token ;
    public Client(ObjectInputStream input, ObjectOutputStream output) throws IOException {
        this.input = input;
        this.server = new ServerProxy(output,input);
    }

    public void run() throws IOException, ClassNotFoundException {
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

    private void runCli() throws IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        Player curr_player;
        String player_name = " ";

        boolean flag = false;
        do{
            System.out.print("\nScegli nome Player > ");
            player_name = scan.nextLine();
            boolean isnew = server.checkName(player_name);
            if(isnew){
                flag = true;
                this.token = server.getToken(player_name);
                System.out.println(token);
            }else if(!isnew){
                System.out.println("Giocatore già presente, reinserisci nome!");
            }else{
                //bisogno di spiegazione codice
                flag = true;
                System.out.println(token + "riconnessa");
            }

        }while (!flag);
        //TODO : gestione persistenza connessioni

        String game_name;

        //If games does not exists
        ArrayList free_games = server.getFreeGame(this.token);
        System.out.println("fino a qui");
        if(free_games == null || free_games.isEmpty()){
            newGameNotAvailable(player_name);
        }else{

        }
    }


    /*
        public void showValue(Integer number) {
            // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
            System.out.print("\n= " + number + "\n> ");
        }


     */

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
        }catch (IOException e) {
            System.out.println("impossibile creare socket input / output");
            return;
        }

    }
}

