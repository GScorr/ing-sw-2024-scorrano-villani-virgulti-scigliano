package it.polimi.ingsw.RMI_FINAL;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualViewF extends Remote {
    public void showUpdate(Integer[] number) throws RemoteException;
    public void reportError(String details) throws RemoteException;
}
