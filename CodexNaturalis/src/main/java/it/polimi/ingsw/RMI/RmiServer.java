package it.polimi.ingsw.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RmiServer implements VirtualServer{

    private GiocoController controller;
    public TokenManager token_manager = new TokenManagerImplement();
    private List<VirtualView> clients = new ArrayList<>();

    private Map<String, Giocatore> mappa;
    private List<GiocoController> games = new ArrayList<>();

    private Map<String, GiocoController> mappa_gp ;

    public RmiServer(GiocoController controller) {
        this.controller = controller;
        this.mappa = new HashMap<>();
        this.mappa_gp = new HashMap<>();
    }

    private BlockingQueue<Integer[]> updates = new ArrayBlockingQueue<>(10);




    private void broadcastUpdateThread() throws InterruptedException, RemoteException {
        while (true){
            Integer[] update = updates.take();
            synchronized (this.clients){
                for(var c: this.clients){
                    c.showUpdate(update);
                }
            }

        }
    }



    public void connect(VirtualView client)throws RemoteException{
        synchronized (this.clients){
            this.clients.add(client);
        }
    }


    @Override
    public void put(int index, Integer number, String player_name) throws RemoteException, InterruptedException {
        Integer[] currentState;
        System.out.println("\n [add request received] \n");
        System.out.println(mappa_gp.size());
        mappa_gp.get(player_name).putInArray(index, number, mappa.get(player_name));

        /*
        if ( gioco.getStatus1() == null || gioco.getStatus2()== null ) {System.err.println("\n [ERROR] \n"); return;}
        if ( player == gioco.getStatus1() ) currentState = gioco.getStatus1().getCampo();
        else currentState = gioco.getStatus2().getCampo();

        try
        {
            updates.put(currentState);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        broadcastUpdateThread();*/

    }

    @Override
    public boolean gamesIsEmpty() {
        return this.games.isEmpty();
    }

    @Override
    public synchronized void createPlayer(String name, String client_token) {
        Giocatore p = controller.createPlayer(name);
        mappa.put( client_token , p );
    }

    @Override
    public String createToken(VirtualView client){
        return token_manager.generateToken(client);
    }

    @Override
    public Map<String, Giocatore> getMap() {
        return mappa;
    }

    @Override
    public synchronized void  clearMap(){
        mappa.clear();
    }

    @Override
    public synchronized List<VirtualView> getListClients() throws RemoteException {
        return clients;
    }

    @Override
    public List<GiocoController> getLisGames() throws RemoteException {
        return games;
    }

    @Override
    public Giocatore getPlayerFromClient(String client) throws RemoteException {

        return mappa.get(client);
    }

    @Override
    public void createGame(String name, String player) throws RemoteException {
        GiocoController game = new GiocoController(name, mappa.get(player));
        games.add(game);
        mappa_gp.put( player , game );
    }

    @Override
    public void addPlayer(int index, String player) throws RemoteException {
        games.get(index).getGame().setPlayer2(mappa.get(player));
        mappa.get(player).setGame(games.get(index));
        mappa_gp.put(player, games.get(index) );
    }

    @Override
    public Giocatore getFromToken(String token){
        return mappa.get(token);
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "VirtualServer";
        VirtualServer server = new RmiServer(new GiocoController("new", null));         //modifica
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : server connected. ");

    }

}