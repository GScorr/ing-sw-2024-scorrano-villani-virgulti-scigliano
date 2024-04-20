/*package it.polimi.ingsw.RMI_FINAL;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI.*;
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

public class RmiServer implements VirtualServerF {

    private GameController controller;
    public TokenManager token_manager = new TokenManagerImplement();
    private Map<String, Player> mappa;
    private List<VirtualView> clients = new ArrayList<>();
    private List<GameController> games = new ArrayList<>();
    private Map<String, GameController> mappa_gp ;

    public RmiServer(GameController controller) {
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

        try
        {
            updates.put(currentState);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        broadcastUpdateThread();

    }

    @Override
    public boolean gamesIsEmpty() {
        return this.games.isEmpty();
    }

    @Override
    public Player getFromToken(String token){
        return mappa.get(token);
    }

    public static void main(String[] args) throws RemoteException {
        final String serverName = "VirtualServer";
        VirtualServer server = new it.polimi.ingsw.RMI.RmiServer(new GameController("new", null));         //modifica
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : server connected. ");

    }

}

 */