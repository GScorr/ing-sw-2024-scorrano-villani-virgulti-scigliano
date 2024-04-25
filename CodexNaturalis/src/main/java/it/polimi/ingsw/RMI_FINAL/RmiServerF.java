package it.polimi.ingsw.RMI_FINAL;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Card.PlayCard;
import it.polimi.ingsw.MODEL.Card.StartingCard;
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
    private TokenManagerF token_manager = new TokenManagerImplementF();
    private List<VirtualViewF> clients = new ArrayList<>();
    private Map<String, Player> token_to_player = new HashMap<>();
    private Map<String, RmiController>  token_to_rmi = new HashMap<>();
    //private List<GameController> controllers = new ArrayList<>();
    private Map<Integer , RmiController> rmi_controllers = new HashMap<>();

    //todo da modificare una volta capito che tipo di update invia
    private BlockingQueue<String> updates = new ArrayBlockingQueue<>(20);



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
        Player p = serverino.createPlayer(p_token,player_name,true);
        token_to_player.put( p_token , p);
        rmi_controllers.put(serverino.getController().getGame().getIndex_game() , serverino);
        token_to_rmi.put( p_token, serverino);
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

        return rmi_controllers.get(game_id).addPlayer(p_token,name);
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

    //Look if there are clients with the same name, return true if it's available false otherwise
    @Override
    public boolean checkName(String name, String token) throws RemoteException {
        for ( String t : token_to_player.keySet() ){
            if( token_to_player.get(t).getName().equals(name) ){
                String error = " Name Already Existing ";
                token_manager.getTokens().get(token).reportError(error);
                return false; }
        }
        return true;
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
        //ritorna true se ho raggiunto i player
        return token_to_rmi.get(token).getFull();
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

    public static void main(String[] args) throws RemoteException {
        final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF();
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : server connected. ");
    }

}
