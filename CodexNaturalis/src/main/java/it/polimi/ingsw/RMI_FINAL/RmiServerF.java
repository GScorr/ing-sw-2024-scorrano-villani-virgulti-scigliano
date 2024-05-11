package it.polimi.ingsw.RMI_FINAL;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.Common_Server;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.StartingCard;
import it.polimi.ingsw.MODEL.Game.IndexRequestManagerF;
import it.polimi.ingsw.MODEL.GameField;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RmiServerF implements VirtualServerF {

    //private GameController ctrl;
    private static final long HEARTBEAT_TIMEOUT = 1500;
    private Common_Server common;
    private TokenManagerF token_manager = new TokenManagerImplementF();
    private static int port;
    private List<VirtualViewF> clients = new ArrayList<>();
    private Map<String, Player> token_to_player = new HashMap<>();
    private Map<String, RmiController>  token_to_rmi = new HashMap<>();
    //private List<GameController> controllers = new ArrayList<>();
    private Map<Integer , RmiController> rmi_controllers = new HashMap<>();

    //todo da modificare una volta capito che tipo di update invia
    private BlockingQueue<String> updates = new ArrayBlockingQueue<>(20);

    private final Map<String, Long> lastHeartbeatTime = new HashMap<>();

    public RmiServerF(Common_Server common) throws RemoteException {
        this.common = common;
        startHeartbeatChecker();}

    //put the client in the clients list
    @Override
    public synchronized void connect(VirtualViewF client)throws RemoteException{this.clients.add(client);}

    //given a client create the token that will represent it
    @Override
    public String createToken(VirtualViewF client) throws RemoteException {return token_manager.generateToken(client);}
    public String createTokenSocket(String name) throws RemoteException {return token_manager.generateTokenSocket(name);}

    @Override
    public Map<String, Player> getTtoP() throws RemoteException {return token_to_player;}

    @Override
    public Map<String, RmiController> getTtoR() throws RemoteException {return token_to_rmi;}

    public Map<Integer, RmiController> getListRmiController() throws RemoteException {return rmi_controllers;}

    //todo: NEL CONTROLLER SCEGLIERE COLORE
    @Override
    public int createGame(String name, int num_player, String p_token, String player_name, VirtualViewF client) throws RemoteException {
        int port = getAvailablePort();
        RmiController gameServer = new RmiController(name,num_player,port);
        gameServer.addPlayer(p_token,player_name, client,true);
        VirtualRmiController serverStub = (VirtualRmiController) UnicastRemoteObject.exportObject(gameServer, 0);
        Registry registry = LocateRegistry.createRegistry(port); // Connect to existing registry
        registry.rebind(String.valueOf(port), serverStub);
        token_to_rmi.put( p_token, gameServer);
        rmi_controllers.put(gameServer.getController().getGame().getIndex_game(), gameServer);
        System.out.println(port);
        return port;
    }

    public int createGameSocket(String name, int num_player, String p_token, String player_name) throws RemoteException {
        int port = getAvailablePort();
        RmiController gameServer = new RmiController(name,num_player,port);
        gameServer.addPlayerSocket(p_token,player_name,true);
        VirtualRmiController serverStub = (VirtualRmiController) UnicastRemoteObject.exportObject(gameServer, 0);
        Registry registry = LocateRegistry.createRegistry(port); // Connect to existing registry
        registry.rebind(String.valueOf(port), serverStub);
        token_to_rmi.put( p_token, gameServer);
        rmi_controllers.put(gameServer.getController().getGame().getIndex_game(), gameServer);
        System.out.println(port);
        return port;
    }

    private int getAvailablePort(){port++;return port;}

    //check if the id is valid, if not I send back a report error, otherwise i insert the player in the given game
    @Override
    public boolean addPlayer(Integer game_id, String p_token, String name, VirtualViewF client) throws RemoteException {rmi_controllers.get(game_id).addPlayer(p_token,name, client,false);return true;}

    public boolean addPlayerSocket(Integer game_id, String p_token, String name) throws RemoteException {
        rmi_controllers.get(game_id).addPlayerSocket(p_token,name,false);
        return true;}

    @Override
    public List<VirtualViewF> getListClient() throws RemoteException {return clients;}
    //returns the list of all game controllers that are accessible ( not full )

    @Override
    public List<RmiController> getFreeGames() throws RemoteException {
        if( rmi_controllers.isEmpty() ) return null;
        List<RmiController> free = new ArrayList<>();
        for ( Integer id : rmi_controllers.keySet() )
        {if( !rmi_controllers.get(id).getFull() ) free.add(rmi_controllers.get(id));}
        return free;

    }
    //return 0 if the name is free, 1 if the name is already used by an active player, 2 if the player is coming back
    public List<SocketRmiControllerObject> getFreeGamesSocket() throws RemoteException {
        if (rmi_controllers.isEmpty()) return null;
        List<SocketRmiControllerObject> free = new ArrayList<>();
        for (Integer id : rmi_controllers.keySet()) {
            if (!rmi_controllers.get(id).getFull()) {
                RmiController r = rmi_controllers.get(id);
                SocketRmiControllerObject tmp = new SocketRmiControllerObject(r.getController().getGame().getName(),r.getController().getGame().getIndex_game(),r.getController().getGame().getNumPlayer(),r.getController().getGame().getMax_num_player());
                free.add(tmp);
            }
        }
        return free;
    }

        @Override
    public String checkName(String name, VirtualViewF client) throws RemoteException {

        for ( Integer i : rmi_controllers.keySet() )
        {
            for ( Integer j : rmi_controllers.get(i).getController().getGame().getGet_player_index().keySet() )
            {
                Player p = rmi_controllers.get(i).getController().getGame().getGet_player_index().get(j);
                if ( p.getName().equals(name) && p.isDisconnected() ) {
                    rmi_controllers.get(i).wakeUp(name,client);
                    for ( String s : rmi_controllers.get(i).getTtoP().keySet() )
                    {
                        if ( rmi_controllers.get(i).getTtoP().get(s).equals(p) ){
                            return s;
                        }
                    }
                }
                else if (p.getName().equals(name) && !p.isDisconnected() ) { return "false"; }
            }
        }
        return "true";
    }

    private Object waitAnswer(String token, Integer idRequest) throws RemoteException {return token_to_rmi.get(token).getAnswer(idRequest);}

    @Override
    public int getPort(String token) throws RemoteException {

        return token_to_rmi.get(token).getPort();
    }

    @Override
    public boolean findRmiController(Integer game_id, String p_token, String player_name, VirtualViewF client) throws RemoteException {

        RmiController index = rmi_controllers.get(game_id);
        if (index != null && !rmi_controllers.get(game_id).getFull())
        {
            token_to_rmi.put(p_token , index );

            return addPlayer(game_id, p_token, player_name,client);
        }
        String error = "\nWRONG ID : Not Available Game\n";
        token_manager.getTokens().get(p_token).reportError(error);
        return false;
    }

    public boolean findRmiControllerSocket(Integer game_id, String p_token, String player_name) throws RemoteException {

        RmiController index = rmi_controllers.get(game_id);
        if (index != null && !rmi_controllers.get(game_id).getFull())
        {
            token_to_rmi.put(p_token , index );

            return addPlayerSocket(game_id, p_token, player_name);
        }
        String error = "\nWRONG ID : Not Available Game\n";
        token_manager.getTokens().get(p_token).reportError(error);
        return false;
    }

    private void broadcastUpdateThread() throws InterruptedException, RemoteException {
        while ( !updates.isEmpty() ){
            String update = updates.take();
            synchronized (this){

                List<String> tokens = new ArrayList<>();
                RmiController gc = token_to_rmi.get(update);

                for( String t : token_to_rmi.keySet() )
                    if( token_to_rmi.get(t).equals(gc) ) tokens.add(t);

                for(String t: tokens){
                    token_manager.getTokens().get(t).showUpdate( token_to_player.get(update).getGameField() );
                }

            }

        }
    }
    @Override
    public RmiController getRmiController(String token) throws RemoteException{return token_to_rmi.get(token);}
    @Override
    public void receiveHeartbeat(String token) throws RemoteException {lastHeartbeatTime.put(token, System.currentTimeMillis());}

    private synchronized void checkHeartbeats() throws RemoteException{
        long currentTime = System.currentTimeMillis();
        Set<String> keys = lastHeartbeatTime.keySet();
        for (String key : keys) {
            if (currentTime - lastHeartbeatTime.get(key) > HEARTBEAT_TIMEOUT) {
                if(token_to_rmi.get(key).getTtoP().get(key).isDisconnected()) continue;
                token_to_rmi.get(key).getTtoP().get(key).disconnect();
                System.out.println(token_to_rmi.get(key).getTtoP().get(key).getName() + " disconnected");
            }
        }
    }

    private void startHeartbeatChecker() throws RemoteException{
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Controlla gli "heartbeats" ogni 5 secondi
                    checkHeartbeats();
                } catch (InterruptedException | RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setNum(int n){
        common.setNum(n);
    }
    public int getNum(){
        return common.getNum();
    }
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
