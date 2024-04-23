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

    private GameController ctrl;
    private TokenManagerF token_manager = new TokenManagerImplementF();
    private List<VirtualViewF> clients = new ArrayList<>();
    private Map<String, Player> token_to_player = new HashMap<>();
    private Map<String, GameController>  token_to_game = new HashMap<>();
    private List<GameController> controllers = new ArrayList<>();

    //todo da modificare una volta capito che tipo di update invia
    private BlockingQueue<String> updates = new ArrayBlockingQueue<>(20);


    public RmiServerF(GameController controller) {
        this.ctrl = controller;
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
    public Map<String, GameController> getTtoG() throws RemoteException {
        return token_to_game;
    }

    @Override
    public List<GameController> getListGameController() throws RemoteException {
        return controllers;
    }

    //todo: decidere come scegliere il colore, per ora lascio sempre green (come da controller)
    @Override
    public void CreatePlayer(String player_name, String client_token, boolean first) throws RemoteException {
        //ctrl = new GameController(4);
        Player p = ctrl.createPlayer(player_name, first );
        token_to_player.put( client_token, p );
    }

    /*create a new GController, add it to the list and after insert player to the new game
    , add token and gcontroller in the token to controller*/
    @Override
    public void createGame(String name, int num_player, String p_token, String player_name) throws RemoteException {
        GameController game_controller = new GameController(name, num_player);
        token_to_player.put( p_token , game_controller.createPlayer(player_name, true) );
        //game_controller.getGame().insertPlayer(token_to_player.get(p_token));
        //game_controller.checkNumPlayer();
        controllers.add(game_controller);
        token_to_game.put( p_token, game_controller);
    }

    /*check if the id is valid, if not I send back a report error, otherwise i insert the
     player in the given game */
    @Override
    public boolean addPlayer(int game_id, String p_token, String name) throws RemoteException {
        int index = controllers.stream()
                .filter(gc -> gc.getGame().getIndex_game() == game_id)
                .findFirst()
                .map(controllers::indexOf)
                .orElse(-1);
        if (index != -1) {
            if( controllers.get(index).getFull() )
                {String error = "\nGame is Full\n";
                token_manager.getTokens().get(p_token).reportError(error);
                return false;}
            token_to_player.put( p_token , controllers.get(index).createPlayer(name, false) );
            //controllers.get(index).checkNumPlayer();
            token_to_game.put(p_token , controllers.get(index) );
            return true;
        }
        String error = "\nWRONG ID : Not Existing Game\n";
        token_manager.getTokens().get(p_token).reportError(error);
        return false;
    }

    @Override
    public List<VirtualViewF> getListClient() throws RemoteException {
        return clients;
    }

    //returns the list of all game controllers that are accessible ( not full )
    @Override
    public List<GameController> getFreeGames() throws RemoteException {
        if( controllers.isEmpty() ) return null;
        List<GameController> free = new ArrayList<>();
        for ( GameController gc : controllers )
            if( !gc.getFull() ) free.add(gc);
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
        token_to_game.get(p_token).playerChooseGoal( token_to_player.get(p_token), goal_index );
    }

    @Override
    public void selectStartingCard(String p_token, boolean flipped) throws RemoteException {
        token_to_game.get(p_token).playerSelectStartingCard( token_to_player.get(p_token) , flipped );
    }

    @Override
    public boolean checkFull(String token) throws RemoteException {
        //ritorna true se ho raggiunto i player
        return token_to_game.get(token).getFull();
    }

    @Override
    public void showStartingCard(String token) throws RemoteException {
        PlayCard card = token_to_player.get(token).getStartingCard();

    }

    private void broadcastUpdateThread() throws InterruptedException, RemoteException {
        while ( !updates.isEmpty() ){
            String update = updates.take();
            synchronized (this){

                List<String> tokens = new ArrayList<>();
                GameController gc = token_to_game.get(update);

                for( String t : token_to_game.keySet() )
                    if( token_to_game.get(t).equals(gc) ) tokens.add(t);

                for(String t: tokens){
                    token_manager.getTokens().get(t).showUpdate( token_to_player.get(update).getGameField() );
                }

            }

        }
    }



    public static void main(String[] args) throws RemoteException {
        final String serverName = "VirtualServer";
        VirtualServerF server = new RmiServerF(new GameController(4));         //modifica
        VirtualServerF stub = (VirtualServerF) UnicastRemoteObject.exportObject(server,0);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName,stub);
        System.out.println("[SUCCESSFUL] : server connected. ");
    }

}
