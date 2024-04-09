package it.polimi.ingsw.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {
    public void showUpdate(Integer[] number) throws RemoteException;
    public void reportError(String details) throws RemoteException;
}
