package it.polimi.ingsw.RMI_FINAL;


import it.polimi.ingsw.MODEL.Player.Player;

import java.io.IOException;
import java.rmi.Remote;
import java.util.List;
import java.util.Map;

/**
 * This interface defines methods for a virtual serverSocket used in a Remote Method Invocation (RMI) system.
 * The serverSocket manages game creation, player connections, and access to game servers.
 */
public interface VirtualServerF extends Remote {
    public void connect(VirtualViewF client)throws IOException;
    public String createToken(VirtualViewF client ) throws  IOException;
    //public String createTokenSocket(String name) throws IOException;
    public Map<String, Player> getTtoP() throws IOException;
    public Map<String, GameServer> getTtoR() throws IOException;
    public Map<Integer, GameServer> getListRmiController() throws  IOException;
    public int createGame(String game_name, int num_player, String p_token, String player_name,VirtualViewF client) throws IOException, InterruptedException;
    public boolean addPlayer(Integer game_id, String token, String name,VirtualViewF client) throws IOException, InterruptedException;
    public List<VirtualViewF> getListClient() throws IOException;
    public List<GameServer> getFreeGames() throws IOException;
    public List<SocketRmiControllerObject> getFreeGamesSocket() throws IOException;
    public String checkName(String name, VirtualViewF client) throws IOException, InterruptedException;
    public boolean findRmiController(Integer id, String p_token, String player_name, VirtualViewF client) throws IOException, InterruptedException;
    public GameServer getRmiController(String token) throws IOException;
    public void receiveHeartbeat(String token) throws IOException;
    int getPort(String token) throws IOException;

}
