package it.polimi.ingsw.RMI_FINAL;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.StartingCard;
import it.polimi.ingsw.MODEL.Game.IndexRequestManagerF;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI.*;

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
    private TokenManagerF token_manager = new TokenManagerImplementF();
    private List<VirtualViewF> clients = new ArrayList<>();
    private Map<String, Player> token_to_player = new HashMap<>();
    private Map<String, RmiController>  token_to_rmi = new HashMap<>();
    //private List<GameController> controllers = new ArrayList<>();
    private Map<Integer , RmiController> rmi_controllers = new HashMap<>();

    //todo da modificare una volta capito che tipo di update invia
    private BlockingQueue<String> updates = new ArrayBlockingQueue<>(20);
    private final Map<String, Long> lastHeartbeatTime = new HashMap<>();

    public RmiServerF() throws RemoteException {
        startHeartbeatChecker();
    }

    //put the client in the clients list
    @Override
    public synchronized void connect(VirtualViewF client)throws RemoteException{
            this.clients.add(client);
    }

    //given a client create the token that will represent it
    @Override
    public String createToken(VirtualViewF client) throws RemoteException {
        return token_manager.generateToken(client);
    }

    @Override
    public Map<String, Player> getTtoP() throws RemoteException {
        return token_to_player;
    }

    @Override
    public Map<String, RmiController> getTtoR() throws RemoteException {
        return token_to_rmi;
    }


    public Map<Integer, RmiController> getListRmiController() throws RemoteException {
        return rmi_controllers;
    }

    //todo: decidere come scegliere il colore, per ora lascio sempre green (come da controller)

    /*create a new GController, add it to the list and after insert player to the new game
    , add token and gcontroller in the token to controller*/
    @Override
    public RmiController createGame(String name, int num_player, String p_token, String player_name) throws RemoteException {
        RmiController serverino = new RmiController(name, num_player);
        token_to_rmi.put( p_token, serverino);
        Integer idRequest = IndexRequestManagerF.getNextIndex();
        serverino.addtoQueue("createPlayer",idRequest,new Wrapper(p_token,player_name,true));
        Player p = (Player) waitAnswer(p_token,idRequest);
        token_to_player.put( p_token , p);
        //Player p = serverino.createPlayer(p_token,player_name,true); /old method
        Integer idRequest2 = IndexRequestManagerF.getNextIndex();
        serverino.addtoQueue("getIndexGame",idRequest2,null);
        rmi_controllers.put((Integer) waitAnswer(p_token, idRequest2), serverino);
        return serverino;
    }



    /*check if the id is valid, if not I send back a report error, otherwise i insert the
     player in the given game */
    @Override
    public boolean addPlayer(Integer game_id, String p_token, String name) throws RemoteException {
       /* RmiController index = rmi_controllers.get(game_id);
        if (index != null) {
            if( index.getController().getFull() )
                {String error = "\nGame is Full\n";
                token_manager.getTokens().get(p_token).reportError(error);
                return false;}
            token_to_player.put( p_token , index.getController().createPlayer(name, false) );
            index.getController().checkNumPlayer();
            token_to_rmi.put(p_token , index );
            return true;
        }
        String error = "\nWRONG ID : Not Existing Game\n";
        token_manager.getTokens().get(p_token).reportError(error);
        return false;*/
        Integer idRequest = IndexRequestManagerF.getNextIndex();
        rmi_controllers.get(game_id).addtoQueue("addPlayer", idRequest, new Wrapper(p_token,name));
        return (boolean) waitAnswer(p_token,idRequest);
        //return rmi_controllers.get(game_id).addPlayer(p_token,name);
    }

    @Override
    public List<VirtualViewF> getListClient() throws RemoteException {
        return clients;
    }

    //returns the list of all game controllers that are accessible ( not full )
    @Override
    public List<RmiController> getFreeGames() throws RemoteException {
        if( rmi_controllers.isEmpty() ) return null;
        List<RmiController> free = new ArrayList<>();
        for ( Integer id : rmi_controllers.keySet() )
            if( !rmi_controllers.get(id).getFull() ) free.add(rmi_controllers.get(id));
        return free;
    }

    //todo
    @Override
    public void insertCard(String p_token, PlayCard card, int pos_x, int pos_y, int index) throws RemoteException, InterruptedException {

        System.out.println("\n [Insert request received] \n");
        //todo cambia gestione flipped
        //token_to_game.get(p_token).statePlaceCard(token_to_player.get(p_token), index, true, pos_x, pos_y );

        try
        {
            updates.put(p_token);
        }catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        //todo come gestire il broadcast per non far intasare il client
        broadcastUpdateThread();
    }

    //return 0 if the name is free, 1 if the name is already used by an active player, 2 if the player is coming back
    @Override
    public String checkName(String name) throws RemoteException {

        for ( Integer i : rmi_controllers.keySet() )
        {
            for ( Integer j : rmi_controllers.get(i).getController().getGame().getGet_player_index().keySet() )
            {
                Player p = rmi_controllers.get(i).getController().getGame().getGet_player_index().get(j);
                if ( p.getName().equals(name) && p.isDisconnected() ) {
                    p.connect();
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


    @Override
    public void chooseGoal(String p_token, int goal_index) throws RemoteException {
        getRmiController(p_token).chooseGoal(p_token, goal_index);
    }

    @Override
    public void selectStartingCard(String p_token, boolean flipped) throws RemoteException {
        token_to_rmi.get(p_token).getController().playerSelectStartingCard( token_to_player.get(p_token) , flipped );
    }

    @Override
    public boolean checkFull(String token) throws RemoteException {
        Integer idRequest = IndexRequestManagerF.getNextIndex();
        Wrapper wrap = new Wrapper();
        //ritorna true se ho raggiunto i player
        token_to_rmi.get(token).addtoQueue("getFull",idRequest,wrap);
        Boolean boo = (boolean) waitAnswer(token,idRequest);
        return boo;
    }

    private Object waitAnswer(String token, Integer idRequest) throws RemoteException {
            return token_to_rmi.get(token).getAnswer(idRequest);
    }

    @Override
    public void showStartingCard(String token) throws RemoteException {
        PlayCard card = getRmiController(token).getTtoP().get(token).getStartingCard();
        token_manager.getTokens().get(token).showCard(card);
    }

    @Override
    public boolean findRmiController(Integer game_id, String p_token, String player_name) throws RemoteException {

        RmiController index = rmi_controllers.get(game_id);
        if (index != null && !rmi_controllers.get(game_id).getFull())
        {
            token_to_rmi.put(p_token , index );

            return addPlayer(game_id, p_token, player_name);
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
    public RmiController getRmiController(String token) throws RemoteException{
        return token_to_rmi.get(token);
    }

    @Override
    public void chooseStartingCard(String token, boolean flip) throws RemoteException {
        getRmiController(token).chooseStartingCard(token, flip);
    }

    @Override
    public void receiveHeartbeat(String token) throws RemoteException {
        lastHeartbeatTime.put(token, System.currentTimeMillis());
    }
    private synchronized void checkHeartbeats() throws RemoteException{
        long currentTime = System.currentTimeMillis();
        Set<String> keys = lastHeartbeatTime.keySet();
        for (String key : keys) {
            if (currentTime - lastHeartbeatTime.get(key) > HEARTBEAT_TIMEOUT) {
                if(token_to_rmi.get(key).getTtoP().get(key).isDisconnected()) continue;
                token_to_rmi.get(key).getTtoP().get(key).disconnect();
                System.out.println(token_to_rmi.get(key).getTtoP().get(key).getName() + " frate me so disconnected");
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

    public static void main(String[] args) throws RemoteException {
        final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF();
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : server connected. ");
    }

}
