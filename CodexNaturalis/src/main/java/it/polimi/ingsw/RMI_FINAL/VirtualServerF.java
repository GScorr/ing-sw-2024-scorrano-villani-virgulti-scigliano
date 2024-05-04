package it.polimi.ingsw.RMI_FINAL;


import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualServerF extends Remote {
    public void connect(VirtualViewF client)throws RemoteException;
    public String createToken(VirtualViewF client ) throws  RemoteException;
    public Map<String, Player> getTtoP() throws RemoteException;
    public Map<String, RmiController> getTtoR() throws RemoteException;
    public Map<Integer,RmiController> getListRmiController() throws  RemoteException;
    public int createGame(String game_name, int num_player, String p_token, String player_name,VirtualViewF client) throws RemoteException;
    public boolean addPlayer(Integer game_id, String token, String name,VirtualViewF client) throws RemoteException;
    public List<VirtualViewF> getListClient() throws RemoteException;
    public List<RmiController> getFreeGames() throws RemoteException;
    public String checkName(String name) throws RemoteException;
    public boolean findRmiController(Integer id, String p_token, String player_name, VirtualViewF client) throws RemoteException;
    public RmiController getRmiController(String token) throws RemoteException;
    public void receiveHeartbeat(String token) throws RemoteException;
    int getPort(String token) throws RemoteException;
}
