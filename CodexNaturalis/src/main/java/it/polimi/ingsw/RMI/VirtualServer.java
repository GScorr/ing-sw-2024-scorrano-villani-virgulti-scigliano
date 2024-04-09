package it.polimi.ingsw.RMI;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualServer extends Remote {
    public void connect(VirtualView client) throws RemoteException;

    public void put(int index, Integer number, Giocatore player) throws RemoteException;

    public boolean gamesIsEmpty();

    public Giocatore createPlayer(String name, RmiClient client);
}
