package it.polimi.ingsw.RMI;

import it.polimi.ingsw.MODEL.GameField;

import java.rmi.RemoteException;

public interface VirtualView {
    public void showUpdate(GameField game) throws RemoteException;
    public void reportError(String details) throws RemoteException;
}
