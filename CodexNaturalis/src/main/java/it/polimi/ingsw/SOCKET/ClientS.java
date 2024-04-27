/*
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

    protected ClientS(BufferedReader input, ObjectOutputStream output) throws IOException {
        this.input = input;
        this.server = new ServerProxyS(output);
    }

    private void run() throws IOException {
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

    private void runCli() throws IOException {
        Scanner scan = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("\n> 1: Crea giocatore \n>2: Modifica Messaggio \n>");
            int command = scan.nextInt();

            if (command == 1) {
                System.out.println("inserisci nome giocatore");
                String nome=scanner.nextLine();
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

    public void reportError(String details) {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    @Override
    public void showValue(String message) {
        System.out.println(message);
    }
    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = Integer.parseInt("4567");

        Socket serverSocket = new Socket(host, port);

        InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(serverSocket.getOutputStream());

        new ClientS(new BufferedReader(socketRx), outputStream).run();
    }
}

*/
