package it.polimi.ingsw.RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualServer extends Remote {
    public void connect(VirtualView client) throws RemoteException;

    public void put(int index, Integer number, String player ) throws RemoteException, InterruptedException;

    public boolean gamesIsEmpty() throws RemoteException;

    public void createPlayer(String name, String client) throws RemoteException;

    public Map<String, Giocatore> getMap() throws RemoteException;

    public void clearMap() throws  RemoteException;

    public List<VirtualView> getListClients() throws RemoteException;
    public List<GiocoController> getLisGames() throws RemoteException;

    public Giocatore getPlayerFromClient(String client ) throws RemoteException;

    public void createGame(String name, Giocatore player ) throws  RemoteException;

    public void addPlayer(int index, Giocatore player) throws  RemoteException;

    public String createToken(VirtualView client ) throws  RemoteException;

    public Giocatore getFromToken(String token) throws  RemoteException;
}
