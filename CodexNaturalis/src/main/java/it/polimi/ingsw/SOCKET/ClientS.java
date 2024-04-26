package it.polimi.ingsw.SOCKET;

import it.polimi.ingsw.RMI.TokenManager;
import it.polimi.ingsw.SOCKET.Token.TokenManagerS;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
public class ClientS implements VirtualViewS{

    final BufferedReader input;
    final ServerProxyS server;

    //genero token
    public String token = new TokenManagerS().generateToken(this);

    protected ClientS(BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.server = new ServerProxyS(output);
    }

    private void run() throws RemoteException {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }

    private void runVirtualServer() throws IOException {
        /*
        da rivedere la chiamata
         */
        String line;
        // Read message type
        while ((line = input.readLine()) != null) {
            // Read message and perform action
            switch (line) {
                case "update" -> this.showValue(String.valueOf(Integer.parseInt(input.readLine())));
                case "error" -> this.reportError(input.readLine());
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }

    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            /*
            mancano le altre funzioni del proxy
             */
            System.out.print("\n> 1: Crea giocatore \n>2: Modifica Messaggio \n>");
            int command = scan.nextInt();

            if (command == 1) {
                System.out.println("entro");
                System.out.println("inserisci nome giocatore");
                String nome = null;
                while(nome==null){
                    nome=scanner.nextLine(); //errore nella lettura della stringa successiva
                    //non legge la stringa da input correttamente, ho provato a mettere anche una'altra variabile di appoggio ma non riescer a passarla correttamente al server
                    //credo che il problema si questo
                }
                server.inserisciGiocatore(nome, token);
            }
            else if(command == 2){
                System.out.println("inserisci messaggio");
                String message = scanner.nextLine();
                server.lunchMessage(message, token);
            }
            else {
                System.out.println("scelta sbagliata");
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

    }
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = Integer.parseInt("4567");

        Socket serverSocket = new Socket(host, port);

        InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
        OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());

        new ClientS(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();
    }
}

