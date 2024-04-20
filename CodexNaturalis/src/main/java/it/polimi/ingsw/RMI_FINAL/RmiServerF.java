package it.polimi.ingsw.RMI_FINAL;
import it.polimi.ingsw.CONTROLLER.GameController;
import it.polimi.ingsw.MODEL.Player.Player;
import it.polimi.ingsw.RMI.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RmiServerF implements VirtualServerF {

    private final GameController ctrl;
    private TokenManagerF token_manager = new TokenManagerImplementF();
    private List<VirtualViewF> clients = new ArrayList<>();
    private Map<String, Player> token_to_player = new HashMap<>();
    private Map<String, GameController>  token_to_game = new HashMap<>();
    private List<GameController> controllers = new ArrayList<>();

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
        Player p = ctrl.createPlayer(player_name, first );
        token_to_player.put( client_token, p );
    }

    /*creo un nuovo GController, lo aggiungo alla lista e dopo aver inserito il player al
    nuovo game, inserisco nella mappa token_to_game token e gamecontroller appena creato*/
    @Override
    public void createGame(int num_player, String p_token) throws RemoteException {
        GameController game_controller = new GameController(num_player);
        controllers.add(game_controller);
        game_controller.getGame().insertPlayer(token_to_player.get(p_token));
        token_to_game.put( p_token, game_controller);
    }

    @Override
    public void addPlayer() throws RemoteException {

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
