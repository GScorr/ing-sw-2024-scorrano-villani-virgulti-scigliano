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

    final GiocoController controller;

    final List<VirtualView> clients = new ArrayList<>();

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
    };


    public static void main(String[] args) throws RemoteException {
        final String serverName = "AdderServer";
        VirtualServer server = new RmiServer(new GiocoController());
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("server bound. ");

    }

}