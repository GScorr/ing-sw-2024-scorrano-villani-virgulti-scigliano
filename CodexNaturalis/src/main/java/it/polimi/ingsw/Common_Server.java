package it.polimi.ingsw;

import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.*;
import it.polimi.ingsw.SOCKET_FINAL.Server;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Common_Server {
    private static final long HEARTBEAT_TIMEOUT = 1500;
    private TokenManagerF token_manager = new TokenManagerImplementF();
    private List<VirtualViewF> clients = new ArrayList<>();
    private static int port;
    private Map<String, Player> token_to_player = new HashMap<>();
    private Map<String, GameServer>  token_to_rmi = new HashMap<>();
    private Map<Integer , GameServer> rmi_controllers = new HashMap<>();
    private final Map<String, Long> lastHeartbeatTime = new HashMap<>();
    public Common_Server(){}

    public String createToken(VirtualViewF client) throws IOException {return token_manager.generateToken(client);}
    public String createTokenSocket(VirtualView client) throws IOException {return token_manager.generateTokenSocket(client);}
    public Map<String, Player> getTtoP() throws IOException {return token_to_player;}
    public Map<String, GameServer> getTtoR() throws IOException {return token_to_rmi;}
    public Map<Integer, GameServer> getListRmiController() throws IOException {return rmi_controllers;}

    public int createGame(String name, int num_player, String p_token, String player_name, VirtualViewF client) throws IOException, InterruptedException {
        int port = getAvailablePort();
        GameServer gameServer = new GameServer(name,num_player,port,this);
        gameServer.addPlayer(p_token,player_name, client,true);
        VirtualGameServer serverStub = (VirtualGameServer) UnicastRemoteObject.exportObject(gameServer, 0);
        Registry registry = LocateRegistry.createRegistry(port); // Connect to existing registry
        registry.rebind(String.valueOf(port), serverStub);
        token_to_rmi.put( p_token, gameServer);
        rmi_controllers.put(gameServer.getController().getGame().getIndex_game(), gameServer);
        //token_manager.getTokens().get(p_token).setState(token_to_player.get(p_token).getActual_state().getNameState());
        return port;
    }

    private int getAvailablePort(){port++;return port;}
    public boolean addPlayer(Integer game_id, String p_token, String name, VirtualViewF client) throws IOException, InterruptedException {rmi_controllers.get(game_id).addPlayer(p_token,name, client,false);return true;}
    public boolean addPlayerSocket(Integer game_id, String p_token, String name, VirtualView client) throws IOException, InterruptedException {
        rmi_controllers.get(game_id).addPlayerSocket(p_token,name,client,false);
        return true;}

    public List<VirtualViewF> getListClient() throws IOException {return clients;}
    public List<GameServer> getFreeGames() throws IOException {
        if( rmi_controllers.isEmpty() ) return null;
        List<GameServer> free = new ArrayList<>();
        for ( Integer id : rmi_controllers.keySet() )
        {if( !rmi_controllers.get(id).getFull() ) free.add(rmi_controllers.get(id));}
        return free;

    }

    public List<SocketRmiControllerObject> getFreeGamesSocket() throws IOException {
        if (rmi_controllers.isEmpty()) return null;
        List<SocketRmiControllerObject> free = new ArrayList<>();
        for (Integer id : rmi_controllers.keySet()) {
            if (!rmi_controllers.get(id).getFull()) {
                GameServer r = rmi_controllers.get(id);
                SocketRmiControllerObject tmp = new SocketRmiControllerObject(r.getController().getGame().getName(),r.getController().getGame().getIndex_game(),r.getController().getGame().getNumPlayer(),r.getController().getGame().getMax_num_player());
                free.add(tmp);
            }
        }
        return free;
    }

    public String checkName(String name, VirtualViewF client) throws IOException, InterruptedException {

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

    public int getPort(String token) throws IOException {
        return token_to_rmi.get(token).getPort();
    }

    public synchronized void  removeGameServer(GameServer gs) throws NoSuchObjectException {

        for( Integer i : rmi_controllers.keySet() ){
            if( rmi_controllers.get(i).equals(gs) )
            {
                System.out.println("GAME SERVER DELETE -> " + rmi_controllers.get(i));
                UnicastRemoteObject.unexportObject(rmi_controllers.get(i), true);
                rmi_controllers.remove(i);
            }
        }

        token_to_rmi.keySet().removeIf(tok -> token_to_rmi.get(tok).equals(gs));
    }

    public boolean findRmiController(Integer game_id, String p_token, String player_name, VirtualViewF client) throws IOException, InterruptedException {

        GameServer index = rmi_controllers.get(game_id);
        if (index != null && !rmi_controllers.get(game_id).getFull())
        {
            token_to_rmi.put(p_token , index );

            return addPlayer(game_id, p_token, player_name,client);
        }
        String error = "\nWRONG ID : Not Available Game\n";
        token_manager.getTokens().get(p_token).reportError(error);
        return false;
    }
    public GameServer getRmiController(String token) throws IOException{return token_to_rmi.get(token);}

    public void receiveHeartbeat(String token) throws IOException {lastHeartbeatTime.put(token, System.currentTimeMillis());}

    private synchronized void checkHeartbeats() throws IOException{
        long currentTime = System.currentTimeMillis();
        Set<String> keys = lastHeartbeatTime.keySet();
        for (String key : keys) {
            if (currentTime - lastHeartbeatTime.get(key) > HEARTBEAT_TIMEOUT) {
                try{
                    if(token_to_rmi.get(key)!=null ){if( token_to_rmi.get(key).getTtoP().get(key).isDisconnected()) continue;
                    token_to_rmi.get(key).getTtoP().get(key).disconnect();
                    System.out.println(token_to_rmi.get(key).getTtoP().get(key).getName() + " disconnected");
                    token_manager.deleteVW(key);}
                        if(token_to_rmi.get(key)!=null ){try{token_to_rmi.get(key).checkEndDisconnect();}catch (ConcurrentModificationException ignored){}
                    token_to_rmi.get(key).clientsRMI.remove( token_to_rmi.get(key).token_manager.getTokens().get(key)  );
                    token_to_rmi.get(key).token_manager.deleteVW(key);}}catch (NullPointerException ignore){}
            }
        }
    }

    public void startHeartbeatChecker() throws IOException{
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000); // Controlla gli "heartbeats" ogni 5 secondi
                    checkHeartbeats();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public synchronized void connect(VirtualViewF client)throws IOException{this.clients.add(client);}

    public static void main(String[] args) throws IOException, NotBoundException {

        Common_Server common = new Common_Server();

        final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF(common);
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : RMI server connected. ");


        int port = 12345;
        ServerSocket listenSocket = new ServerSocket(port);
        System.out.println("[SUCCESSFUL] : SOCKET server is running...");
        new Server(listenSocket,  common).runServer();
    }
}


