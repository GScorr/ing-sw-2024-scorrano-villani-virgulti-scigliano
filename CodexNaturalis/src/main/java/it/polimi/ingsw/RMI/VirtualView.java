package it.polimi.ingsw.RMI;

import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.GameFieldSingleCell;

import java.rmi.RemoteException;

public interface VirtualView {
    public void showUpdate(GameFieldSingleCell[][] game) throws RemoteException;
    public void reportError(String details) throws RemoteException;
}
