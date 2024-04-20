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

    public RmiServer(GiocoController controller) {
        this.controller = controller;
        this.mappa = new HashMap<>();
    }

    final BlockingQueue<Integer[]> updates = new ArrayBlockingQueue<>(10);




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
    public void put(int index, Integer number, Giocatore player, GiocoController controller) throws RemoteException{
        Integer[] currentState;
        System.out.println("\n [add request received] \n");
        controller.putInArray(index, number, player);
        if ( controller.getStatus1() == null || controller.getStatus2()== null ) {System.err.println("\n [ERROR] \n"); return;}
        if ( player == controller.getStatus1() ) currentState = controller.getStatus1().getCampo();
        else currentState = controller.getStatus2().getCampo();

        try
        {
            updates.put(currentState);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
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
    public void createGame(String name, Giocatore player) throws RemoteException {
        GiocoController game = new GiocoController(name, player);
        games.add(game);
    }

    @Override
    public void addPlayer(GiocoController game, Giocatore player) throws RemoteException {
        game.getGame().setPlayer2(player);
        game.getGame().getPlayersName();
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