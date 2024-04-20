package it.polimi.ingsw.RMI_FINAL;


import it.polimi.ingsw.CONTROLLER.GameController;
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
    public Map<String, GameController> getTtoG() throws RemoteException;
    public List<GameController> getListGameController() throws  RemoteException;
    public void CreatePlayer(String name, String client_token, boolean first) throws RemoteException;
    public void createGame(int num_player, String p_token) throws RemoteException;
    public void addPlayer() throws RemoteException;
}
