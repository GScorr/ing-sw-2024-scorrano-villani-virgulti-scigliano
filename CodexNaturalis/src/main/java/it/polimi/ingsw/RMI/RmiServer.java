package it.polimi.ingsw.RMI;

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

    GiocoController controller;

    private List<VirtualView> clients = new ArrayList<>();

    private Map<VirtualView, Giocatore> mappa = new HashMap<>();
    private List<GiocoController> games = new ArrayList<>();

    public RmiServer(GiocoController controller) {
        this.controller = controller;
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
    public void put(int index, Integer number, Giocatore player) throws RemoteException{
        Integer[] currentState;
        System.err.println("add request received");
        this.controller.putInArray(index, number, player);
        if ( player == controller.getStatus1() ) currentState = this.controller.getStatus1().getCampo();
        else currentState = this.controller.getStatus2().getCampo();

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
    public synchronized Giocatore createPlayer(String name, VirtualView client) {
        Giocatore p = controller.createPlayer(name);
        mappa.put(client, p );
        return p;
    }

    @Override
    public Map<VirtualView, Giocatore> getMap() {
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
    public Giocatore getPlayerFromClient(VirtualView client) throws RemoteException {
        return mappa.get(client);
    }

    @Override
    public void createGame(String name, Giocatore player) throws RemoteException {
        GiocoController game = new GiocoController(name, player);
        games.add(game);
    }

    @Override
    public void addPlayer(Gioco game, Giocatore player) throws RemoteException {
        game.setPlayer2(player);
    }


    public static void main(String[] args) throws RemoteException {
        final String serverName = "VirtualServer";
        VirtualServer server = new RmiServer(new GiocoController("new", null));         //modifica
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("server bound. ");

    }

}