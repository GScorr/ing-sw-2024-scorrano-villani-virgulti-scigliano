package it.polimi.ingsw.RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;


public class RmiClient extends UnicastRemoteObject implements VirtualView {
    final VirtualServer server;
    private  String token;

    protected RmiClient(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    private void run() throws RemoteException, InterruptedException {
        this.server.connect(this);
        runCli();
    }

    private void runCli() throws RemoteException, InterruptedException {

        Scanner scan = new Scanner(System.in);
        VirtualView curr_client = this;
        String player_name;
        Giocatore curr_player;


        // Creo giocatore
        System.out.print("\nScegli nome Player > ");
        player_name = scan.nextLine();

        // Create a token associated with a client, in the rmi server we have a reference to TokenManagerImplement
        // which contains a map that associate the client with the token, and we also have a map in server that
        // associate the token with the player
        // < RmiClient , TOKEN > < TOKEN , Player >
        this.token = server.createToken(this);
        //System.out.print("\nToken Player > " + this.token);
        server.createPlayer(player_name, this.token);

        String game_name;

        // Se non esistono partite
        if (server.gamesIsEmpty()) {
            System.out.println("\nNon esiste nessuna partita, creane una nuova!");
            System.out.print("\nScegli nome Partita > ");
            game_name = scan.nextLine();
            System.out.print("\nScegli numero giocatori partita (da 2 a 4) > ");
            int numplayers = scan.nextInt();
            server.createGame(game_name, numplayers, token);
        } else {
            int done=0;
            while(done==0) {
                System.out.println("\nDigita 'new' per creare una nuova partita, 'old' per entrare in una delle partite disponibili");
                String decision = scan.nextLine();
                if (decision.equalsIgnoreCase("old")) {
                    boolean check=false;
                    while(!check) {
                        done = 1;
                        System.out.println("\nElenco partite disponibili: ");
                        List<GiocoController> partite = server.getLisGames();

                        for (GiocoController g : partite) {
                            System.out.println(g.getGame().getName() + " " + g.getGame().getPlayers().size() + "/" + g.getGame().getNumplayers() + " ID:" + g.getGame().getIndex_game());
                        }

                        System.out.println("\nInserisci ID partita in cui entrare");
                        int ID = scan.nextInt();
                        check = server.addPlayer(ID, token);
                    }
                } else if (decision.equalsIgnoreCase("new")) {
                    done=1;
                    System.out.print("\nScegli nome Partita > ");
                    game_name = scan.nextLine();
                    int right=0;
                    int numplayers=4;
                    while(right==0) {
                        System.out.print("\nScegli numero giocatori partita (da 2 a 4) > ");
                        numplayers = scan.nextInt();
                        if(numplayers>=2&&numplayers<=4){
                            right=1;
                        }
                    }

                    server.createGame(game_name, numplayers, token);
                } else {
                    System.out.println("\nInserimento errato!");
                }
            }
        }
        System.out.print("creazione Player andata a buon fine!");

        /*while (true) {
            System.out.print("\n Inserisci valore nel tuo array, INDICE  >  VALORE>  ");
            int index = scan.nextInt();
            int value = scan.nextInt();
            server.put(index, value, token );
            Integer[] campo = server.getFromToken(token).getCampo();
            for(int i=0; i<10; i++)
                System.out.println(" " +campo[i]);
        }*/

    }

    @Override
    public void showUpdate(Integer[] number) throws RemoteException {
        System.out.print("\n> ");
        int a=0;
        for ( int i=0 ; i<10; i++)
        {   if( number[i] != null )
            System.out.print( number[i] );
        else
            System.out.println(a);
        }
        System.out.print("\n> ");
    }

    @Override
    public void reportError(String details) throws RemoteException {
        // TODO Attenzione! Questo può causare data race con il thread dell'interfaccia o un altro thread
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, InterruptedException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        VirtualServer server = (VirtualServer) registry.lookup("VirtualServer");
        new RmiClient(server).run();
    }
}

// todo check numero massimo giocatori partita, check id partita, check su scritta new/old
