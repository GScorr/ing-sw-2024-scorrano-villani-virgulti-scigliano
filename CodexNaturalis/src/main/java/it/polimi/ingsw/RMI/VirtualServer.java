package it.polimi.ingsw.RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualServer extends Remote {
    public void connect(VirtualView client) throws RemoteException;

    public void put(int index, Integer number, Giocatore player) throws RemoteException;

    public boolean gamesIsEmpty() throws RemoteException;

    public Giocatore createPlayer(String name, VirtualView client) throws RemoteException;

    public Map<VirtualView, Giocatore> getMap() throws RemoteException;

    public void clearMap() throws  RemoteException;

    public List<VirtualView> getListClients() throws RemoteException;
    public List<GiocoController> getLisGames() throws RemoteException;

    public Giocatore getPlayerFromClient(VirtualView client ) throws RemoteException;

    public void createGame(String name, Giocatore player ) throws  RemoteException;

    public void addPlayer(Gioco game, Giocatore player) throws  RemoteException;

}
