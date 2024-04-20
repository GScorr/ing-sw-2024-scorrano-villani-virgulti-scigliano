package it.polimi.ingsw.RMI_FINAL;


import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualServerF extends Remote {
    public void connect(VirtualViewF client) throws RemoteException;


    public boolean gamesIsEmpty() throws RemoteException;

    public String createToken(VirtualViewF client ) throws  RemoteException;

    public Player getFromToken(String token) throws  RemoteException;
}
