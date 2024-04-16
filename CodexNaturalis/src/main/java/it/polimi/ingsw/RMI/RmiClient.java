package it.polimi.ingsw.RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

//
public class RmiClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;
    private  String token;

    protected RmiClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    private void run() throws RemoteException {
        this.server.connect(this);
        runCli();
    }

    private void runCli() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        VirtualView curr_client =  this;
        String player_name ;
        Giocatore curr_player;

        //creo giocatore
        System.out.print("\n Scegli nome Player > ");
        player_name = scan.nextLine();
        // Create a token associated with a client, in the rmi server we have a reference to TokenManagerImplement which contains a
        // map that associate the client with the token, and we also have a map in server that associate the token with the player
        // < RmiClient , TOKEN > < TOKEN , Player >
        this.token = server.createToken(this);
        System.out.print("\n Token Player > " + this.token);
        server.createPlayer( player_name , this.token );

        curr_player = server.getFromToken(this.token);

        synchronized (this) {

            //controllo che la partita sia non vuota
            if (server.gamesIsEmpty()) {
                //creo partita e inserisco come player 2
                System.out.print("\n Scegli nome Partita > ");
                String game_name = scan.nextLine();
                server.createGame(game_name, curr_player);
                System.out.print("\n Giocatore " + server.getLisGames().getFirst().getGame().getPlayer1().getName() + " Ha creato a una nuova Partita  ");

            } else {
                //inserisco player come player 2 in game todo correzione bug inserimento giocatore 2____ il giocatore 2 non viene inserito nel game
                Gioco existing_game = server.getLisGames().getFirst().getGame();
                server.addPlayer(existing_game, curr_player);
                System.out.print("\n Giocatore " + curr_player.getName() + " " + existing_game.getPlayer2() + " Aggiunto a partita esistente");
            }
        }
        /*System.out.print("\nnome Scelto > " + player_name + " > creazione Player...\n");


        System.out.print("...creazione Player andata a buon fine");
        System.out.print("\nCONTIENE: " + server.getMap().size() );

        // for (Giocatore g : server.getMap().values() ) System.out.print(" " +  g.getName() );
        //server.getMap().values().forEach(giocatore -> System.out.println(giocatore.getName()));
        //System.out.print("\nche giocatore sono ?" + server.getPlayerFromClient(this).getName() );

        /*
        while (true) {
            System.out.print("\n Inserisci valore nel tuo array, INDICE  >  VALORE>  ");
            int index = scan.nextInt();
            int value = scan.nextInt();
            server.put(index, value, curr_player );
        }*/

    }

    @Override
    public void showUpdate(Integer[] number) throws RemoteException {
        System.out.print("\n> ");
        for ( int i=0 ; i<10; i++)
            System.out.print( number[i] );
        System.out.print("\n> ");
    }

    @Override
    public void reportError(String details) throws RemoteException {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");
        new RmiClient(server).run();
    }
}
