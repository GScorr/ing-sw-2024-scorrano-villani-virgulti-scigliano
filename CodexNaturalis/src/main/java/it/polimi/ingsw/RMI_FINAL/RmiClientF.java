/*package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

//
public class RmiClientF extends UnicastRemoteObject implements VirtualViewF {
    final VirtualServerF server;
    private  String token;

    protected RmiClientF(VirtualServerF server) throws RemoteException {
        this.server = server;
    }

    private void run() throws RemoteException, InterruptedException {
        this.server.connect(this);
        runCli();
    }

    private void runCli() throws RemoteException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        VirtualViewF curr_client =  this;
        String player_name ;
        Player curr_player;

        //creo giocatore
        System.out.print("\n Scegli nome Player > ");
        player_name = scan.nextLine();
        // Create a token associated with a client, in the rmi server we have a reference to TokenManagerImplement which contains a
        // map that associate the client with the token, and we also have a map in server that associate the token with the player
        // < RmiClient , TOKEN > < TOKEN , Player >
        this.token = server.createToken(this);
        //System.out.print("\n Token Player > " + this.token);
        server.createPlayer( player_name , this.token );

        //se non esistono partite
        if (server.gamesIsEmpty()) {
            //creo partita e inserisco come player 2
            System.out.println("\n Non esiste nessuna partita, creane una nuova!");
            System.out.print("\n Scegli nome Partita > ");
            String game_name = scan.nextLine();
            server.createGame(game_name, token);

        } else {
            System.out.println("\n Elenco partite disponibili: ");
            for(GameController game: server.getListGames()){
                System.out.println("\n" + game.getGame().getNam);
            }
            //inserisco player come player 2 in game
            server.addPlayer(0, token);
            System.out.print("\n Giocatore " + server.getListGames().get(0).getGame().getPlayer2().getName() +" Aggiunto a partita esistente");
        }

        System.out.print("...creazione Player andata a buon fine");
        System.out.print("\nCONTIENE: " + server.getMap().size() );

    }

    @Override
    public void showUpdate(GameField game_field) throws RemoteException {
    }

    @Override
    public void reportError(String details) throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        VirtualServerF server = (VirtualServerF) registry.lookup("VirtualServer");
        new RmiClientF(server).run();
    }
}
*/