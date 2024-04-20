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

    public void createPlayer(String name, String client) throws RemoteException;

    public Map<String, Player> getMap() throws RemoteException;

    public void clearMap() throws  RemoteException;

    public List<VirtualViewF> getListClients() throws RemoteException;
    public List<GameController> getListGames() throws RemoteException;

    public Player getPlayerFromClient(String client ) throws RemoteException;

    public void createGame(String name, String player ) throws  RemoteException;

    public void addPlayer(int index, String player) throws  RemoteException;

    public String createToken(VirtualViewF client ) throws  RemoteException;

    public Player getFromToken(String token) throws  RemoteException;
}
