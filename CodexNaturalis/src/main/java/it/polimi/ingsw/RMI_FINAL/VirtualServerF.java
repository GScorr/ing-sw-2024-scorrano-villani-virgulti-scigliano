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
public List<RmiController> getListRmiController() throws  RemoteException;
    public RmiController createGame(String game_name, int num_player, String p_token, String player_name) throws RemoteException;
    public boolean addPlayer(int game_id, String token, String name) throws RemoteException;
    public List<VirtualViewF> getListClient() throws RemoteException;
    public List<RmiController> getFreeGames() throws RemoteException;
    public void insertCard(String p_token, PlayCard card, int x, int y, int index) throws RemoteException, InterruptedException;
    public boolean checkName(String name, String token) throws RemoteException;
    public void chooseGoal(String p_token, int goal_index) throws RemoteException;
    public void selectStartingCard(String token, boolean flipped) throws RemoteException;
    public boolean checkFull(String token) throws RemoteException;
    public void showStartingCard(String token) throws RemoteException;

    RmiController findRmiController(int id, String p_token) throws RemoteException;
}
