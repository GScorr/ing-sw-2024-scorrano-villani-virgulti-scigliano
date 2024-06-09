package it.polimi.ingsw.RMI_FINAL;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RmiServerF implements VirtualServerF {
    private Common_Server common;
    public RmiServerF(Common_Server common) throws IOException {
        this.common = common;
        common.startHeartbeatChecker();}
    @Override
    public synchronized void connect(VirtualViewF client)throws IOException{common.connect(client);}
    @Override
    public String createToken(VirtualViewF client) throws IOException {return common.createToken(client);}
    @Override
    public Map<String, Player> getTtoP() throws IOException {return common.getTtoP();}
    @Override
    public Map<String, GameServer> getTtoR() throws IOException {return common.getTtoR();}
    public Map<Integer, GameServer> getListRmiController() throws IOException {return common.getListRmiController();}
    @Override
    public int createGame(String name, int num_player, String p_token, String player_name, VirtualViewF client) throws IOException, InterruptedException, ClassNotFoundException {
        return common.createGame( name , num_player, p_token, player_name, client);}
    @Override
    public boolean addPlayer(Integer game_id, String p_token, String name, VirtualViewF client) throws IOException, InterruptedException, ClassNotFoundException {return common.addPlayer(game_id, p_token, name, client);}
    @Override
    public List<VirtualViewF> getListClient() throws IOException {return common.getListClient();}
    //returns the list of all game controllers that are accessible ( not full )
    @Override
    public List<GameServer> getFreeGames() throws IOException {return common.getFreeGames();}
    //return 0 if the name is free, 1 if the name is already used by an active player, 2 if the player is coming back
    public List<SocketRmiControllerObject> getFreeGamesSocket() throws IOException {return common.getFreeGamesSocket();}
    @Override
    public String checkName(String name, VirtualViewF client) throws IOException {return common.checkName(name, client);}
    //private Object waitAnswer(String token, Integer idRequest) throws IOException {return token_to_rmi.get(token).getAnswer(idRequest);}
    @Override
    public int getPort(String token) throws IOException {return common.getPort(token);}
    @Override
    public boolean findRmiController(Integer game_id, String p_token, String player_name, VirtualViewF client) throws IOException, InterruptedException, ClassNotFoundException {return  common.findRmiController(game_id, p_token, player_name, client);}

    @Override
    public GameServer getRmiController(String token) throws IOException{return common.getRmiController(token);}
    @Override
    public void receiveHeartbeat(String token) throws IOException {common.receiveHeartbeat(token);}




    public static void main(String[] args) throws IOException {
    }

}
