package it.polimi.ingsw.RMI_FINAL;

import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Player.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface VirtualRmiController extends Remote {
    public boolean getFull() throws RemoteException;
    public void connect(VirtualViewF client)throws RemoteException;
    public List<VirtualViewF> getClients() throws RemoteException;
    public Map<String, Player> getTtoP() throws RemoteException;
    public GameController getController() throws RemoteException;
    public Player createPlayer(String pla, String playerName, boolean b) throws RemoteException;
    public boolean addPlayer(String p_token, String name, VirtualViewF client) throws RemoteException;
    public void chooseGoal(String token, int index) throws RemoteException;
    public void chooseStartingCard(String token, boolean flip) throws RemoteException;
    public void checkQueue() throws RemoteException;
    public void executeCall(Integer function) throws RemoteException;
    public void addtoQueue(String string,Integer idRequest, Wrapper wrap) throws RemoteException;
    public Object getAnswer(Integer idRequest) throws RemoteException;
    public int getPort() throws RemoteException;
    public void showStartingCard(String token) throws RemoteException;
    public void showGameField(String token) throws RemoteException;
}
