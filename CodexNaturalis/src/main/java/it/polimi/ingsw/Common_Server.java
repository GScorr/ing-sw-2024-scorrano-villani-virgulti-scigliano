package it.polimi.ingsw;

import it.polimi.ingsw.CONSTANTS.Constants;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI_FINAL.*;
import it.polimi.ingsw.SOCKET_FINAL.ServerSocket;
import it.polimi.ingsw.SOCKET_FINAL.VirtualView;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * This class represents the serverSocket-side application. It manages game creation, player connections,
 * communication with clients (both RMI and Socket), and disconnections.
 * It also implements a heartbeat to detect inactive clients.
 */
public class Common_Server {

    private static final long HEARTBEAT_TIMEOUT = 6000;
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

    /**
     * Creates a new game with the specified parameters.
     *
     * @param name The name of the game.
     * @param num_player The number of players in the game.
     * @param p_token The token of the player creating the game.
     * @param player_name The name of the player creating the game.
     * @param client The client requesting game creation.
     * @return The port number where the game serverSocket is listening.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     */
    public int createGame(String name, int num_player, String p_token, String player_name, VirtualViewF client) throws IOException, InterruptedException {
        int port = getAvailablePort();
        GameServer gameServer = new GameServer(name,num_player,port,this);
        gameServer.addPlayer(p_token,player_name, client,true);
        VirtualGameServer serverStub = (VirtualGameServer) UnicastRemoteObject.exportObject(gameServer, 0);
        Registry registry = LocateRegistry.createRegistry(port); // Connect to existing registry
        registry.rebind(String.valueOf(port), serverStub);
        token_to_rmi.put( p_token, gameServer);
        rmi_controllers.put(gameServer.getController().getGame().getIndex_game(), gameServer);
        return port;
    }

    private int getAvailablePort(){port++;return port;}

    /**
     * Adds a player to a game identified by its ID.
     *
     * @param game_id The unique identifier of the game to add the player to.
     * @param p_token A player token used for authorization.
     * @param name The name of the player to be added.
     * @param client A reference to the player's virtual view object, likely used for rendering game information to the player.
     * @return  `true` if the player was successfully added to the game, `false` otherwise. (implementation specific)
     * @throws IOException  If an I/O error occurs while communicating with the remote game serverSocket.
     * @throws InterruptedException If the calling thread is interrupted while waiting for a response from the serverSocket.
     */
    public boolean addPlayer(Integer game_id, String p_token, String name, VirtualViewF client) throws IOException, InterruptedException {
        rmi_controllers.get(game_id).addPlayer(p_token,name, client,false);
        return true;
    }

    public boolean addPlayerSocket(Integer game_id, String p_token, String name, VirtualView client) throws IOException, InterruptedException {
        rmi_controllers.get(game_id).addPlayerSocket(p_token,name,client,false);
        return true;
    }

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

    /**
     * Checks if a player exists and wakes them up if disconnected.
     * Iterates through RMI controllers and their games to find a matching player.
     *
     * @param name The player's name.
     * @param client The VirtualViewF client.
     * @return "true" if no player found, "false" if connected player found, or session ID of woken player.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If interrupted.
     */
    public String checkName(String name, VirtualViewF client) throws IOException, InterruptedException {
        for ( Integer i : rmi_controllers.keySet() ) {
            for ( Integer j : rmi_controllers.get(i).getController().getGame().getGet_player_index().keySet() ) {
                Player p = rmi_controllers.get(i).getController().getGame().getGet_player_index().get(j);
                if ( p.getName().equals(name) && p.isDisconnected() ) {
                    rmi_controllers.get(i).wakeUp(name,client);
                    for ( String s : rmi_controllers.get(i).getTtoP().keySet() ) {
                        if ( rmi_controllers.get(i).getTtoP().get(s).equals(p) ){
                            return s;
                        }
                    }
                }
                else if (p.getName().equals(name) && !p.isDisconnected() ) {
                    return "false"; }
            }
        }
        return "true";
    }

    public int getPort(String token) throws IOException {
        return token_to_rmi.get(token).getPort();
    }

    /**
     * Removes a game serverSocket from the serverSocket list.
     *
     * @param gs The GameServer object to remove.
     * @throws NoSuchObjectException If the game serverSocket is not found in the registry.
     */
    public synchronized void  removeGameServer(GameServer gs) throws NoSuchObjectException {

        for( Integer i : rmi_controllers.keySet() ){
            if( rmi_controllers.get(i).equals(gs) ) {
                System.out.println("GAME SERVER DELETE -> " + rmi_controllers.get(i));
                UnicastRemoteObject.unexportObject(rmi_controllers.get(i), true);
                rmi_controllers.remove(i);
            }
        }
        token_to_rmi.keySet().removeIf(tok -> token_to_rmi.get(tok).equals(gs));
    }

    /**
     * Attempts to find an RMI controller for a specific game and adds a player.
     *
     *
     * @param game_id The ID of the game.
     * @param p_token The player's token.
     * @param player_name The player's name.
     * @param client The VirtualViewF client.
     * @return True if the player is added successfully, false otherwise.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If interrupted.
     */
    public boolean findRmiController(Integer game_id, String p_token, String player_name, VirtualViewF client) throws IOException, InterruptedException {

        GameServer index = rmi_controllers.get(game_id);

        if (index != null && !rmi_controllers.get(game_id).getFull()) {
            token_to_rmi.put(p_token , index );
            return addPlayer(game_id, p_token, player_name,client);
        }
        String error = "\nWRONG ID : Not Available Game\n";
        token_manager.getTokens().get(p_token).reportError(error);
        return false;
    }

    public GameServer getRmiController(String token) throws IOException{return token_to_rmi.get(token);}

    /**
     * Updates the last heartbeat time for a player identified by their token.
     *
     * This method is likely called upon receiving a heartbeat message from a player.
     * It stores the current system time in milliseconds for the given player token
     * in the `lastHeartbeatTime` map.
     *
     * @param token The player's token.
     * @throws IOException If an I/O error occurs while updating the map.
     */
    public void receiveHeartbeat(String token) throws IOException {
        lastHeartbeatTime.put(token, System.currentTimeMillis());
    }

    /**
     * This method periodically checks for inactive clients by monitoring their heartbeat times.
     *
     * @throws IOException If there's an error communicating with disconnected clients.
     */
    private synchronized void checkHeartbeats() throws IOException{
        long currentTime = System.currentTimeMillis();
        Set<String> keys = lastHeartbeatTime.keySet();
        for (String key : keys) {
            if (currentTime - lastHeartbeatTime.get(key) > HEARTBEAT_TIMEOUT && lastHeartbeatTime.get(key)!=0) {
                try{
                    if(token_to_rmi.get(key)!=null ){if( token_to_rmi.get(key).getTtoP().get(key).isDisconnected()) continue;
                    token_to_rmi.get(key).getTtoP().get(key).disconnect();
                    lastHeartbeatTime.put(key,0L);
                    System.out.println(token_to_rmi.get(key).getTtoP().get(key).getName() + " just disconnected");
                    token_manager.deleteVW(key);}
                        if(token_to_rmi.get(key)!=null ){try{token_to_rmi.get(key).checkEndDisconnect();}catch (ConcurrentModificationException ignored){}
                    token_to_rmi.get(key).clientsRMI.remove( token_to_rmi.get(key).token_manager.getTokens().get(key)  );
                    token_to_rmi.get(key).token_manager.deleteVW(key);}}catch (NullPointerException ignore){}
            }
        }
    }

    /**
     * Starts a background thread to periodically check for inactive clients based on their heartbeat times.
     *
     * @throws IOException If there's an error during heartbeat checking or communication with disconnected clients.
     */
    public void startHeartbeatChecker() throws IOException{
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000); // check heartbeats every 5 seconds
                    checkHeartbeats();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Establishes a connection with a new VirtualViewF client. This method likely adds the client to a collection for managing connections.
     *
     * @param client The VirtualViewF object representing the client to connect.
     * @throws IOException If there's an error during the connection process.
     * @throws IllegalStateException If the connection is attempted after the serverSocket has been stopped.
     */
    public synchronized void connect(VirtualViewF client)throws IOException{
        this.clients.add(client);
    }

    public static void main(String[] args) throws IOException, NotBoundException {


        Scanner scan = new Scanner(System.in);

        System.out.println("Choose the correct IP address, if you click 'y' (or 'Y') you will be asked to insert the IP address, if you click any other button you will continue.");
        String ipChoice = scan.nextLine();
        if (ipChoice.equalsIgnoreCase("y")) {
            System.out.println("Please enter the IP address:");
            String ipAddress = scan.nextLine();
            System.out.println("IP address set to: " + ipAddress);
            Constants.IPV4 = ipAddress;
        }

        Common_Server common = new Common_Server();

        final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF(common);
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : RMI serverSocket connected. ");


        int port = 12345;
        java.net.ServerSocket listenSocket = new java.net.ServerSocket(port);
        System.out.println("[SUCCESSFUL] : SOCKET serverSocket is running...");
        new ServerSocket(listenSocket,  common).runServer();
    }
}
