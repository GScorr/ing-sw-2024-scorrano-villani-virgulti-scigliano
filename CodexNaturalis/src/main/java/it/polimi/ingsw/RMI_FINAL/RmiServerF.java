package it.polimi.ingsw.RMI_FINAL;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Player.Player;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RmiServerF implements VirtualServerF {
    private Common_Server common;
    public RmiServerF(Common_Server common) throws RemoteException {
        this.common = common;
        common.startHeartbeatChecker();}
    @Override
    public synchronized void connect(VirtualViewF client)throws RemoteException{common.connect(client);}
    @Override
    public String createToken(VirtualViewF client) throws RemoteException {return common.createToken(client);}
    public String createTokenSocket(String name) throws RemoteException {return common.createTokenSocket(name);}
    @Override
    public Map<String, Player> getTtoP() throws RemoteException {return common.getTtoP();}
    @Override
    public Map<String, GameServer> getTtoR() throws RemoteException {return common.getTtoR();}
    public Map<Integer, GameServer> getListRmiController() throws RemoteException {return common.getListRmiController();}
    @Override
    public int createGame(String name, int num_player, String p_token, String player_name, VirtualViewF client) throws RemoteException {return common.createGame( name , num_player, p_token, player_name, client);}
    public int createGameSocket(String name, int num_player, String p_token, String player_name) throws RemoteException {return common.createGameSocket(name, num_player, p_token, player_name);}
    @Override
    public boolean addPlayer(Integer game_id, String p_token, String name, VirtualViewF client) throws RemoteException {return common.addPlayer(game_id, p_token, name, client);}
    public boolean addPlayerSocket(Integer game_id, String p_token, String name) throws RemoteException {return common.addPlayerSocket(game_id, p_token, name);}
    @Override
    public List<VirtualViewF> getListClient() throws RemoteException {return common.getListClient();}
    //returns the list of all game controllers that are accessible ( not full )
    @Override
    public List<GameServer> getFreeGames() throws RemoteException {return common.getFreeGames();}
    //return 0 if the name is free, 1 if the name is already used by an active player, 2 if the player is coming back
    public List<SocketRmiControllerObject> getFreeGamesSocket() throws RemoteException {return common.getFreeGamesSocket();}
    @Override
    public String checkName(String name, VirtualViewF client) throws RemoteException {return common.checkName(name, client);}
    //private Object waitAnswer(String token, Integer idRequest) throws RemoteException {return token_to_rmi.get(token).getAnswer(idRequest);}
    @Override
    public int getPort(String token) throws RemoteException {return common.getPort(token);}
    @Override
    public boolean findRmiController(Integer game_id, String p_token, String player_name, VirtualViewF client) throws RemoteException {return  common.findRmiController(game_id, p_token, player_name, client);}
    public boolean findRmiControllerSocket(Integer game_id, String p_token, String player_name) throws RemoteException {return common.findRmiControllerSocket(game_id, p_token, player_name);}
    /*private void broadcastUpdateThread() throws InterruptedException, RemoteException {
        while ( !updates.isEmpty() ){
            String update = updates.take();
            synchronized (this){

                List<String> tokens = new ArrayList<>();
                GameServer gc = token_to_rmi.get(update);

                for( String t : token_to_rmi.keySet() )
                    if( token_to_rmi.get(t).equals(gc) ) tokens.add(t);

                for(String t: tokens){
                    token_manager.getTokens().get(t).showUpdate( token_to_player.get(update).getGameField() );
                }

            }

        }
    }*/
    @Override
    public GameServer getRmiController(String token) throws RemoteException{return common.getRmiController(token);}
    @Override
    public void receiveHeartbeat(String token) throws RemoteException {common.receiveHeartbeat(token);}




    public static void main(String[] args) throws RemoteException {
      /*  final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF();
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        port=1099;
        Registry registry = LocateRegistry.createRegistry(1);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : server connected. ");*/
    }

}
